// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface HaxeMultiplicativeExpression extends HaxeExpression {

  @NotNull
  List<HaxeExpression> getExpressionList();

  @Nullable
  HaxeIfStatement getIfStatement();

  @Nullable
  HaxeSwitchStatement getSwitchStatement();

  @Nullable
  HaxeTryStatement getTryStatement();

}
