// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi;

import java.util.List;

import javax.annotation.*;

public interface HaxeSwitchCase extends HaxePsiCompositeElement {

  @Nonnull
  List<HaxeExpression> getExpressionList();

  @Nullable
  HaxeSwitchCaseBlock getSwitchCaseBlock();

}
