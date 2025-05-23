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
package com.intellij.plugins.haxe.ide.module;

import com.intellij.plugins.haxe.config.HaxeTarget;
import com.intellij.plugins.haxe.config.NMETarget;
import com.intellij.plugins.haxe.module.HaxeModuleSettingsBase;
import com.intellij.plugins.haxe.module.impl.HaxeModuleSettingsBaseImpl;
import consulo.annotation.component.ComponentScope;
import consulo.annotation.component.ServiceAPI;
import consulo.annotation.component.ServiceImpl;
import consulo.component.persist.PersistentStateComponent;
import consulo.component.persist.State;
import consulo.component.persist.Storage;
import consulo.module.Module;
import consulo.util.xml.serializer.XmlSerializerUtil;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import jakarta.annotation.Nonnull;

/**
 * @author: Fedor.Korotkov
 */
@Singleton
@State(
    name = "HaxeModuleSettingsStorage",
    storages = {
        @Storage(
            file = "$MODULE_FILE$"
        )
    }
)
@ServiceAPI(ComponentScope.MODULE)
@ServiceImpl
public class HaxeModuleSettings extends HaxeModuleSettingsBaseImpl
    implements PersistentStateComponent<HaxeModuleSettings>, HaxeModuleSettingsBase {

  private String flexSdkName = "";
  private String myHXCPPPort = "";

  @Inject
  public HaxeModuleSettings() {
  }

  public HaxeModuleSettings(String mainClass,
                            HaxeTarget haxeTarget,
                            NMETarget nmeTarget,
                            String arguments,
                            String nmeFlags,
                            boolean excludeFromCompilation,
                            String outputFileName,
                            String flexSdkName,
                            int buildConfig,
                            String hxmlPath,
                            String nmmlPath) {
    super(mainClass, outputFileName, arguments, nmeFlags, excludeFromCompilation, haxeTarget, nmeTarget, hxmlPath, nmmlPath, buildConfig);
    this.flexSdkName = flexSdkName;
  }

  @Override
  public HaxeModuleSettings getState() {
    return this;
  }

  @Override
  public void loadState(HaxeModuleSettings state) {
    XmlSerializerUtil.copyBean(state, this);
  }

  public void setFlexSdkName(String flexSdkName) {
    this.flexSdkName = flexSdkName;
  }

  public String getFlexSdkName() {
    return flexSdkName;
  }

  public static HaxeModuleSettings getInstance(@Nonnull Module module) {
    return module.getInstance(HaxeModuleSettings.class);
  }

  public String getHXCPPPort() {
    return myHXCPPPort;
  }

  public void setHXCPPPort(String value) {
    myHXCPPPort = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    HaxeModuleSettings settings = (HaxeModuleSettings) o;

    if (excludeFromCompilation != settings.excludeFromCompilation) return false;
    if (buildConfig != settings.buildConfig) return false;
    if (arguments != null ? !arguments.equals(settings.arguments) : settings.arguments != null) return false;
    if (nmeFlags != null ? !nmeFlags.equals(settings.nmeFlags) : settings.nmeFlags != null) return false;
    if (flexSdkName != null ? !flexSdkName.equals(settings.flexSdkName) : settings.flexSdkName != null) return false;
    if (hxmlPath != null ? !hxmlPath.equals(settings.hxmlPath) : settings.hxmlPath != null) return false;
    if (mainClass != null ? !mainClass.equals(settings.mainClass) : settings.mainClass != null) return false;
    if (outputFileName != null ? !outputFileName.equals(settings.outputFileName) : settings.outputFileName != null)
      return false;
    if (haxeTarget != settings.haxeTarget) return false;
    if (nmeTarget != settings.nmeTarget) return false;
    if (myHXCPPPort != settings.myHXCPPPort) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = mainClass != null ? mainClass.hashCode() : 0;
    result = 31 * result + (outputFileName != null ? outputFileName.hashCode() : 0);
    result = 31 * result + (arguments != null ? arguments.hashCode() : 0);
    result = 31 * result + (nmeFlags != null ? nmeFlags.hashCode() : 0);
    result = 31 * result + (excludeFromCompilation ? 1 : 0);
    result = 31 * result + (haxeTarget != null ? haxeTarget.hashCode() : 0);
    result = 31 * result + (nmeTarget != null ? nmeTarget.hashCode() : 0);
    result = 31 * result + (flexSdkName != null ? flexSdkName.hashCode() : 0);
    result = 31 * result + (hxmlPath != null ? hxmlPath.hashCode() : 0);
    result = 31 * result + (myHXCPPPort != null ? myHXCPPPort.hashCode() : 0);
    result = 31 * result + buildConfig;
    return result;
  }
}
