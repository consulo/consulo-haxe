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

package consulo.haxe.module.extension.packageSupport;

import consulo.haxe.module.extension.HaxeModuleExtension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.project.Project;
import com.intellij.plugins.haxe.lang.psi.HaxePackage;
import consulo.psi.PsiPackageManager;

/**
 * @author VISTALL
 * @since 12.09.13.
 */
public class HaxePackageUtil
{
	public static HaxePackage findPackage(@NotNull Project project, @NotNull String packageName)
	{
		return (HaxePackage) PsiPackageManager.getInstance(project).findPackage(packageName, HaxeModuleExtension.class);
	}

	public static boolean isQualifiedName(@Nullable String text)
	{
		if(text == null)
		{
			return false;
		}
		int index = 0;
		while(true)
		{
			int index1 = text.indexOf('.', index);
			if(index1 < 0)
			{
				index1 = text.length();
			}
			//if (!isIdentifier(text.substring(index, index1))) return false; TODO [VISTALL] check for keywords
			if(index1 == text.length())
			{
				return true;
			}
			index = index1 + 1;
		}
	}
}
