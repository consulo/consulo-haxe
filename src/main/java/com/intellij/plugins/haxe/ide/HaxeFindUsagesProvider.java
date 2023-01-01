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
package com.intellij.plugins.haxe.ide;

import com.intellij.plugins.haxe.HaxeComponentType;
import com.intellij.plugins.haxe.HaxeLanguage;
import com.intellij.plugins.haxe.lang.psi.HaxeNamedComponent;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.findUsage.FindUsagesProvider;
import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;

import javax.annotation.Nonnull;

/**
 * @author: Fedor.Korotkov
 */
@ExtensionImpl
public class HaxeFindUsagesProvider implements FindUsagesProvider {
  @Override
  public boolean canFindUsagesFor(@Nonnull PsiElement psiElement) {
    return PsiTreeUtil.getParentOfType(psiElement, HaxeNamedComponent.class, false) != null;
  }

  @Nonnull
  public String getType(@Nonnull final PsiElement element) {
    final String result = HaxeComponentType.getName(element.getParent());
    return result == null ? "reference" : result;
  }

  @Nonnull
  public String getDescriptiveName(@Nonnull final PsiElement element) {
    final String result = HaxeComponentType.getPresentableName(element.getParent());
    return result == null ? "" : result;
  }

  @Nonnull
  public String getNodeText(@Nonnull final PsiElement element, final boolean useFullName) {
    return element.getText();
  }

  @Nonnull
  @Override
  public Language getLanguage() {
    return HaxeLanguage.INSTANCE;
  }
}
