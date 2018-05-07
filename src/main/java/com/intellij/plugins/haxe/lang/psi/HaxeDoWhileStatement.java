// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi;

import java.util.List;

import javax.annotation.*;

public interface HaxeDoWhileStatement extends HaxePsiCompositeElement {

  @Nullable
  HaxeBlockStatement getBlockStatement();

  @Nullable
  HaxeBreakStatement getBreakStatement();

  @Nullable
  HaxeConditional getConditional();

  @Nullable
  HaxeContinueStatement getContinueStatement();

  @Nullable
  HaxeDoWhileStatement getDoWhileStatement();

  @Nonnull
  List<HaxeExpression> getExpressionList();

  @Nullable
  HaxeForStatement getForStatement();

  @Nullable
  HaxeIfStatement getIfStatement();

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
