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

import consulo.language.codeStyle.CodeStyleManager;
import consulo.language.codeStyle.CodeStyleSettings;
import consulo.language.codeStyle.CodeStyleSettingsManager;
import consulo.language.editor.completion.lookup.LookupManager;
import consulo.ide.impl.idea.codeInsight.lookup.impl.LookupImpl;
import consulo.ide.impl.idea.codeInsight.template.impl.actions.ListTemplatesAction;
import consulo.application.ApplicationManager;
import consulo.codeEditor.Editor;
import com.intellij.plugins.haxe.HaxeCodeInsightFixtureTestCase;
import com.intellij.plugins.haxe.HaxeFileType;
import consulo.language.editor.completion.lookup.Lookup;
import consulo.project.Project;

/**
 * @author: Fedor.Korotkov
 */
public abstract class HaxeLiveTemplatesTest extends HaxeCodeInsightFixtureTestCase {
  @Override
  protected String getBasePath() {
    return "/liveTemplates/";
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

  public static void expandTemplate(final Editor editor) {
    new ListTemplatesAction().actionPerformedImpl(editor.getProject(), editor);
    ((consulo.ide.impl.idea.codeInsight.lookup.impl.LookupImpl)LookupManager.getActiveLookup(editor)).finishLookup(Lookup.NORMAL_SELECT_CHAR);
  }

  private void doTest(String... files) throws Exception {
    myFixture.configureByFiles(files);
    expandTemplate(myFixture.getEditor());
    ApplicationManager.getApplication().runWriteAction(new Runnable() {
      @Override
      public void run() {
        CodeStyleManager.getInstance(myFixture.getProject()).reformat(myFixture.getFile());
      }
    });
    myFixture.getEditor().getSelectionModel().removeSelection();
    myFixture.checkResultByFile(getTestName(false) + "_after.hx");
  }

  public void testIter() throws Throwable {
    doTest("Iter.hx", "Array.hx");
  }

  public void testItar() throws Throwable {
    doTest("Itar.hx", "Array.hx");
  }
}
