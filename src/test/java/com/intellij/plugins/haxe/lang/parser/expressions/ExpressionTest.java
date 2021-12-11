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
package com.intellij.plugins.haxe.lang.parser.expressions;

import com.intellij.plugins.haxe.lang.parser.HaxeParsingTestBase;

/**
 * @author fedor.korotkov
 */
public abstract class ExpressionTest extends HaxeParsingTestBase {
  public ExpressionTest() {
    super("parsing", "haxe", "expressions");
  }

  public void testHaxe3() throws Throwable {
    doTest(true);
  }

  public void testTest1() throws Throwable {
    doTest(true);
  }

  public void testTest2() throws Throwable {
    doTest(true);
  }

  public void testTest3() throws Throwable {
    doTest(true);
  }

  public void testTest4() throws Throwable {
    doTest(true);
  }

  public void testTest5() throws Throwable {
    doTest(true);
  }

  public void testTest6() throws Throwable {
    doTest(true);
  }

  public void testTest7() throws Throwable {
    doTest(true);
  }

  public void testTest8() throws Throwable {
    doTest(true);
  }
}