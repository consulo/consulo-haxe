// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi;

import java.util.List;

import javax.annotation.Nonnull;

public interface HaxeBitwiseExpression extends HaxeExpression {

  @Nonnull
  HaxeBitOperation getBitOperation();

  @Nonnull
  List<HaxeExpression> getExpressionList();

}
