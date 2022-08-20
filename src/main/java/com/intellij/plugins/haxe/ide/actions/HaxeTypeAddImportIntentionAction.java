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
package com.intellij.plugins.haxe.ide.actions;

import com.intellij.plugins.haxe.HaxeBundle;
import com.intellij.plugins.haxe.lang.psi.HaxeClass;
import com.intellij.plugins.haxe.lang.psi.HaxeComponent;
import com.intellij.plugins.haxe.util.HaxeAddImportHelper;
import consulo.application.Result;
import consulo.codeEditor.Editor;
import consulo.codeEditor.EditorPopupHelper;
import consulo.document.util.TextRange;
import consulo.language.editor.WriteCommandAction;
import consulo.language.editor.hint.HintManager;
import consulo.language.editor.hint.QuestionAction;
import consulo.language.editor.inspection.LocalQuickFix;
import consulo.language.editor.inspection.ProblemDescriptor;
import consulo.language.editor.intention.HintAction;
import consulo.language.editor.ui.DefaultPsiElementCellRenderer;
import consulo.language.editor.ui.PopupNavigationUtil;
import consulo.language.inject.InjectedLanguageManager;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.resolve.PsiElementProcessor;
import consulo.language.util.IncorrectOperationException;
import consulo.project.Project;
import consulo.ui.ex.popup.JBPopup;
import consulo.undoRedo.CommandProcessor;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author: Fedor.Korotkov
 */
public class HaxeTypeAddImportIntentionAction implements HintAction, QuestionAction, LocalQuickFix {
  private final List<HaxeComponent> candidates;
  private final PsiElement myType;
  private Editor myEditor;

  public HaxeTypeAddImportIntentionAction(@Nonnull PsiElement type, @Nonnull List<HaxeComponent> components) {
    myType = type;
    candidates = components;
  }

  @Override
  public boolean showHint(@Nonnull Editor editor) {
    myEditor = editor;
    TextRange range = InjectedLanguageManager.getInstance(myType.getProject()).injectedToHost(myType, myType.getTextRange());
    HintManager.getInstance().showQuestionHint(editor, getText(), range.getStartOffset(), range.getEndOffset(), this);
    return true;
  }

  @Nonnull
  @Override
  public String getText() {
    if (candidates.size() > 1) {
      final HaxeClass haxeClass = (HaxeClass) candidates.iterator().next();
      return HaxeBundle.message("add.import.multiple.candidates", haxeClass.getQualifiedName());
    } else if (candidates.size() == 1) {
      final HaxeClass haxeClass = (HaxeClass) candidates.iterator().next();
      return haxeClass.getQualifiedName() + " ?";
    }
    return "";
  }

  @Nonnull
  @Override
  public String getName() {
    return getText();
  }

  @Nonnull
  @Override
  public String getFamilyName() {
    return getText();
  }

  @Override
  public void applyFix(@Nonnull Project project, @Nonnull ProblemDescriptor descriptor) {
    invoke(project, myEditor, descriptor.getPsiElement().getContainingFile());
  }

  @Override
  public boolean isAvailable(@Nonnull Project project, Editor editor, PsiFile file) {
    return myType.isValid();
  }

  @Override
  public void invoke(@Nonnull final Project project, final Editor editor, PsiFile file) throws IncorrectOperationException {
    if (candidates.size() > 1) {
      JBPopup popup = PopupNavigationUtil.getPsiElementPopup(
          candidates.toArray(new PsiElement[candidates.size()]),
          new DefaultPsiElementCellRenderer(),
          HaxeBundle.message("choose.class.to.import.title"),
          new PsiElementProcessor<PsiElement>() {
            public boolean execute(@Nonnull final PsiElement element) {
              CommandProcessor.getInstance().executeCommand(
                  project,
                  () -> doImport(editor, element),
                  getClass().getName(),
                  this
              );
              return false;
            }
          }
      );

      EditorPopupHelper.getInstance().showPopupInBestPositionFor(editor, popup);
    } else {
      doImport(editor, candidates.iterator().next());
    }
  }

  private void doImport(final Editor editor, final PsiElement component) {
    new WriteCommandAction(myType.getProject(), myType.getContainingFile()) {
      @Override
      protected void run(Result result) throws Throwable {
        HaxeAddImportHelper.addImport(((HaxeClass) component).getQualifiedName(), myType.getContainingFile());
      }
    }.execute();
  }

  @Override
  public boolean startInWriteAction() {
    return true;
  }

  @Override
  public boolean execute() {
    final PsiFile containingFile = myType.getContainingFile();
    invoke(containingFile.getProject(), myEditor, containingFile);
    return true;
  }
}
