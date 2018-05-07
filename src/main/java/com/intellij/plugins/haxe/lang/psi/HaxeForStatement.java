// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi;

import javax.annotation.Nullable;

public interface HaxeForStatement extends HaxeComponent {

  @Nullable
  HaxeBlockStatement getBlockStatement();

  @Nullable
  HaxeBreakStatement getBreakStatement();

  @Nullable
  HaxeComponentName getComponentName();

  @Nullable
  HaxeConditional getConditional();

  @Nullable
  HaxeContinueStatement getContinueStatement();

  @Nullable
  HaxeDoWhileStatement getDoWhileStatement();

  @Nullable
  HaxeExpression getExpression();

  @Nullable
  HaxeForStatement getForStatement();

  @Nullable
  HaxeIfStatement getIfStatement();

  @Nullable
  HaxeIterable getIterable();

  @Nullable
  HaxeLocalFunctionDeclaration getLocalFunctionDeclaration();

  @Nullable
  HaxeLocalVarDeclaration getLocalVarDeclaration();

  @Nullable
  HaxeReturnStatement getReturnStatement();

  @Nullable
  HaxeSwitchStatement getSwitchStatement();

  @Nullable
  HaxeThrowStatement getThrowStatement();

  @Nullable
  HaxeTryStatement getTryStatement();

  @Nullable
  HaxeWhileStatement getWhileStatement();

}
