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
package com.intellij.plugins.haxe.ide.annotator;

import consulo.language.editor.annotation.AnnotationHolder;
import consulo.language.editor.annotation.Annotator;
import com.intellij.plugins.haxe.HaxeBundle;
import com.intellij.plugins.haxe.ide.actions.HaxeTypeAddImportIntentionAction;
import com.intellij.plugins.haxe.ide.index.HaxeComponentIndex;
import com.intellij.plugins.haxe.lang.psi.HaxeComponent;
import com.intellij.plugins.haxe.lang.psi.HaxeReferenceExpression;
import com.intellij.plugins.haxe.lang.psi.HaxeType;
import com.intellij.plugins.haxe.lang.psi.HaxeVisitor;
import com.intellij.plugins.haxe.util.HaxeResolveUtil;
import consulo.language.psi.PsiElement;
import consulo.language.psi.scope.GlobalSearchScope;

import javax.annotation.Nonnull;

import java.util.List;

/**
 * @author: Fedor.Korotkov
 */
public class HaxeTypeAnnotator extends HaxeVisitor implements Annotator {
  private AnnotationHolder myHolder = null;

  @Override
  public void annotate(@Nonnull PsiElement element, @Nonnull AnnotationHolder holder) {
    assert myHolder == null;
    myHolder = holder;
    try {
      element.accept(this);
    }
    finally {
      myHolder = null;
    }
  }

  @Override
  public void visitType(@Nonnull HaxeType type) {
    super.visitType(type);
    final HaxeReferenceExpression expression = type.getReferenceExpression();
    if (expression.resolve() != null) {
      return;
    }

    tryCreateAnnotation(expression);
  }

  @Override
  public void visitReferenceExpression(@Nonnull HaxeReferenceExpression expression) {
    super.visitReferenceExpression(expression);

    if (expression.resolve() == null) {
      tryCreateAnnotation(expression);
    }
  }

  private void tryCreateAnnotation(HaxeReferenceExpression expression) {
    final GlobalSearchScope scope = HaxeResolveUtil.getScopeForElement(expression);
    final List<HaxeComponent> components =
      HaxeComponentIndex.getItemsByName(expression.getText(), expression.getProject(), scope);
    if (!components.isEmpty()) {
      myHolder.createErrorAnnotation(expression, HaxeBundle.message("haxe.unresolved.type"))
        .registerFix(new HaxeTypeAddImportIntentionAction(expression, components));
    }
  }
}
