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

import consulo.annotation.component.ExtensionImpl;
import consulo.content.ContentFolderTypeProvider;
import consulo.language.content.ProductionContentFolderTypeProvider;
import consulo.module.content.layer.ContentFolderSupportPatcher;
import consulo.module.content.layer.ModifiableRootModel;

import jakarta.annotation.Nonnull;
import java.util.Set;

/**
 * @author VISTALL
 * @since 13.02.15
 */
@ExtensionImpl
public class HaxeContentFolderSupportPatcher implements ContentFolderSupportPatcher {
  @Override
  public void patch(@Nonnull ModifiableRootModel model, @Nonnull Set<ContentFolderTypeProvider> set) {
    HaxeModuleExtension extension = model.getExtension(HaxeModuleExtension.class);
    if (extension != null) {
      set.add(ProductionContentFolderTypeProvider.getInstance());
    }
  }
}
