// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi;

import java.util.List;

import javax.annotation.*;

public interface HaxeEnumDeclaration extends HaxeClass {

  @Nonnull
  List<HaxeAutoBuildMacro> getAutoBuildMacroList();

  @Nonnull
  List<HaxeBitmapMeta> getBitmapMetaList();

  @Nonnull
  List<HaxeBuildMacro> getBuildMacroList();

  @Nullable
  HaxeComponentName getComponentName();

  @Nonnull
  List<HaxeCustomMeta> getCustomMetaList();

  @Nullable
  HaxeEnumBody getEnumBody();

  @Nullable
  HaxeExternOrPrivate getExternOrPrivate();

  @Nonnull
  List<HaxeFakeEnumMeta> getFakeEnumMetaList();

  @Nullable
  HaxeGenericParam getGenericParam();

  @Nonnull
  List<HaxeJsRequireMeta> getJsRequireMetaList();

  @Nonnull
  List<HaxeMetaMeta> getMetaMetaList();

  @Nonnull
  List<HaxeNativeMeta> getNativeMetaList();

  @Nonnull
  List<HaxeNsMeta> getNsMetaList();

  @Nonnull
  List<HaxeRequireMeta> getRequireMetaList();

}
