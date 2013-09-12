package com.intellij.plugins.haxe.lang.psi.impl;

import com.intellij.lang.Language;
import com.intellij.plugins.haxe.HaxeLanguage;
import com.intellij.plugins.haxe.lang.psi.HaxePackage;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.file.PsiPackageBase;
import com.intellij.util.ArrayFactory;
import org.consulo.module.extension.ModuleExtension;
import org.consulo.psi.PsiPackage;
import org.consulo.psi.PsiPackageManager;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

/**
 * @author VISTALL
 * @since 12.09.13.
 */
public class HaxePackageImpl extends PsiPackageBase implements HaxePackage {
	public HaxePackageImpl(PsiManager manager, PsiPackageManager packageManager, Class<? extends ModuleExtension> extensionClass, String qualifiedName) {
		super(manager, packageManager, extensionClass, qualifiedName);
	}

	@Override
	protected Collection<PsiDirectory> getAllDirectories() {
		return Collections.emptyList();
	}

	@Override
	protected ArrayFactory<? extends PsiPackage> getPackageArrayFactory() {
		return HaxePackage.ARRAY_FACTORY;
	}

	@NotNull
	@Override
	public HaxePackage[] getSubPackages() {
		return (HaxePackage[]) super.getSubPackages();
	}

	@NotNull
	@Override
	public Language getLanguage() {
		return HaxeLanguage.INSTANCE;
	}
}
