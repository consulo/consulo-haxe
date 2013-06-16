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
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ModifiableRootModel;
import org.consulo.module.extension.MutableModuleExtensionWithSdk;
import org.consulo.module.extension.MutableModuleInheritableNamedPointer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author VISTALL
 * @since 14:48/16.06.13
 */
public class HaxeMutableModuleExtension extends HaxeModuleExtension implements MutableModuleExtensionWithSdk<HaxeModuleExtension> {
  @NotNull private final HaxeModuleExtension myModuleExtension;

  public HaxeMutableModuleExtension(@NotNull String id, @NotNull Module module, @NotNull HaxeModuleExtension moduleExtension) {
    super(id, module);
    myModuleExtension = moduleExtension;
  }

  @Nullable
  @Override
  public JComponent createConfigurablePanel(@NotNull ModifiableRootModel model, @Nullable Runnable runnable) {
    return null;
  }

  @Override
  public void setEnabled(boolean b) {
    myIsEnabled = b;
  }

  @Override
  public boolean isModified() {
    return isModifiedImpl(myModuleExtension);
  }

  @Override
  public void commit() {
    myModuleExtension.commit(this);
  }

  @NotNull
  @Override
  public MutableModuleInheritableNamedPointer<Sdk> getInheritableSdk() {
    return (MutableModuleInheritableNamedPointer<Sdk>)super.getInheritableSdk();
  }
}
