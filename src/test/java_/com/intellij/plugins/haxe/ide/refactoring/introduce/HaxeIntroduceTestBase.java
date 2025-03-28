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

import consulo.ide.impl.idea.codeInsight.template.impl.TemplateManagerImpl;
import consulo.language.editor.template.TemplateState;
import com.intellij.plugins.haxe.HaxeCodeInsightFixtureTestCase;
import com.intellij.plugins.haxe.lang.psi.HaxeExpression;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.ide.impl.idea.util.Consumer;
import consulo.util.lang.StringUtil;

import jakarta.annotation.Nullable;

import java.util.Collection;

/**
 * @author: Fedor.Korotkov
 */
public abstract class HaxeIntroduceTestBase extends HaxeCodeInsightFixtureTestCase {
  protected void doTestSuggestions(Class<? extends HaxeExpression> parentClass, String... expectedNames) {
    final Collection<String> names = buildSuggestions(parentClass);
    for (String expectedName : expectedNames) {
      assertTrue(StringUtil.join(names, ", "), names.contains(expectedName));
    }
  }

  protected Collection<String> buildSuggestions(Class<? extends HaxeExpression> parentClass) {
    myFixture.configureByFile(getTestName(true) + ".hx");
    HaxeIntroduceHandler handler = createHandler();
    HaxeExpression expr = PsiTreeUtil.getParentOfType(
      myFixture.getFile().findElementAt(myFixture.getEditor().getCaretModel().getOffset()),
      parentClass
    );
    return handler.getSuggestedNames(expr);
  }

  protected abstract HaxeIntroduceHandler createHandler();

  protected void doTest() {
    doTest(null, true);
  }

  protected void doTest(@Nullable Consumer<HaxeIntroduceOperation> customization, boolean replaceAll) {
    myFixture.configureByFile(getTestName(true) + ".hx");
    boolean inplaceEnabled = myFixture.getEditor().getSettings().isVariableInplaceRenameEnabled();
    try {
      myFixture.getEditor().getSettings().setVariableInplaceRenameEnabled(false);
      HaxeIntroduceHandler handler = createHandler();
      final HaxeIntroduceOperation operation =
        new HaxeIntroduceOperation(myFixture.getProject(), myFixture.getEditor(), myFixture.getFile(), "foo");
      operation.setReplaceAll(replaceAll);
      if (customization != null) {
        customization.consume(operation);
      }
      handler.performAction(operation);
      myFixture.checkResultByFile(getTestName(true) + ".after.hx");
    }
    finally {
      myFixture.getEditor().getSettings().setVariableInplaceRenameEnabled(inplaceEnabled);
    }
  }

  protected void doTestInplace(@Nullable Consumer<HaxeIntroduceOperation> customization) {
    String name = getTestName(true);
    myFixture.configureByFile(name + ".hx");
    final boolean enabled = myFixture.getEditor().getSettings().isVariableInplaceRenameEnabled();
    consulo.ide.impl.idea.codeInsight.template.impl.TemplateManagerImpl.setTemplateTesting(getProject(), getTestRootDisposable());
    myFixture.getEditor().getSettings().setVariableInplaceRenameEnabled(true);

    HaxeIntroduceHandler handler = createHandler();
    final HaxeIntroduceOperation introduceOperation =
      new HaxeIntroduceOperation(myFixture.getProject(), myFixture.getEditor(), myFixture.getFile(), "a");
    introduceOperation.setReplaceAll(true);
    if (customization != null) {
      customization.consume(introduceOperation);
    }
    handler.performAction(introduceOperation);

    TemplateState state = consulo.ide.impl.idea.codeInsight.template.impl.TemplateManagerImpl.getTemplateState(myFixture.getEditor());
    assert state != null;
    state.gotoEnd(false);
    myFixture.checkResultByFile(name + ".after.hx", true);
    myFixture.getEditor().getSettings().setVariableInplaceRenameEnabled(enabled);
  }
}
