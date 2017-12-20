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

import com.intellij.openapi.actionSystem.DataProvider;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.plugins.haxe.HaxeCodeInsightFixtureTestCase;
import com.intellij.usageView.UsageInfo;
import com.intellij.usages.PsiElementUsageTarget;
import com.intellij.usages.UsageTarget;
import com.intellij.usages.UsageTargetUtil;
import org.jetbrains.annotations.NonNls;

import java.util.Collection;

/**
 * @author: Fedor.Korotkov
 */
public class HaxeFindUsagesTest extends HaxeCodeInsightFixtureTestCase {
  @Override
  protected String getBasePath() {
    return "/findUsages/";
  }

  protected void doTest(int size) throws Throwable {
    final Collection<UsageInfo> elements = findUsages();
    assertNotNull(elements);
    assertEquals(size, elements.size());
  }

  private Collection<UsageInfo> findUsages()
    throws Throwable {
    final UsageTarget[] targets = UsageTargetUtil.findUsageTargets(new DataProvider() {
      @Override
      public Object getData(@NonNls String dataId) {
        return ((EditorEx)myFixture.getEditor()).getDataContext().getData(dataId);
      }
    });

    assert targets != null && targets.length > 0 && targets[0] instanceof PsiElementUsageTarget;
    return myFixture.findUsages(((PsiElementUsageTarget)targets[0]).getElement());
  }

  public void testVarDeclaration() throws Throwable {
    myFixture.configureByFiles("VarDeclaration.hx", "com/bar/Foo.hx");
    doTest(0);
  }

  public void testLocalFunctionParameter() throws Throwable {
    myFixture.configureByFiles("LocalFunctionParameter.hx", "com/bar/Foo.hx");
    doTest(3);
  }

  public void testForDeclaration() throws Throwable {
    myFixture.configureByFiles("ForDeclaration.hx", "com/bar/IBar.hx");
    doTest(3);
  }

  public void testLocalVarDeclaration1() throws Throwable {
    myFixture.configureByFiles("LocalVarDeclaration1.hx");
    doTest(1);
  }

  public void testLocalVarDeclaration2() throws Throwable {
    myFixture.configureByFiles("LocalVarDeclaration2.hx");
    doTest(1);
  }

  public void testFunctionParameter() throws Throwable {
    myFixture.configureByFiles("FunctionParameter.hx");
    doTest(2);
  }

  public void testClassDeclaration() throws Throwable {
    myFixture.configureByFiles("com/bar/ClassToFind.hx", "ClassDeclaration.hx");
    doTest(7);
  }
}
