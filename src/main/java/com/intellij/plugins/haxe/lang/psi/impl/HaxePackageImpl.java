package com.intellij.plugins.haxe.lang.psi.impl;

import com.intellij.plugins.haxe.HaxeLanguage;
import com.intellij.plugins.haxe.lang.psi.HaxePackage;
import consulo.language.Language;
import consulo.language.impl.psi.PsiPackageBase;
import consulo.language.psi.PsiManager;
import consulo.language.psi.PsiPackage;
import consulo.language.psi.PsiPackageManager;
import consulo.module.extension.ModuleExtension;
import consulo.util.collection.ArrayFactory;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 12.09.13.
 */
public class HaxePackageImpl extends PsiPackageBase implements HaxePackage {
  public HaxePackageImpl(PsiManager manager, PsiPackageManager packageManager, Class<? extends ModuleExtension> extensionClass, String qualifiedName) {
    super(manager, packageManager, extensionClass, qualifiedName);
  }

  @Override
  protected ArrayFactory<? extends PsiPackage> getPackageArrayFactory() {
    return HaxePackage.ARRAY_FACTORY;
  }

  @Nonnull
  @Override
  public HaxePackage[] getSubPackages() {
    return (HaxePackage[]) super.getSubPackages();
  }

  @Nonnull
  @Override
  public Language getLanguage() {
    return HaxeLanguage.INSTANCE;
  }
}
