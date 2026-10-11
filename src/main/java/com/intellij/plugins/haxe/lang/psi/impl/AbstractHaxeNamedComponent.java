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
import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.access.RequiredWriteAction;
import consulo.language.ast.ASTNode;
import consulo.language.icon.IconDescriptorUpdaters;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiNamedElement;
import consulo.language.psi.PsiReference;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.language.util.IncorrectOperationException;
import consulo.localize.LocalizeValue;
import consulo.navigation.ItemPresentation;
import consulo.ui.image.Image;
import consulo.util.collection.ContainerUtil;
import consulo.util.lang.Pair;
import jakarta.annotation.Nonnull;

/**
 * @author Fedor.Korotkov
 */
abstract public class AbstractHaxeNamedComponent extends HaxePsiCompositeElementImpl implements HaxeNamedComponent, PsiNamedElement {
    public AbstractHaxeNamedComponent(@Nonnull ASTNode node) {
        super(node);
    }

    @Override
    @RequiredReadAction
    public String getName() {
        HaxeComponentName name = getComponentName();
        if (name != null) {
            return name.getText();
        }
        return super.getName();
    }

    @Override
    @RequiredReadAction
    public String getText() {
        return super.getText();
    }

    @Override
    @RequiredWriteAction
    public PsiElement setName(@Nonnull String name) throws IncorrectOperationException {
        HaxeComponentName componentName = getComponentName();
        if (componentName != null) {
            componentName.setName(name);
        }
        return this;
    }

    @Override
    public ItemPresentation getPresentation() {
        return new ItemPresentation() {
            @Override
            @RequiredReadAction
            public LocalizeValue getPresentableText() {
                StringBuilder result = new StringBuilder();
                HaxeComponentName componentName = getComponentName();
                if (componentName != null) {
                    result.append(componentName.getText());
                }
                else if (HaxeComponentType.typeOf(AbstractHaxeNamedComponent.this) == HaxeComponentType.METHOD) {
                    // constructor
                    result.append("new");
                }
                HaxeComponentType type = HaxeComponentType.typeOf(AbstractHaxeNamedComponent.this);
                if (type == HaxeComponentType.METHOD) {
                    String parameterList = HaxePresentableUtil.getPresentableParameterList(AbstractHaxeNamedComponent.this);
                    result.append("(").append(parameterList).append(")");
                }
                if (type == HaxeComponentType.METHOD || type == HaxeComponentType.FIELD) {
                    HaxeTypeTag typeTag = PsiTreeUtil.getChildOfType(AbstractHaxeNamedComponent.this, HaxeTypeTag.class);
                    HaxeTypeOrAnonymous typeOrAnonymous =
                        typeTag != null ? ContainerUtil.getFirstItem(typeTag.getTypeOrAnonymousList()) : null;
                    if (typeOrAnonymous != null) {
                        result.append(":");
                        result.append(HaxePresentableUtil.buildTypeText(AbstractHaxeNamedComponent.this, typeOrAnonymous.getType()));
                    }
                }
                return LocalizeValue.of(result.toString());
            }

            @Override
            public String getLocationString() {
                HaxeClass haxeClass = AbstractHaxeNamedComponent.this instanceof HaxeClass
                    ? (HaxeClass) AbstractHaxeNamedComponent.this
                    : PsiTreeUtil.getParentOfType(AbstractHaxeNamedComponent.this, HaxeClass.class);
                if (haxeClass instanceof HaxeAnonymousType) {
                    HaxeTypedefDeclaration typedefDeclaration = PsiTreeUtil.getParentOfType(haxeClass, HaxeTypedefDeclaration.class);
                    if (typedefDeclaration != null) {
                        haxeClass = typedefDeclaration;
                    }
                }
                if (haxeClass == null) {
                    return "";
                }
                Pair<String, String> qName = HaxeResolveUtil.splitQName(haxeClass.getQualifiedName());
                if (haxeClass == AbstractHaxeNamedComponent.this) {
                    return qName.getFirst();
                }
                return haxeClass.getQualifiedName();
            }

            @Override
            @RequiredReadAction
            public Image getIcon() {
                return IconDescriptorUpdaters.getIcon(AbstractHaxeNamedComponent.this, 0);
            }
        };
    }

    @Override
    @RequiredReadAction
    public HaxeNamedComponent getTypeComponent() {
        HaxeTypeTag typeTag = PsiTreeUtil.getChildOfType(getParent(), HaxeTypeTag.class);
        HaxeType type = typeTag == null ? null : ContainerUtil.getFirstItem(typeTag.getTypeOrAnonymousList()).getType();
        PsiReference reference = type == null ? null : type.getReference();
        return reference != null && reference.resolve() instanceof HaxeNamedComponent typeComp ? typeComp : null;
    }

    @Override
    @RequiredReadAction
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
        PsiElement parent = getParent();
        return hasPublicAccessor(this) || (parent instanceof HaxePsiCompositeElement compElem && hasPublicAccessor(compElem));
    }

    @RequiredReadAction
    private static boolean hasPublicAccessor(HaxePsiCompositeElement element) {
        if (UsefulPsiTreeUtil.getChildOfType(element, HaxeTokenTypes.KPUBLIC) != null) {
            return true;
        }
        HaxeDeclarationAttribute[] declarationAttributeList = PsiTreeUtil.getChildrenOfType(element, HaxeDeclarationAttribute.class);
        return HaxeResolveUtil.getDeclarationTypes(declarationAttributeList).contains(HaxeTokenTypes.KPUBLIC);
    }

    @Override
    @RequiredReadAction
    public boolean isStatic() {
        HaxeDeclarationAttribute[] declarationAttributeList = PsiTreeUtil.getChildrenOfType(this, HaxeDeclarationAttribute.class);
        return HaxeResolveUtil.getDeclarationTypes(declarationAttributeList).contains(HaxeTokenTypes.KSTATIC);
    }

    @Override
    @RequiredReadAction
    public boolean isOverride() {
        HaxeDeclarationAttribute[] declarationAttributeList = PsiTreeUtil.getChildrenOfType(this, HaxeDeclarationAttribute.class);
        return HaxeResolveUtil.getDeclarationTypes(declarationAttributeList).contains(HaxeTokenTypes.KOVERRIDE);
    }
}
