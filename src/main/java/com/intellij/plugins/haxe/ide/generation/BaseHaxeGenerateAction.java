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
package com.intellij.plugins.haxe.ide.generation;

import consulo.language.editor.LangDataKeys;
import consulo.language.editor.PlatformDataKeys;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.ui.ex.action.AnActionEvent;
import consulo.codeEditor.Editor;
import consulo.project.Project;
import consulo.util.lang.Pair;
import com.intellij.plugins.haxe.lang.psi.HaxeClass;
import consulo.language.psi.PsiFile;
import consulo.ui.ex.action.AnAction;

/**
 * @author: Fedor.Korotkov
 */
public abstract class BaseHaxeGenerateAction extends AnAction {

  public void actionPerformed(final AnActionEvent e) {
    final Project project = e.getData(PlatformDataKeys.PROJECT);
    assert project != null;
    final Pair<Editor, PsiFile> editorAndPsiFile = getEditorAndPsiFile(e);
    getGenerateHandler().invoke(project, editorAndPsiFile.first, editorAndPsiFile.second);
  }

  private static Pair<Editor, PsiFile> getEditorAndPsiFile(final AnActionEvent e) {
    final Project project = e.getData(PlatformDataKeys.PROJECT);
    if (project == null) return Pair.create(null, null);
    Editor editor = e.getData(PlatformDataKeys.EDITOR);
    PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
    return Pair.create(editor, psiFile);
  }

  protected abstract BaseHaxeGenerateHandler getGenerateHandler();

  @Override
  public void update(final AnActionEvent e) {
    final Pair<Editor, PsiFile> editorAndPsiFile = getEditorAndPsiFile(e);
    final Editor editor = editorAndPsiFile.first;
    final PsiFile psiFile = editorAndPsiFile.second;

    final int caretOffset = editor == null ? -1 : editor.getCaretModel().getOffset();
    final boolean inClass = psiFile != null && PsiTreeUtil.getParentOfType(psiFile.findElementAt(caretOffset), HaxeClass.class) != null;

    e.getPresentation().setEnabled(inClass);
    e.getPresentation().setVisible(inClass);
  }
}
