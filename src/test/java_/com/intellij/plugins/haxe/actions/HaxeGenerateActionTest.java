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

import jakarta.annotation.Nonnull;

import consulo.ide.impl.idea.openapi.util.io.FileUtil;
import com.intellij.plugins.haxe.ide.generation.BaseHaxeGenerateHandler;
import com.intellij.plugins.haxe.ide.generation.CreateGetterSetterFix;
import com.intellij.plugins.haxe.ide.generation.HaxeGenerateAccessorHandler;
import com.intellij.plugins.haxe.ide.generation.HaxeImplementMethodHandler;
import com.intellij.plugins.haxe.ide.generation.HaxeOverrideMethodHandler;
import com.intellij.plugins.haxe.util.HaxeTestUtils;
import com.intellij.testFramework.LightPlatformCodeInsightTestCase;

/**
 * @author: Fedor.Korotkov
 */
abstract public class HaxeGenerateActionTest extends LightPlatformCodeInsightTestCase {
  @Nonnull
  @Override
  protected String getTestDataPath() {
    return HaxeTestUtils.BASE_TEST_DATA_PATH + FileUtil.toSystemDependentName("/generate/");
  }

  protected void doOverrideTest() {
    doTest(new HaxeOverrideMethodHandler());
  }

  protected void doImplementTest() {
    doTest(new HaxeImplementMethodHandler());
  }

  protected void doGetterSetterTest(CreateGetterSetterFix.Strategy strategy) {
    configureByFile(getTestName(false) + ".hx");
    doTest(new HaxeGenerateAccessorHandler(strategy) {
      @Override
      protected String getTitle() {
        return "";
      }
    });
  }

  protected void doTest(BaseHaxeGenerateHandler anAction) {
    anAction.invoke(getProject(), getEditor(), getFile());
    checkResultByFile(getTestName(false) + ".txt");
  }

  public void testImplement1() throws Throwable {
    configureByFile("Implement1.hx");
    doImplementTest();
  }

  public void testImplement2() throws Throwable {
    configureByFile("Implement2.hx");
    doImplementTest();
  }

  public void testOverride1() throws Throwable {
    configureByFile("Override1.hx");
    doOverrideTest();
  }

  public void testGetter1() throws Throwable {
    doGetterSetterTest(CreateGetterSetterFix.Strategy.GETTER);
  }

  public void testGetter2() throws Throwable {
    doGetterSetterTest(CreateGetterSetterFix.Strategy.GETTER);
  }

  public void testSetter1() throws Throwable {
    doGetterSetterTest(CreateGetterSetterFix.Strategy.SETTER);
  }

  public void testSetter2() throws Throwable {
    doGetterSetterTest(CreateGetterSetterFix.Strategy.SETTER);
  }

  public void testGetterSetter1() throws Throwable {
    doGetterSetterTest(CreateGetterSetterFix.Strategy.GETTERSETTER);
  }

  public void testGetterSetter2() throws Throwable {
    doGetterSetterTest(CreateGetterSetterFix.Strategy.GETTERSETTER);
  }
}
