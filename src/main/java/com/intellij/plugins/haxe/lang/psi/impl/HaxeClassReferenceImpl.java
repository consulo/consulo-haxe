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

import com.intellij.plugins.haxe.lang.psi.*;
import com.intellij.plugins.haxe.util.HaxeResolveUtil;
import consulo.document.util.TextRange;
import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiReference;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.language.util.IncorrectOperationException;
import consulo.util.collection.ArrayUtil;

import jakarta.annotation.Nonnull;

/**
 * @author: Fedor.Korotkov
 */
public class HaxeClassReferenceImpl extends HaxeExpressionImpl implements HaxeReference {
  public HaxeClassReferenceImpl(ASTNode node) {
    super(node);
  }

  @Override
  public PsiElement getElement() {
    return this;
  }

  @Override
  public PsiReference getReference() {
    return this;
  }

  @Override
  public TextRange getRangeInElement() {
    final TextRange textRange = getTextRange();
    return new TextRange(0, textRange.getEndOffset() - textRange.getStartOffset());
  }

  @Override
  public PsiElement resolve() {
    return null;
  }

  @Nonnull
  @Override
  public String getCanonicalText() {
    return getText();
  }

  @Override
  public boolean isSoft() {
    return false;
  }

  @Override
  public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException
  {
    return this;
  }

  @Override
  public PsiElement bindToElement(@Nonnull PsiElement element) throws IncorrectOperationException
  {
    return this;
  }

  @Override
  public boolean isReferenceTo(PsiElement element) {
    return false;
  }

  @Nonnull
  @Override
  public Object[] getVariants() {
    return ArrayUtil.EMPTY_OBJECT_ARRAY;
  }

  @Nonnull
  @Override
  public HaxeClassResolveResult resolveHaxeClass() {
    final HaxeFunctionType functionType = PsiTreeUtil.getChildOfType(this, HaxeFunctionType.class);
    HaxeTypeOrAnonymous typeOrAnonymous = PsiTreeUtil.getChildOfType(this, HaxeTypeOrAnonymous.class);
    if (functionType != null && !functionType.getTypeOrAnonymousList().isEmpty()) {
      typeOrAnonymous = functionType.getTypeOrAnonymousList().iterator().next();
    }
    final HaxeType type = typeOrAnonymous != null ? typeOrAnonymous.getType() : PsiTreeUtil.getChildOfType(this, HaxeType.class);
    return HaxeClassResolveResult.create(HaxeResolveUtil.tryResolveClassByQName(type));
  }
}
