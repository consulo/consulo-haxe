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

import com.intellij.plugins.haxe.HaxeLanguage;
import com.intellij.plugins.haxe.lang.psi.*;
import com.intellij.plugins.haxe.util.HaxeResolveUtil;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.editor.completion.*;
import consulo.language.editor.completion.lookup.LookupElementBuilder;
import consulo.language.pattern.PsiElementPattern;
import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.language.util.ProcessingContext;

import jakarta.annotation.Nonnull;

import static consulo.language.pattern.PlatformPatterns.psiElement;

/**
 * @author: Fedor.Korotkov
 */
@ExtensionImpl
public class HaxeSmartCompletionContributor extends CompletionContributor {
  public HaxeSmartCompletionContributor() {
    final PsiElementPattern.Capture<PsiElement> idInExpression =
        psiElement().withSuperParent(1, HaxeIdentifier.class).withSuperParent(2, HaxeReference.class);
    extend(CompletionType.SMART,
        idInExpression.and(psiElement().withSuperParent(3, HaxeVarInit.class)),
        new CompletionProvider() {
          @Override
          public void addCompletions(@Nonnull CompletionParameters parameters,
                                     ProcessingContext context,
                                     @Nonnull CompletionResultSet result) {
            tryAddVariantsForEnums(result, parameters.getPosition());
          }
        });
  }

  private static void tryAddVariantsForEnums(CompletionResultSet result, @Nonnull PsiElement element) {
    final HaxeVarInit varInit = PsiTreeUtil.getParentOfType(element, HaxeVarInit.class);
    assert varInit != null;
    final HaxeClassResolveResult resolveResult =
        HaxeResolveUtil.tryResolveClassByTypeTag(varInit.getParent(), HaxeGenericSpecialization.EMPTY);
    final HaxeClass haxeClass = resolveResult.getHaxeClass();
    if (haxeClass instanceof HaxeEnumDeclaration) {
      final String className = haxeClass.getName();
      for (HaxeNamedComponent component : HaxeResolveUtil.getNamedSubComponents(haxeClass)) {
        result.addElement(LookupElementBuilder.create(className + "." + component.getName()));
      }
    }
  }

  @Nonnull
  @Override
  public Language getLanguage() {
    return HaxeLanguage.INSTANCE;
  }
}
