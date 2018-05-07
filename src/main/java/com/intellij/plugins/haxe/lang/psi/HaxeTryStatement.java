// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi;

import java.util.List;

import javax.annotation.*;

public interface HaxeTryStatement extends HaxePsiCompositeElement {

  @Nullable
  HaxeBlockStatement getBlockStatement();

  @Nullable
  HaxeBreakStatement getBreakStatement();

  @Nonnull
  List<HaxeCatchStatement> getCatchStatementList();

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
