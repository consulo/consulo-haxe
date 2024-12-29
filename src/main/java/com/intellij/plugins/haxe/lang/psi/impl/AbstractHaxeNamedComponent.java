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
import com.intellij.plugins.haxe.lang.lexer.HaxeTokenTypes;
import com.intellij.plugins.haxe.lang.psi.*;
import com.intellij.plugins.haxe.util.HaxePresentableUtil;
import com.intellij.plugins.haxe.util.HaxeResolveUtil;
import com.intellij.plugins.haxe.util.UsefulPsiTreeUtil;
import consulo.language.ast.ASTNode;
import consulo.language.icon.IconDescriptorUpdaters;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiNamedElement;
import consulo.language.psi.PsiReference;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.language.util.IncorrectOperationException;
import consulo.navigation.ItemPresentation;
import consulo.ui.image.Image;
import consulo.util.collection.ContainerUtil;
import consulo.util.lang.Pair;
import org.jetbrains.annotations.NonNls;

import jakarta.annotation.Nonnull;

/**
 * @author: Fedor.Korotkov
 */
abstract public class AbstractHaxeNamedComponent extends HaxePsiCompositeElementImpl implements HaxeNamedComponent, PsiNamedElement
{
  public AbstractHaxeNamedComponent(@Nonnull ASTNode node) {
    super(node);
  }

  @Override
  public String getName() {
    final HaxeComponentName name = getComponentName();
    if (name != null) {
      return name.getText();
    }
    return super.getName();
  }

  @Override
  public String getText() {
    return super.getText();
  }

  @Override
  public PsiElement setName(@NonNls @Nonnull String name) throws IncorrectOperationException
  {
    final HaxeComponentName componentName = getComponentName();
    if (componentName != null) {
      componentName.setName(name);
    }
    return this;
  }

  @Override
  public ItemPresentation getPresentation() {
    return new ItemPresentation() {
      @Override
      public String getPresentableText() {
        final StringBuilder result = new StringBuilder();
        final HaxeComponentName componentName = getComponentName();
        if (componentName != null) {
          result.append(componentName.getText());
        }
        else if (HaxeComponentType.typeOf(AbstractHaxeNamedComponent.this) == HaxeComponentType.METHOD) {
          // constructor
          result.append("new");
        }
        final HaxeComponentType type = HaxeComponentType.typeOf(AbstractHaxeNamedComponent.this);
        if (type == HaxeComponentType.METHOD) {
          final String parameterList = HaxePresentableUtil.getPresentableParameterList(AbstractHaxeNamedComponent.this);
          result.append("(").append(parameterList).append(")");
        }
        if (type == HaxeComponentType.METHOD || type == HaxeComponentType.FIELD) {
          final HaxeTypeTag typeTag = PsiTreeUtil.getChildOfType(AbstractHaxeNamedComponent.this, HaxeTypeTag.class);
          final HaxeTypeOrAnonymous typeOrAnonymous = typeTag != null ? ContainerUtil.getFirstItem(typeTag.getTypeOrAnonymousList()) : null;
          if (typeOrAnonymous != null) {
            result.append(":");
            result.append(HaxePresentableUtil.buildTypeText(AbstractHaxeNamedComponent.this, typeOrAnonymous.getType()));
          }
        }
        return result.toString();
      }

      @Override
      public String getLocationString() {
        HaxeClass haxeClass = AbstractHaxeNamedComponent.this instanceof HaxeClass
                              ? (HaxeClass)AbstractHaxeNamedComponent.this
                              : PsiTreeUtil.getParentOfType(AbstractHaxeNamedComponent.this, HaxeClass.class);
        if (haxeClass instanceof HaxeAnonymousType) {
          final HaxeTypedefDeclaration typedefDeclaration = PsiTreeUtil.getParentOfType(haxeClass, HaxeTypedefDeclaration.class);
          if (typedefDeclaration != null) {
            haxeClass = typedefDeclaration;
          }
        }
        if (haxeClass == null) {
          return "";
        }
        final Pair<String, String> qName = HaxeResolveUtil.splitQName(haxeClass.getQualifiedName());
        if (haxeClass == AbstractHaxeNamedComponent.this) {
          return qName.getFirst();
        }
        return haxeClass.getQualifiedName();
      }

      @Override
      public Image getIcon() {
        return IconDescriptorUpdaters.getIcon(AbstractHaxeNamedComponent.this, 0);
      }
    };
  }

  @Override
  public HaxeNamedComponent getTypeComponent() {
    final HaxeTypeTag typeTag = PsiTreeUtil.getChildOfType(getParent(), HaxeTypeTag.class);
    final HaxeType type = typeTag == null ? null : ContainerUtil.getFirstItem(typeTag.getTypeOrAnonymousList()).getType();
    final PsiReference reference = type == null ? null : type.getReference();
    if (reference != null) {
      final PsiElement result = reference.resolve();
      if (result instanceof HaxeNamedComponent) {
        return (HaxeNamedComponent)result;
      }
    }
    return null;
  }

  @Override
  public boolean isPublic() {
    if (PsiTreeUtil.getParentOfType(this, HaxeExternClassDeclaration.class) != null) {
      return true;
    }
    if (PsiTreeUtil.getParentOfType(this, HaxeInterfaceDeclaration.class, HaxeEnumDeclaration.class) != null) {
      return true;
    }
    if (PsiTreeUtil.getParentOfType(this, HaxeAnonymousType.class) != null) {
      return true;
    }
    final PsiElement parent = getParent();
    return hasPublicAccessor(this) || (parent instanceof HaxePsiCompositeElement && hasPublicAccessor((HaxePsiCompositeElement)parent));
  }

  private static boolean hasPublicAccessor(HaxePsiCompositeElement element) {
    if (UsefulPsiTreeUtil.getChildOfType(element, HaxeTokenTypes.KPUBLIC) != null) {
      return true;
    }
    final HaxeDeclarationAttribute[] declarationAttributeList = PsiTreeUtil.getChildrenOfType(element, HaxeDeclarationAttribute.class);
    return HaxeResolveUtil.getDeclarationTypes(declarationAttributeList).contains(HaxeTokenTypes.KPUBLIC);
  }

  @Override
  public boolean isStatic() {
    final HaxeDeclarationAttribute[] declarationAttributeList = PsiTreeUtil.getChildrenOfType(this, HaxeDeclarationAttribute.class);
    return HaxeResolveUtil.getDeclarationTypes(declarationAttributeList).contains(HaxeTokenTypes.KSTATIC);
  }

  @Override
  public boolean isOverride() {
    final HaxeDeclarationAttribute[] declarationAttributeList = PsiTreeUtil.getChildrenOfType(this, HaxeDeclarationAttribute.class);
    return HaxeResolveUtil.getDeclarationTypes(declarationAttributeList).contains(HaxeTokenTypes.KOVERRIDE);
  }
}
