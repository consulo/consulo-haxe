// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi;

import javax.annotation.Nullable;

public interface HaxeFunctionLiteral extends HaxeExpression {

  @Nullable
  HaxeBlockStatement getBlockStatement();

  @Nullable
  HaxeExpression getExpression();

  @Nullable
  HaxeParameterList getParameterList();

  @Nullable
  HaxeReturnStatement getReturnStatement();

  @Nullable
  HaxeThrowStatement getThrowStatement();

  @Nullable
  HaxeTypeTag getTypeTag();

}
