// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi;

import java.util.List;

import javax.annotation.*;

public interface HaxeExternClassDeclaration extends HaxeClass {

  @Nonnull
  List<HaxeAutoBuildMacro> getAutoBuildMacroList();

  @Nonnull
  List<HaxeBitmapMeta> getBitmapMetaList();

  @Nonnull
  List<HaxeBuildMacro> getBuildMacroList();

  @Nonnull
  HaxeComponentName getComponentName();

  @Nonnull
  List<HaxeCustomMeta> getCustomMetaList();

  @Nullable
  HaxeExternClassDeclarationBody getExternClassDeclarationBody();

  @Nonnull
  List<HaxeExternOrPrivate> getExternOrPrivateList();

  @Nonnull
  List<HaxeFakeEnumMeta> getFakeEnumMetaList();

  @Nullable
  HaxeGenericParam getGenericParam();

  @Nullable
  HaxeInheritList getInheritList();

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
