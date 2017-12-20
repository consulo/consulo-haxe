package com.intellij.plugins.haxe.lang.psi;

import com.intellij.util.ArrayFactory;
import consulo.psi.PsiPackage;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 12.09.13.
 */
public interface HaxePackage extends PsiPackage
{
	HaxePackage[] EMPTY_ARRAY = new HaxePackage[0];

	ArrayFactory<HaxePackage> ARRAY_FACTORY = new ArrayFactory<HaxePackage>() {
		@NotNull
		@Override
		public HaxePackage[] create(int i) {
			return i == 0 ? EMPTY_ARRAY : new HaxePackage[i];
		}
	};

	@NotNull
	@Override
	HaxePackage[] getSubPackages();
}
