// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi;

import java.util.List;

import javax.annotation.Nonnull;

public interface HaxeClassBody extends HaxePsiCompositeElement {

  @Nonnull
  List<HaxeConditional> getConditionalList();

  @Nonnull
  List<HaxeFunctionDeclarationWithAttributes> getFunctionDeclarationWithAttributesList();

  @Nonnull
  List<HaxeVarDeclaration> getVarDeclarationList();

}
