/*
 * Copyright 2000-2013 JetBrains s.r.o.
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

import com.intellij.openapi.module.Module;
import com.intellij.openapi.projectRoots.SdkType;
import com.intellij.plugins.haxe.config.sdk.HaxeSdkType;
import org.consulo.module.extension.impl.ModuleExtensionWithSdkImpl;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 13:54/16.06.13
 */
public class HaxeModuleExtension extends ModuleExtensionWithSdkImpl<HaxeModuleExtension> {
  public HaxeModuleExtension(@NotNull String id,
                             @NotNull Module module) {
    super(id, module);
  }

  @Override
  protected Class<? extends SdkType> getSdkTypeClass() {
    return HaxeSdkType.class;
  }
}