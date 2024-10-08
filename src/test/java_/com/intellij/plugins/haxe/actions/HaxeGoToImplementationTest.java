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
package com.intellij.plugins.haxe.actions;

import consulo.ide.impl.idea.codeInsight.navigation.GotoImplementationHandler;
import consulo.ide.impl.idea.codeInsight.navigation.GotoTargetHandler;
import com.intellij.plugins.haxe.HaxeCodeInsightFixtureTestCase;

/**
 * @author: Fedor.Korotkov
 */
public abstract class HaxeGoToImplementationTest extends HaxeCodeInsightFixtureTestCase {
  @Override
  protected String getBasePath() {
    return "/gotoImplementation/";
  }

  private void doTest(int expectedLength) throws Throwable {
    myFixture.configureByFile(getTestName(false) + ".hx");
    final GotoTargetHandler.GotoData data =
      new consulo.ide.impl.idea.codeInsight.navigation.GotoImplementationHandler().getSourceAndTargetElements(myFixture.getEditor(), myFixture.getFile());
    assertNotNull(myFixture.getFile().toString(), data);
    assertEquals(expectedLength, data.targets.length);
  }

  public void testGti1() throws Throwable {
    doTest(2);
  }

  public void testGti2() throws Throwable {
    doTest(1);
  }

  public void testGti3() throws Throwable {
    doTest(3);
  }

  public void testGti4() throws Throwable {
    doTest(2);
  }
}
