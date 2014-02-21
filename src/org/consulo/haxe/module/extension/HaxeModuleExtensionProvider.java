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
package org.consulo.haxe.module.extension;

import javax.swing.Icon;

import org.consulo.module.extension.ModuleExtensionProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.module.Module;
import com.intellij.plugins.haxe.HaxeBundle;
import icons.HaxeIcons;

/**
 * @author VISTALL
 * @since 14:48/16.06.13
 */
public class HaxeModuleExtensionProvider implements ModuleExtensionProvider<HaxeModuleExtension, HaxeMutableModuleExtension>
{
	@Nullable
	@Override
	public Icon getIcon()
	{
		return HaxeIcons.Haxe;
	}

	@NotNull
	@Override
	public String getName()
	{
		return HaxeBundle.message("haxe.title");
	}

	@NotNull
	@Override
	public HaxeModuleExtension createImmutable(@NotNull String s, @NotNull Module module)
	{
		return new HaxeModuleExtension(s, module);
	}

	@NotNull
	@Override
	public HaxeMutableModuleExtension createMutable(@NotNull String s, @NotNull Module module)
	{
		return new HaxeMutableModuleExtension(s, module);
	}
}
