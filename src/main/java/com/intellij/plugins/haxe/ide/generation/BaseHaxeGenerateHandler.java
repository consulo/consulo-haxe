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

import com.intellij.plugins.haxe.HaxeLanguage;
import com.intellij.plugins.haxe.ide.HaxeNamedElementNode;
import com.intellij.plugins.haxe.lang.psi.HaxeClass;
import com.intellij.plugins.haxe.lang.psi.HaxeClassDeclaration;
import com.intellij.plugins.haxe.lang.psi.HaxeFile;
import com.intellij.plugins.haxe.lang.psi.HaxeNamedComponent;
import consulo.application.ApplicationManager;
import consulo.codeEditor.Editor;
import consulo.language.Language;
import consulo.language.editor.FileModificationService;
import consulo.language.editor.action.LanguageCodeInsightActionHandler;
import consulo.language.editor.generation.ClassMember;
import consulo.language.editor.generation.MemberChooserBuilder;
import consulo.language.psi.PsiFile;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.language.util.IncorrectOperationException;
import consulo.localize.LocalizeValue;
import consulo.logging.Logger;
import consulo.project.Project;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.undoRedo.CommandProcessor;
import consulo.util.collection.ContainerUtil;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author: Fedor.Korotkov
 */
public abstract class BaseHaxeGenerateHandler implements LanguageCodeInsightActionHandler {
  @Override
  public boolean isValidFor(Editor editor, PsiFile file) {
    return file instanceof HaxeFile;
  }

  @Nonnull
  @Override
  public Language getLanguage() {
    return HaxeLanguage.INSTANCE;
  }

  @RequiredUIAccess
  @Override
  public void invoke(@Nonnull Project project, @Nonnull Editor editor, @Nonnull PsiFile file) {
    if (!FileModificationService.getInstance().prepareFileForWrite(file)) return;
    final HaxeClass haxeClass =
        PsiTreeUtil.getParentOfType(file.findElementAt(editor.getCaretModel().getOffset()), HaxeClassDeclaration.class);
    if (haxeClass == null) return;

    final List<HaxeNamedComponent> candidates = new ArrayList<HaxeNamedComponent>();
    collectCandidates(haxeClass, candidates);

    if (ApplicationManager.getApplication().isUnitTestMode()) {
      List<HaxeNamedElementNode> selectedElements = ContainerUtil.map(candidates, HaxeNamedElementNode::new);

      final BaseCreateMethodsFix createMethodsFix = createFix(haxeClass);
      doInvoke(project, editor, file, selectedElements, createMethodsFix);
    } else if (!candidates.isEmpty()) {
      final MemberChooserBuilder<HaxeNamedElementNode> chooser = createMemberChooserDialog(project, haxeClass, candidates, getTitle());

      chooser.showAsync(project, dataHolder -> {
        List elements = dataHolder.getUserData(ClassMember.KEY_OF_LIST);

        final BaseCreateMethodsFix createMethodsFix = createFix(haxeClass);
        doInvoke(project, editor, file, elements, createMethodsFix);
      });
    }
  }

  protected void doInvoke(final Project project,
                          final Editor editor,
                          final PsiFile file,
                          final Collection<HaxeNamedElementNode> selectedElements,
                          final BaseCreateMethodsFix createMethodsFix) {
    Runnable runnable = new Runnable() {
      public void run() {
        createMethodsFix.addElementsToProcessFrom(selectedElements);
        createMethodsFix.beforeInvoke(project, editor, file);

        ApplicationManager.getApplication().runWriteAction(new Runnable() {
          public void run() {
            try {
              createMethodsFix.invoke(project, editor, file);
            } catch (IncorrectOperationException ex) {
              Logger.getInstance(getClass().getName()).error(ex);
            }
          }
        });
      }
    };

    if (!CommandProcessor.getInstance().hasCurrentCommand()) {
      CommandProcessor.getInstance().executeCommand(project, runnable, getClass().getName(), null);
    } else {
      runnable.run();
    }
  }

  protected abstract BaseCreateMethodsFix createFix(HaxeClass haxeClass);

  protected abstract String getTitle();

  abstract void collectCandidates(HaxeClass aClass, List<HaxeNamedComponent> candidates);


  @Override
  public boolean startInWriteAction() {
    return true;
  }

  protected MemberChooserBuilder<HaxeNamedElementNode> createMemberChooserDialog(final Project project,
                                                                                 final HaxeClass haxeClass,
                                                                                 final Collection<HaxeNamedComponent> candidates,
                                                                                 String title) {
    HaxeNamedElementNode[] nodes = ContainerUtil.map(candidates, HaxeNamedElementNode::new).toArray(new HaxeNamedElementNode[candidates.size()]);

    MemberChooserBuilder<HaxeNamedElementNode> builder = MemberChooserBuilder.create(nodes);

    builder.withTitle(LocalizeValue.of(title));

    return builder;
  }
}
