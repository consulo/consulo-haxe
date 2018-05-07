// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi;

import java.util.List;

import javax.annotation.*;

public interface HaxeFunctionDeclarationWithAttributes extends HaxeComponentWithDeclarationList {

  @Nonnull
  List<HaxeAutoBuildMacro> getAutoBuildMacroList();

  @Nullable
  HaxeBlockStatement getBlockStatement();

  @Nonnull
  List<HaxeBuildMacro> getBuildMacroList();

  @Nullable
  HaxeComponentName getComponentName();

  @Nonnull
  List<HaxeCustomMeta> getCustomMetaList();

  @Nonnull
  List<HaxeDeclarationAttribute> getDeclarationAttributeList();

  @Nullable
  HaxeExpression getExpression();

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

  @Nullable
  HaxeReturnStatement getReturnStatement();

  @Nonnull
  List<HaxeSetterMeta> getSetterMetaList();

  @Nullable
  HaxeThrowStatement getThrowStatement();

  @Nullable
  HaxeTypeTag getTypeTag();

}
