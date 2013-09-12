package org.consulo.haxe.module.extension.packageSupport;

import com.intellij.plugins.haxe.lang.psi.impl.HaxePackageImpl;
import com.intellij.psi.PsiManager;
import org.consulo.haxe.module.extension.HaxeModuleExtension;
import org.consulo.module.extension.ModuleExtension;
import org.consulo.psi.PsiPackage;
import org.consulo.psi.PsiPackageManager;
import org.consulo.psi.PsiPackageSupportProvider;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 12.09.13.
 */
public class HaxePsiPackageSupportProvider implements PsiPackageSupportProvider {
	@NotNull
	@Override
	public Class<? extends ModuleExtension> getSupportedModuleExtensionClass() {
		return HaxeModuleExtension.class;
	}

	@NotNull
	@Override
	public PsiPackage createPackage(@NotNull PsiManager psiManager, @NotNull PsiPackageManager psiPackageManager, @NotNull Class<? extends ModuleExtension> aClass, @NotNull String s) {
		return new HaxePackageImpl(psiManager, psiPackageManager, aClass, s);
	}
}
