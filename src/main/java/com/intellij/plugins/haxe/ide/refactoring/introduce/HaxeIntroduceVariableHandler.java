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
package com.intellij.plugins.haxe.ide.refactoring.introduce;

import com.intellij.plugins.haxe.HaxeBundle;
import consulo.language.psi.PsiElement;
import jakarta.annotation.Nonnull;

import java.util.List;

/**
 * @author: Fedor.Korotkov
 */
public class HaxeIntroduceVariableHandler extends HaxeIntroduceHandler {
  public HaxeIntroduceVariableHandler() {
    super(HaxeBundle.message("refactoring.introduce.variable.dialog.title"));
  }

  @Override
  protected PsiElement addDeclaration(@Nonnull final PsiElement expression,
                                      @Nonnull final PsiElement declaration,
                                      @Nonnull HaxeIntroduceOperation operation) {
    return doIntroduceVariable(expression, declaration, operation.getOccurrences(), operation.isReplaceAll());
  }

  public static PsiElement doIntroduceVariable(PsiElement expression,
																	PsiElement declaration,
																	List<PsiElement> occurrences,
																	boolean replaceAll) {
    PsiElement anchor = replaceAll ? findAnchor(occurrences) : findAnchor(expression);
    assert anchor != null;
    final PsiElement parent = anchor.getParent();
    return parent.addBefore(declaration, anchor);
  }
}
