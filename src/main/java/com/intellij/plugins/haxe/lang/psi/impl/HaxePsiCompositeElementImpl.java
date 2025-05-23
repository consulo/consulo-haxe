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
package com.intellij.plugins.haxe.lang.psi.impl;

import consulo.language.ast.IElementType;
import consulo.language.impl.psi.ASTWrapperPsiElement;
import consulo.language.ast.ASTNode;
import com.intellij.plugins.haxe.lang.psi.*;
import com.intellij.plugins.haxe.util.UsefulPsiTreeUtil;
import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.language.psi.resolve.PsiScopeProcessor;
import consulo.language.psi.resolve.ResolveState;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HaxePsiCompositeElementImpl extends ASTWrapperPsiElement implements HaxePsiCompositeElement {
  public HaxePsiCompositeElementImpl(@Nonnull ASTNode node) {
    super(node);
  }

  public IElementType getTokenType() {
    return getNode().getElementType();
  }

  public String toString() {
    return getTokenType().toString();
  }

  @Override
  public boolean processDeclarations(@Nonnull PsiScopeProcessor processor,
                                     @Nonnull ResolveState state,
                                     PsiElement lastParent,
                                     @Nonnull PsiElement place) {
    for (PsiElement element : getDeclarationElementToProcess(lastParent)) {
      if (!processor.execute(element, state)) {
        return false;
      }
    }
    return super.processDeclarations(processor, state, lastParent, place);
  }

  private List<PsiElement> getDeclarationElementToProcess(PsiElement lastParent) {
    final boolean isBlock = this instanceof HaxeBlockStatement || this instanceof HaxeSwitchCaseBlock;
    final PsiElement stopper = isBlock ? lastParent : null;
    final List<PsiElement> result = new ArrayList<PsiElement>();
    addVarDeclarations(result, PsiTreeUtil.getChildrenOfType(this, HaxeVarDeclaration.class));
    addLocalVarDeclarations(result, UsefulPsiTreeUtil.getChildrenOfType(this, HaxeLocalVarDeclaration.class, stopper));

    addDeclarations(result, PsiTreeUtil.getChildrenOfType(this, HaxeFunctionDeclarationWithAttributes.class));
    addDeclarations(result, UsefulPsiTreeUtil.getChildrenOfType(this, HaxeLocalFunctionDeclaration.class, stopper));
    addDeclarations(result, PsiTreeUtil.getChildrenOfType(this, HaxeClassDeclaration.class));
    addDeclarations(result, PsiTreeUtil.getChildrenOfType(this, HaxeExternClassDeclaration.class));
    addDeclarations(result, PsiTreeUtil.getChildrenOfType(this, HaxeEnumDeclaration.class));
    addDeclarations(result, PsiTreeUtil.getChildrenOfType(this, HaxeInterfaceDeclaration.class));
    addDeclarations(result, PsiTreeUtil.getChildrenOfType(this, HaxeTypedefDeclaration.class));

    final HaxeParameterList parameterList = PsiTreeUtil.getChildOfType(this, HaxeParameterList.class);
    if (parameterList != null) {
      result.addAll(parameterList.getParameterList());
    }
    final HaxeGenericParam tygenericParameParam = PsiTreeUtil.getChildOfType(this, HaxeGenericParam.class);
    if (tygenericParameParam != null) {
      result.addAll(tygenericParameParam.getGenericListPartList());
    }

    if (this instanceof HaxeForStatement && ((HaxeForStatement)this).getIterable() != lastParent) {
      result.add(this);
    }
    return result;
  }

  private static void addLocalVarDeclarations(@Nonnull List<PsiElement> result,
                                              @Nullable HaxeLocalVarDeclaration[] items) {
    if (items == null) {
      return;
    }
    for (HaxeLocalVarDeclaration varDeclaration : items) {
      result.addAll(varDeclaration.getLocalVarDeclarationPartList());
    }
  }

  private static void addVarDeclarations(@Nonnull List<PsiElement> result, @Nullable HaxeVarDeclaration[] items) {
    if (items == null) {
      return;
    }
    for (HaxeVarDeclaration varDeclaration : items) {
      result.addAll(varDeclaration.getVarDeclarationPartList());
    }
  }

  private static void addDeclarations(@Nonnull List<PsiElement> result, @Nullable PsiElement[] items) {
    if (items != null) {
      result.addAll(Arrays.asList(items));
    }
  }
}
