/*
 * Copyright 2013-2015 must-be.org
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

package consulo.haxe.module.extension;

import java.util.Set;

import consulo.haxe.module.extension.HaxeModuleExtension;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.roots.ModifiableRootModel;
import consulo.roots.ContentFolderSupportPatcher;
import consulo.roots.ContentFolderTypeProvider;
import consulo.roots.impl.ProductionContentFolderTypeProvider;

/**
 * @author VISTALL
 * @since 13.02.15
 */
public class HaxeContentFolderSupportPatcher implements ContentFolderSupportPatcher
{
	@Override
	public void patch(@NotNull ModifiableRootModel model, @NotNull Set<ContentFolderTypeProvider> set)
	{
		HaxeModuleExtension extension = model.getExtension(HaxeModuleExtension.class);
		if(extension != null)
		{
			set.add(ProductionContentFolderTypeProvider.getInstance());
		}
	}
}
