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

import consulo.ide.impl.idea.codeInsight.generation.surroundWith.SurroundWithHandler;
import consulo.application.ApplicationManager;
import com.intellij.plugins.haxe.HaxeCodeInsightFixtureTestCase;
import com.intellij.plugins.haxe.HaxeFileType;
import com.intellij.plugins.haxe.ide.surroundWith.*;
import consulo.language.codeStyle.CodeStyleManager;
import consulo.language.codeStyle.CodeStyleSettingsManager;
import consulo.language.psi.PsiDocumentManager;
import consulo.language.codeStyle.CodeStyleSettings;
import consulo.language.editor.surroundWith.Surrounder;
import consulo.project.Project;

/**
 * @author: Fedor.Korotkov
 */
public abstract class HaxeSurroundTest extends HaxeCodeInsightFixtureTestCase {
  @Override
  protected String getBasePath() {
    return "/surroundWith/";
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    setTestStyleSettings();
  }

  private void setTestStyleSettings() {
    Project project = getProject();
    CodeStyleSettings currSettings = CodeStyleSettingsManager.getSettings(project);
    assertNotNull(currSettings);
    CodeStyleSettings tempSettings = currSettings.clone();
    CodeStyleSettings.IndentOptions indentOptions = tempSettings.getIndentOptions(HaxeFileType.HAXE_FILE_TYPE);
    indentOptions.INDENT_SIZE = 2;
    assertNotNull(indentOptions);
    CodeStyleSettingsManager.getInstance(project).setTemporarySettings(tempSettings);
  }

  protected void doTest(final Surrounder surrounder) throws Exception {
    myFixture.configureByFile(getTestName(false) + ".hx");

    ApplicationManager.getApplication().runWriteAction(new Runnable() {
      public void run() {
        consulo.ide.impl.idea.codeInsight.generation.surroundWith.SurroundWithHandler.invoke(getProject(), myFixture.getEditor(), myFixture.getFile(), surrounder);
        PsiDocumentManager.getInstance(getProject()).doPostponedOperationsAndUnblockDocument(myFixture.getDocument(myFixture.getFile()));
        CodeStyleManager.getInstance(myFixture.getProject()).reformat(myFixture.getFile());
      }
    });

    myFixture.checkResultByFile(getTestName(false) + "_after.hx");
  }

  public void testIf() throws Exception {
    doTest(new HaxeIfSurrounder());
  }

  public void testIfElse() throws Exception {
    doTest(new HaxeIfElseSurrounder());
  }

  public void testWhile() throws Exception {
    doTest(new HaxeWhileSurrounder());
  }

  public void testDoWhile() throws Exception {
    doTest(new HaxeDoWhileSurrounder());
  }

  public void testTryCatch() throws Exception {
    doTest(new HaxeTryCatchSurrounder());
  }
}
