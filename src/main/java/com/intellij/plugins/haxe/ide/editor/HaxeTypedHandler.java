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
package com.intellij.plugins.haxe.ide.editor;

import com.intellij.plugins.haxe.lang.psi.HaxeComponentName;
import com.intellij.plugins.haxe.lang.psi.HaxePsiCompositeElement;
import com.intellij.plugins.haxe.lang.psi.HaxeType;
import com.intellij.plugins.haxe.util.UsefulPsiTreeUtil;
import consulo.annotation.component.ExtensionImpl;
import consulo.codeEditor.Editor;
import consulo.language.editor.action.TypedHandlerDelegate;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.project.Project;
import consulo.virtualFileSystem.fileType.FileType;

import jakarta.annotation.Nonnull;

@ExtensionImpl(id = "haxe")
public class HaxeTypedHandler extends TypedHandlerDelegate {
  private boolean myAfterTypeOrComponentName = false;
  private boolean myAfterDollar = false;

  @Override
  public Result beforeCharTyped(char c,
                                Project project,
                                Editor editor,
                                PsiFile file,
                                FileType fileType) {
    if (c == '<') {
      myAfterTypeOrComponentName = checkAfterTypeOrComponentName(file, editor.getCaretModel().getOffset());
    }
    if (c == '{') {
      myAfterDollar = checkAfterDollarInString(file, editor.getCaretModel().getOffset());
    }
    return super.beforeCharTyped(c, project, editor, file, fileType);
  }

  private static boolean checkAfterTypeOrComponentName(PsiFile file, int offset) {
    PsiElement at = file.findElementAt(offset - 1);
    PsiElement toCheck = UsefulPsiTreeUtil.getPrevSiblingSkipWhiteSpacesAndComments(at, false);
    return PsiTreeUtil.getParentOfType(toCheck, HaxeType.class, HaxeComponentName.class) != null;
  }

  private static boolean checkAfterDollarInString(PsiFile file, int offset) {
    PsiElement at = file.findElementAt(offset - 1);
    final String text = at != null ? at.getText() : "";
    return text.endsWith("$") && PsiTreeUtil.getParentOfType(at, HaxePsiCompositeElement.class) != null;
  }

  @Override
  public Result charTyped(char c, Project project, Editor editor, @Nonnull PsiFile file) {
    String textToInsert = null;
    if (c == '<' && myAfterTypeOrComponentName) {
      myAfterTypeOrComponentName = false;
      textToInsert = ">";
    } else if (c == '{' && myAfterDollar) {
      myAfterDollar = false;
      textToInsert = "}";
    }
    if (textToInsert != null) {
      int offset = editor.getCaretModel().getOffset();
      if (offset >= 0) {
        editor.getDocument().insertString(offset, textToInsert);
        editor.getCaretModel().moveToOffset(offset);
        return Result.STOP;
      }
    }
    return super.charTyped(c, project, editor, file);
  }
}
