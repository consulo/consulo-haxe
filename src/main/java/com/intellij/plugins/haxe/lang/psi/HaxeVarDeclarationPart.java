// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi;

import javax.annotation.*;

public interface HaxeVarDeclarationPart extends HaxeComponent {

  @Nonnull
  HaxeComponentName getComponentName();

  @Nullable
  HaxePropertyDeclaration getPropertyDeclaration();

  @Nullable
  HaxeTypeTag getTypeTag();

  @Nullable
  HaxeVarInit getVarInit();

}
