// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi;

import java.util.List;

import javax.annotation.*;

public interface HaxeMultiplicativeExpression extends HaxeExpression {

  @Nonnull
  List<HaxeExpression> getExpressionList();

  @Nullable
  HaxeIfStatement getIfStatement();

  @Nullable
  HaxeSwitchStatement getSwitchStatement();

  @Nullable
  HaxeTryStatement getTryStatement();

}
