// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi;

import java.util.List;

import javax.annotation.Nonnull;

public interface HaxeIfStatement extends HaxePsiCompositeElement {

  @Nonnull
  List<HaxeBlockStatement> getBlockStatementList();

  @Nonnull
  List<HaxeBreakStatement> getBreakStatementList();

  @Nonnull
  List<HaxeConditional> getConditionalList();

  @Nonnull
  List<HaxeContinueStatement> getContinueStatementList();

  @Nonnull
  List<HaxeDoWhileStatement> getDoWhileStatementList();

  @Nonnull
  List<HaxeExpression> getExpressionList();

  @Nonnull
  List<HaxeForStatement> getForStatementList();

  @Nonnull
  List<HaxeIfStatement> getIfStatementList();

  @Nonnull
  List<HaxeLocalFunctionDeclaration> getLocalFunctionDeclarationList();

  @Nonnull
  List<HaxeLocalVarDeclaration> getLocalVarDeclarationList();

  @Nonnull
  List<HaxeReturnStatement> getReturnStatementList();

  @Nonnull
  List<HaxeSwitchStatement> getSwitchStatementList();

  @Nonnull
  List<HaxeThrowStatement> getThrowStatementList();

  @Nonnull
  List<HaxeTryStatement> getTryStatementList();

  @Nonnull
  List<HaxeWhileStatement> getWhileStatementList();

}
