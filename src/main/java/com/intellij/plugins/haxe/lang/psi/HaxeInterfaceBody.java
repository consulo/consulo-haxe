// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi;

import java.util.List;

import javax.annotation.Nonnull;

public interface HaxeInterfaceBody extends HaxePsiCompositeElement {

  @Nonnull
  List<HaxeConditional> getConditionalList();

  @Nonnull
  List<HaxeFunctionPrototypeDeclarationWithAttributes> getFunctionPrototypeDeclarationWithAttributesList();

  @Nonnull
  List<HaxeVarDeclaration> getVarDeclarationList();

}
