// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi;

import java.util.List;

import javax.annotation.Nonnull;

public interface HaxeVarDeclaration extends HaxePsiCompositeElement {

  @Nonnull
  List<HaxeAutoBuildMacro> getAutoBuildMacroList();

  @Nonnull
  List<HaxeBuildMacro> getBuildMacroList();

  @Nonnull
  List<HaxeCustomMeta> getCustomMetaList();

  @Nonnull
  List<HaxeDeclarationAttribute> getDeclarationAttributeList();

  @Nonnull
  List<HaxeGetterMeta> getGetterMetaList();

  @Nonnull
  List<HaxeMetaMeta> getMetaMetaList();

  @Nonnull
  List<HaxeNsMeta> getNsMetaList();

  @Nonnull
  List<HaxeRequireMeta> getRequireMetaList();

  @Nonnull
  List<HaxeSetterMeta> getSetterMetaList();

  @Nonnull
  List<HaxeVarDeclarationPart> getVarDeclarationPartList();

}
