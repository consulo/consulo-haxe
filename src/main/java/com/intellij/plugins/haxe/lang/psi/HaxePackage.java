package com.intellij.plugins.haxe.lang.psi;

import consulo.language.psi.PsiPackage;
import consulo.util.collection.ArrayFactory;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 12.09.13.
 */
public interface HaxePackage extends PsiPackage {
  HaxePackage[] EMPTY_ARRAY = new HaxePackage[0];

  ArrayFactory<HaxePackage> ARRAY_FACTORY = i -> i == 0 ? EMPTY_ARRAY : new HaxePackage[i];

  @Nonnull
  @Override
  HaxePackage[] getSubPackages();
}
