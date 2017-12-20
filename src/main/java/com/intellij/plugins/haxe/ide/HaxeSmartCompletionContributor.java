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

import static com.intellij.patterns.PlatformPatterns.psiElement;

import org.jetbrains.annotations.NotNull;
import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.plugins.haxe.lang.psi.HaxeClass;
import com.intellij.plugins.haxe.lang.psi.HaxeClassResolveResult;
import com.intellij.plugins.haxe.lang.psi.HaxeEnumDeclaration;
import com.intellij.plugins.haxe.lang.psi.HaxeGenericSpecialization;
import com.intellij.plugins.haxe.lang.psi.HaxeIdentifier;
import com.intellij.plugins.haxe.lang.psi.HaxeNamedComponent;
import com.intellij.plugins.haxe.lang.psi.HaxeReference;
import com.intellij.plugins.haxe.lang.psi.HaxeVarInit;
import com.intellij.plugins.haxe.util.HaxeResolveUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import consulo.codeInsight.completion.CompletionProvider;

/**
 * @author: Fedor.Korotkov
 */
public class HaxeSmartCompletionContributor extends CompletionContributor {
  public HaxeSmartCompletionContributor() {
    final PsiElementPattern.Capture<PsiElement> idInExpression =
      psiElement().withSuperParent(1, HaxeIdentifier.class).withSuperParent(2, HaxeReference.class);
    extend(CompletionType.SMART,
           idInExpression.and(psiElement().withSuperParent(3, HaxeVarInit.class)),
           new CompletionProvider() {
             @Override
			 public void addCompletions(@NotNull CompletionParameters parameters,
                                           ProcessingContext context,
                                           @NotNull CompletionResultSet result) {
               tryAddVariantsForEnums(result, parameters.getPosition());
             }
           });
  }

  private static void tryAddVariantsForEnums(CompletionResultSet result, @NotNull PsiElement element) {
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
}
