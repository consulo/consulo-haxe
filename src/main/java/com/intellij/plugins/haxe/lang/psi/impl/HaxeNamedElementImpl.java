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

import com.intellij.plugins.haxe.HaxeComponentType;
import com.intellij.plugins.haxe.lang.psi.*;
import com.intellij.plugins.haxe.util.HaxeElementGenerator;
import consulo.content.scope.SearchScope;
import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;
import consulo.language.psi.scope.LocalSearchScope;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.language.util.IncorrectOperationException;
import consulo.navigation.ItemPresentation;
import consulo.navigation.NavigationItem;
import consulo.util.io.FileUtil;
import org.jetbrains.annotations.NonNls;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * @author: Fedor.Korotkov
 */
public abstract class HaxeNamedElementImpl extends HaxePsiCompositeElementImpl implements HaxeComponentName {
  public HaxeNamedElementImpl(@Nonnull ASTNode node) {
    super(node);
  }

  @Override
  public PsiElement setName(@NonNls @Nonnull String newElementName) throws IncorrectOperationException
  {
    final HaxeIdentifier identifier = getIdentifier();
    final HaxeIdentifier identifierNew = HaxeElementGenerator.createIdentifierFromText(getProject(), newElementName);

    final String oldName = getName();
    if (identifierNew != null) {
      getNode().replaceChild(identifier.getNode(), identifierNew.getNode());
    }

    if (getParent() instanceof HaxeClass) {
      final HaxeFile haxeFile = (HaxeFile)getParent().getParent();
      if (oldName != null && oldName.equals(FileUtil.getNameWithoutExtension(haxeFile.getName()))) {
        haxeFile.setName(newElementName + "." + FileUtil.getExtension(haxeFile.getName()));
      }
    }
    return this;
  }

  @Override
  public String getName() {
    return getIdentifier().getText();
  }

  @Override
  public PsiElement getNameIdentifier() {
    return this;
  }

  @Nonnull
  @Override
  public SearchScope getUseScope() {
    final HaxeComponentType type = HaxeComponentType.typeOf(getParent());
    final HaxeComponent component = PsiTreeUtil.getParentOfType(getParent(), HaxeComponent.class, true);
    if (type == null || component == null) {
      return super.getUseScope();
    }
    if (type == HaxeComponentType.FUNCTION || type == HaxeComponentType.PARAMETER || type == HaxeComponentType.VARIABLE) {
      return new LocalSearchScope(component);
    }
    return super.getUseScope();
  }

  @Nullable
  public ItemPresentation getPresentation() {
    if (getParent() instanceof NavigationItem) {
      return ((NavigationItem)getParent()).getPresentation();
    }
    return null;
  }
}
