package org.consulo.haxe.module.extension.packageSupport;

import com.intellij.openapi.project.Project;
import com.intellij.plugins.haxe.lang.psi.HaxePackage;
import org.consulo.haxe.module.extension.HaxeModuleExtension;
import org.consulo.psi.PsiPackageManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author VISTALL
 * @since 12.09.13.
 */
public class HaxePackageUtil {
	public static HaxePackage findPackage(@NotNull Project project, @NotNull String packageName) {
		return (HaxePackage) PsiPackageManager.getInstance(project).findPackage(packageName, HaxeModuleExtension.class);
	}

	public static boolean isQualifiedName(@Nullable String text) {
		if (text == null) return false;
		int index = 0;
		while (true) {
			int index1 = text.indexOf('.', index);
			if (index1 < 0) index1 = text.length();
			//if (!isIdentifier(text.substring(index, index1))) return false; TODO [VISTALL] check for keywords
			if (index1 == text.length()) return true;
			index = index1 + 1;
		}
	}
}
