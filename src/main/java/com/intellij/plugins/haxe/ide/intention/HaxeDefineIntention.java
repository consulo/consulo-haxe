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
package com.intellij.plugins.haxe.ide.intention;

import com.intellij.plugins.haxe.HaxeBundle;
import com.intellij.plugins.haxe.config.HaxeProjectSettings;
import com.intellij.plugins.haxe.util.HaxeUtil;
import consulo.codeEditor.Editor;
import consulo.language.editor.intention.SyntheticIntentionAction;
import consulo.language.psi.PsiFile;
import consulo.language.util.IncorrectOperationException;
import consulo.project.Project;
import consulo.util.collection.ArrayUtil;
import org.jetbrains.annotations.Nls;

import jakarta.annotation.Nonnull;
import java.util.Set;

/**
 * @author: Fedor.Korotkov
 */
public class HaxeDefineIntention implements SyntheticIntentionAction {
  private final String myWord;
  private final boolean isDefined;

  public HaxeDefineIntention(@Nls String word, boolean contains) {
    myWord = word;
    isDefined = contains;
  }

  @Nonnull
  @Override
  public String getText() {
    return HaxeBundle.message(isDefined ? "haxe.intention.undefine" : "haxe.intention.define", myWord);
  }

  @Override
  public boolean isAvailable(@Nonnull Project project, Editor editor, PsiFile file) {
    return true;
  }

  @Override
  public void invoke(@Nonnull Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
    final HaxeProjectSettings projectSettings = HaxeProjectSettings.getInstance(file.getProject());
    final Set<String> definitions = projectSettings.getUserCompilerDefinitionsAsSet();
    projectSettings.setCompilerDefinitions(changeDefinitions(definitions));
    HaxeUtil.reparseProjectFiles(project);
  }

  private String[] changeDefinitions(Set<String> definitions) {
    if (isDefined) {
      definitions.remove(myWord);
    }
    else {
      definitions.add(myWord);
    }
    return ArrayUtil.toStringArray(definitions);
  }

  @Override
  public boolean startInWriteAction() {
    return false;
  }
}
