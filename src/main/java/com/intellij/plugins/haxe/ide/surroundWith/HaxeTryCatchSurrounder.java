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
package com.intellij.plugins.haxe.ide.surroundWith;

import jakarta.annotation.Nonnull;

import consulo.document.util.TextRange;
import com.intellij.plugins.haxe.HaxeBundle;
import com.intellij.plugins.haxe.lang.psi.HaxeCatchStatement;
import com.intellij.plugins.haxe.lang.psi.HaxeParameter;
import com.intellij.plugins.haxe.lang.psi.HaxeTryStatement;
import com.intellij.plugins.haxe.util.HaxeElementGenerator;
import consulo.language.psi.PsiElement;

/**
 * @author: Fedor.Korotkov
 */
public class HaxeTryCatchSurrounder extends HaxeManyStatementsSurrounder {
  @Nonnull
  @Override
  protected PsiElement doSurroundElements(PsiElement[] elements, PsiElement parent) {
    final HaxeTryStatement tryStatement =
      (HaxeTryStatement)HaxeElementGenerator.createStatementFromText(elements[0].getProject(), "try {\n} catch(a) {\n}");
    addStatements(tryStatement.getBlockStatement(), elements);
    return tryStatement;
  }

  @Override
  protected TextRange getSurroundSelectionRange(PsiElement element) {
    final HaxeCatchStatement catchStatement = ((HaxeTryStatement)element).getCatchStatementList().iterator().next();
    final HaxeParameter parameter = catchStatement.getParameter();
    return parameter == null ? catchStatement.getTextRange() : parameter.getTextRange();
  }

  @Override
  public String getTemplateDescription() {
    return HaxeBundle.message("haxe.surrounder.try.catch");
  }
}
