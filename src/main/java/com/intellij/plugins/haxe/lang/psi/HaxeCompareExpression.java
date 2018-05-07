// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi;

import java.util.List;

import javax.annotation.Nonnull;

public interface HaxeCompareExpression extends HaxeExpression {

  @Nonnull
  HaxeCompareOperation getCompareOperation();

  @Nonnull
  List<HaxeExpression> getExpressionList();

}
