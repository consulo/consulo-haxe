// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi;

import javax.annotation.*;

public interface HaxeLocalFunctionDeclaration extends HaxeComponent {

  @Nullable
  HaxeBlockStatement getBlockStatement();

  @Nonnull
  HaxeComponentName getComponentName();

  @Nullable
  HaxeExpression getExpression();

  @Nullable
  HaxeGenericParam getGenericParam();

  @Nullable
  HaxeParameterList getParameterList();

  @Nullable
  HaxeReturnStatement getReturnStatement();

  @Nullable
  HaxeThrowStatement getThrowStatement();

  @Nullable
  HaxeTypeTag getTypeTag();

}
