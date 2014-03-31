/*
 * Copyright 2013-2014 must-be.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.consulo.haxe.module.extension.packageSupport;

import org.consulo.haxe.module.extension.HaxeModuleExtension;
import org.consulo.module.extension.ModuleExtension;
import org.consulo.psi.PsiPackage;
import org.consulo.psi.PsiPackageManager;
import org.consulo.psi.PsiPackageSupportProvider;
import org.jetbrains.annotations.NotNull;
import com.intellij.plugins.haxe.lang.psi.impl.HaxePackageImpl;
import com.intellij.psi.PsiManager;

/**
 * @author VISTALL
 * @since 12.09.13.
 */
public class HaxePsiPackageSupportProvider implements PsiPackageSupportProvider
{
	@Override
	public boolean isSupported(@NotNull ModuleExtension moduleExtension)
	{
		return moduleExtension instanceof HaxeModuleExtension;
	}

	@NotNull
	@Override
	public PsiPackage createPackage(@NotNull PsiManager psiManager, @NotNull PsiPackageManager psiPackageManager,
			@NotNull Class<? extends ModuleExtension> aClass, @NotNull String s)
	{
		return new HaxePackageImpl(psiManager, psiPackageManager, aClass, s);
	}
}
