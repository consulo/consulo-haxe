// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi;

import java.util.List;

import javax.annotation.*;

public interface HaxeFunctionPrototypeDeclarationWithAttributes extends HaxeComponentWithDeclarationList {

  @Nonnull
  List<HaxeAutoBuildMacro> getAutoBuildMacroList();

  @Nonnull
  List<HaxeBuildMacro> getBuildMacroList();

  @Nullable
  HaxeComponentName getComponentName();

  @Nonnull
  List<HaxeCustomMeta> getCustomMetaList();

  @Nonnull
  List<HaxeDeclarationAttribute> getDeclarationAttributeList();

  @Nullable
  HaxeGenericParam getGenericParam();

  @Nonnull
  List<HaxeGetterMeta> getGetterMetaList();

  @Nonnull
  List<HaxeMetaMeta> getMetaMetaList();

  @Nonnull
  List<HaxeNsMeta> getNsMetaList();

  @Nonnull
  List<HaxeOverloadMeta> getOverloadMetaList();

  @Nullable
  HaxeParameterList getParameterList();

  @Nonnull
  List<HaxeRequireMeta> getRequireMetaList();

  @Nonnull
  List<HaxeSetterMeta> getSetterMetaList();

  @Nullable
  HaxeTypeTag getTypeTag();

}
