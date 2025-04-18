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

import com.intellij.plugins.haxe.lang.psi.HaxeBlockStatement;
import consulo.codeEditor.Editor;
import consulo.document.util.TextRange;
import consulo.language.ast.ASTNode;
import consulo.language.editor.surroundWith.Surrounder;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiParserFacade;
import consulo.language.util.IncorrectOperationException;
import consulo.project.Project;

import jakarta.annotation.Nonnull;

/**
 * @author: Fedor.Korotkov
 */
public abstract class HaxeManyStatementsSurrounder implements Surrounder {
  @Override
  public boolean isApplicable(@Nonnull PsiElement[] elements) {
    if (elements.length == 0) return false;
    final PsiElement parent = elements[0].getParent();
    return parent instanceof HaxeBlockStatement;
  }

  @Override
  public TextRange surroundElements(@Nonnull Project project, @Nonnull Editor editor, @Nonnull PsiElement[] elements)
    throws IncorrectOperationException
  {
    if (elements.length == 0) return null;

    PsiElement element1 = elements[0];
    final PsiElement newStmt = doSurroundElements(elements, element1.getParent());

    ASTNode parentNode = element1.getParent().getNode();
    if (elements.length > 1) {
      parentNode.removeRange(element1.getNode().getTreeNext(), elements[elements.length - 1].getNode().getTreeNext());
    }
    parentNode.replaceChild(element1.getNode(), newStmt.getNode());

    return getSurroundSelectionRange(newStmt);
  }

  protected static void addStatements(HaxeBlockStatement block, PsiElement[] elements) throws IncorrectOperationException {
    block.addRangeAfter(elements[0], elements[elements.length - 1], block.getFirstChild());
    final PsiElement newLineNode =
      PsiParserFacade.SERVICE.getInstance(block.getProject()).createWhiteSpaceFromText("\n");
    block.addAfter(newLineNode, block.getFirstChild());
  }

  @Nonnull
  protected abstract PsiElement doSurroundElements(PsiElement[] elements, PsiElement parent);

  protected abstract TextRange getSurroundSelectionRange(PsiElement element);
}
