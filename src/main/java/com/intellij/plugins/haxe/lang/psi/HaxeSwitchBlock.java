// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi;

import java.util.List;

import javax.annotation.*;

public interface HaxeSwitchBlock extends HaxePsiCompositeElement {

  @Nullable
  HaxeDefaultCase getDefaultCase();

  @Nonnull
  List<HaxeSwitchCase> getSwitchCaseList();

}
