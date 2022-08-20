/*
 * Copyright 2000-2013 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intellij.plugins.haxe.ide.info;

import com.intellij.plugins.haxe.HaxeComponentType;
import com.intellij.plugins.haxe.HaxeLanguage;
import com.intellij.plugins.haxe.lang.psi.*;
import com.intellij.plugins.haxe.util.UsefulPsiTreeUtil;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.editor.completion.lookup.LookupElement;
import consulo.language.editor.parameterInfo.*;
import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.util.collection.ArrayUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author: Fedor.Korotkov
 */
@ExtensionImpl
public class HaxeParameterInfoHandler implements ParameterInfoHandler<PsiElement, HaxeFunctionDescription> {
  String myParametersListPresentableText = "";

  @Override
  public boolean couldShowInLookup() {
    return true;
  }

  @Override
  public Object[] getParametersForLookup(LookupElement item, ParameterInfoContext context) {
    final Object o = item.getObject();
    if (o instanceof PsiElement) {
      final PsiElement element = (PsiElement) o;
      final HaxeComponentType type = HaxeComponentType.typeOf(element.getParent());
      if (type == HaxeComponentType.METHOD) {
        return new Object[]{element.getParent()};
      }
    }
    return ArrayUtil.EMPTY_OBJECT_ARRAY;
  }

  @Override
  public PsiElement findElementForParameterInfo(CreateParameterInfoContext context) {
    final PsiElement at = context.getFile().findElementAt(context.getEditor().getCaretModel().getOffset());
    return PsiTreeUtil.getParentOfType(at, HaxeCallExpression.class);
  }

  @Override
  public PsiElement findElementForUpdatingParameterInfo(UpdateParameterInfoContext context) {
    return context.getFile().findElementAt(context.getEditor().getCaretModel().getOffset());
  }

  @Override
  public void showParameterInfo(@Nonnull PsiElement element, CreateParameterInfoContext context) {
    assert element instanceof HaxeCallExpression;
    final HaxeFunctionDescription functionDescription = tryGetDescription((HaxeCallExpression) element);
    if (functionDescription != null && functionDescription.getParameters().length > 0) {
      context.setItemsToShow(new Object[]{functionDescription});
      context.showHint(element, element.getTextRange().getStartOffset(), this);
    }
  }

  @Nullable
  private static HaxeFunctionDescription tryGetDescription(HaxeCallExpression callExpression) {
    final PsiElement target = ((HaxeReference) callExpression.getExpression()).resolve();
    final PsiElement targetParent = target == null ? null : target.getParent();
    if (target instanceof HaxeComponentName && targetParent instanceof HaxeNamedComponent) {
      final HaxeReference[] references = PsiTreeUtil.getChildrenOfType(callExpression.getExpression(), HaxeReference.class);
      final HaxeClassResolveResult resolveResult = (references != null && references.length == 2)
          ? references[0].resolveHaxeClass()
          : HaxeClassResolveResult
          .create(PsiTreeUtil.getParentOfType(callExpression, HaxeClass.class));
      return HaxeFunctionDescription.createDescription((HaxeNamedComponent) targetParent, resolveResult);
    }
    return null;
  }

  @Override
  public void updateParameterInfo(@Nonnull PsiElement place, UpdateParameterInfoContext context) {
    final HaxeExpressionList expressionList = PsiTreeUtil.getParentOfType(place, HaxeExpressionList.class, false);
    int parameterIndex = -1;
    if (place == expressionList) {
      assert expressionList != null;
      final HaxeFunctionDescription functionDescription = tryGetDescription((HaxeCallExpression) expressionList.getParent());
      // the last one
      parameterIndex = functionDescription == null ? -1 : functionDescription.getParameters().length - 1;
    } else if (expressionList != null) {
      for (HaxeExpression expression : expressionList.getExpressionList()) {
        ++parameterIndex;
        if (expression.getTextRange().getEndOffset() >= place.getTextOffset()) {
          break;
        }
      }
    } else if (UsefulPsiTreeUtil.getPrevSiblingSkipWhiteSpacesAndComments(place, true) instanceof HaxeExpressionList) {
      // seems foo(param1, param2<caret>)
      final PsiElement prevSibling = UsefulPsiTreeUtil.getPrevSiblingSkipWhiteSpacesAndComments(place, true);
      assert prevSibling != null;
      final HaxeFunctionDescription functionDescription = tryGetDescription((HaxeCallExpression) prevSibling.getParent());
      parameterIndex = functionDescription == null ? -1 : functionDescription.getParameters().length - 1;
    } else if (PsiTreeUtil.getParentOfType(place, HaxeCallExpression.class, true) != null) {
      // seems foo(<caret>)
      parameterIndex = 0;
    }
    context.setCurrentParameter(parameterIndex);

    if (context.getParameterOwner() == null) {
      context.setParameterOwner(place);
    } else if (context.getParameterOwner() != PsiTreeUtil.getParentOfType(place, HaxeCallExpression.class)) {
      context.removeHint();
      return;
    }
    final Object[] objects = context.getObjectsToView();

    for (int i = 0; i < objects.length; i++) {
      context.setUIComponentEnabled(i, true);
    }
  }

  @Override
  public void updateUI(HaxeFunctionDescription p, ParameterInfoUIContext context) {
    if (p == null) {
      context.setUIComponentEnabled(false);
      return;
    }
    myParametersListPresentableText = p.getParametersListPresentableText();
    context.setupUIComponentPresentation(
        myParametersListPresentableText,
        p.getParameterRange(context.getCurrentParameterIndex()).getStartOffset(),
        p.getParameterRange(context.getCurrentParameterIndex()).getEndOffset(),
        !context.isUIComponentEnabled(),
        false,
        false,
        context.getDefaultParameterColor()
    );
  }

  @Nonnull
  @Override
  public Language getLanguage() {
    return HaxeLanguage.INSTANCE;
  }
}
