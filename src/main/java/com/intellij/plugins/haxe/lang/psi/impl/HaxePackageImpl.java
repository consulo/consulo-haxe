package com.intellij.plugins.haxe.lang.psi.impl;

import org.jetbrains.annotations.NotNull;
import com.intellij.lang.Language;
import com.intellij.plugins.haxe.HaxeLanguage;
import com.intellij.plugins.haxe.lang.psi.HaxePackage;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.file.PsiPackageBase;
import com.intellij.util.ArrayFactory;
import consulo.module.extension.ModuleExtension;
import consulo.psi.PsiPackage;
import consulo.psi.PsiPackageManager;

/**
 * @author VISTALL
 * @since 12.09.13.
 */
public class HaxePackageImpl extends PsiPackageBase implements HaxePackage
{
	public HaxePackageImpl(PsiManager manager, PsiPackageManager packageManager, Class<? extends ModuleExtension> extensionClass, String qualifiedName)
	{
		super(manager, packageManager, extensionClass, qualifiedName);
	}

	@Override
	protected ArrayFactory<? extends PsiPackage> getPackageArrayFactory()
	{
		return HaxePackage.ARRAY_FACTORY;
	}

	@NotNull
	@Override
	public HaxePackage[] getSubPackages()
	{
		return (HaxePackage[]) super.getSubPackages();
	}

	@NotNull
	@Override
	public Language getLanguage()
	{
		return HaxeLanguage.INSTANCE;
	}
}
