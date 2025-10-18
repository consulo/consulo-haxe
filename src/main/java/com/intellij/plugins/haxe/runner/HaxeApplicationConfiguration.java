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
package com.intellij.plugins.haxe.runner;

import com.intellij.plugins.haxe.ide.module.HaxeModuleSettings;
import com.intellij.plugins.haxe.runner.ui.HaxeRunConfigurationEditorForm;
import consulo.execution.RuntimeConfigurationException;
import consulo.execution.configuration.ModuleBasedConfiguration;
import consulo.execution.configuration.RunConfiguration;
import consulo.execution.configuration.RunConfigurationWithSuppressedDefaultRunAction;
import consulo.execution.configuration.RunProfileState;
import consulo.execution.configuration.ui.SettingsEditor;
import consulo.execution.executor.Executor;
import consulo.execution.runner.ExecutionEnvironment;
import consulo.haxe.localize.HaxeLocalize;
import consulo.module.Module;
import consulo.module.ModuleManager;
import consulo.process.ExecutionException;
import consulo.project.Project;
import consulo.util.xml.serializer.InvalidDataException;
import consulo.util.xml.serializer.WriteExternalException;
import consulo.util.xml.serializer.XmlSerializer;
import jakarta.annotation.Nonnull;
import org.jdom.Element;

import java.util.Arrays;
import java.util.Collection;

public class HaxeApplicationConfiguration extends ModuleBasedConfiguration<HaxeApplicationModuleBasedConfiguration>
  implements RunConfigurationWithSuppressedDefaultRunAction
{
  private boolean customFileToLaunch = false;
  private String customFileToLaunchPath = "";
  private String customExecutablePath = "";
  private boolean customExecutable = false;

  public HaxeApplicationConfiguration(String name, Project project, HaxeRunConfigurationType configurationType) {
    super(name, new HaxeApplicationModuleBasedConfiguration(project), configurationType.getConfigurationFactories()[0]);
  }

  @Override
  public Collection<Module> getValidModules() {
    Module[] modules = ModuleManager.getInstance(getProject()).getModules();
    return Arrays.asList(modules);
  }

  @Override
  protected ModuleBasedConfiguration createInstance() {
    return new HaxeApplicationConfiguration(getName(), getProject(), HaxeRunConfigurationType.getInstance());
  }

  public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
    return new HaxeRunConfigurationEditorForm(getProject());
  }

  public RunProfileState getState(@Nonnull Executor executor, @Nonnull ExecutionEnvironment env) throws ExecutionException
  {
    return HaxeRunner.EMPTY_RUN_STATE;
  }

  @Override
  public void checkConfiguration() throws RuntimeConfigurationException
  {
    super.checkConfiguration();
    final HaxeApplicationModuleBasedConfiguration configurationModule = getConfigurationModule();
    final Module module = configurationModule.getModule();
    if (module == null) {
      throw new RuntimeConfigurationException(HaxeLocalize.haxeRunNoModule(getName()));
    }
    final HaxeModuleSettings settings = HaxeModuleSettings.getInstance(module);
    if (settings.isUseHxmlToBuild() && !customFileToLaunch) {
      throw new RuntimeConfigurationException(HaxeLocalize.haxeRunSelectCustomFile());
    }
    if (settings.isUseNmmlToBuild() && customFileToLaunch) {
      throw new RuntimeConfigurationException(HaxeLocalize.haxeRunDoNotSelectCustomFile());
    }
    if (settings.isUseNmmlToBuild() && customExecutable) {
      throw new RuntimeConfigurationException(HaxeLocalize.haxeRunDoNotSelectCustomExecutable());
    }
  }

  public boolean isCustomFileToLaunch() {
    return customFileToLaunch;
  }

  public void setCustomFileToLaunch(boolean customFileToLaunch) {
    this.customFileToLaunch = customFileToLaunch;
  }

  public boolean isCustomExecutable() {
    return customExecutable;
  }

  public void setCustomExecutable(boolean customExecutable) {
    this.customExecutable = customExecutable;
  }

  public String getCustomFileToLaunchPath() {
    return customFileToLaunchPath;
  }

  public void setCustomFileToLaunchPath(String customFileToLaunchPath) {
    this.customFileToLaunchPath = customFileToLaunchPath;
  }

  public String getCustomExecutablePath() {
    return customExecutablePath;
  }

  public void setCustomExecutablePath(String customExecutablePath) {
    this.customExecutablePath = customExecutablePath;
  }

  public void writeExternal(final Element element) throws WriteExternalException
  {
    super.writeExternal(element);
    writeModule(element);
    XmlSerializer.serializeInto(this, element);
  }

  public void readExternal(final Element element) throws InvalidDataException {
    super.readExternal(element);
    readModule(element);
    XmlSerializer.deserializeInto(this, element);
  }
}
