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

import consulo.haxe.module.extension.HaxeModuleExtension;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.filters.TextConsoleBuilder;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.plugins.haxe.HaxeBundle;
import com.intellij.plugins.haxe.config.HaxeTarget;
import com.intellij.plugins.haxe.config.sdk.HaxeSdkData;
import com.intellij.plugins.haxe.ide.module.HaxeModuleSettings;
import consulo.compiler.roots.CompilerPathsImpl;

public class NekoRunningState extends CommandLineState {
  private final Module module;
  @Nullable
  private final String customFileToLaunch;

  public NekoRunningState(ExecutionEnvironment env, Module module, @Nullable String fileToLaunch) {
    super(env);
    this.module = module;
    customFileToLaunch = fileToLaunch;
  }

  @Nonnull
  @Override
  protected ProcessHandler startProcess() throws ExecutionException {
    final HaxeModuleSettings settings = HaxeModuleSettings.getInstance(module);
    final Sdk sdk = ModuleUtilCore.getSdk(module, HaxeModuleExtension.class);
    assert sdk != null;
    assert settings.getHaxeTarget() == HaxeTarget.NEKO;
    final HaxeSdkData sdkData = sdk.getSdkAdditionalData() instanceof HaxeSdkData ? (HaxeSdkData)sdk.getSdkAdditionalData() : null;

    GeneralCommandLine commandLine = getCommandForNeko(sdkData, settings);

    return new OSProcessHandler(commandLine.createProcess(), commandLine.getCommandLineString());
  }

  private GeneralCommandLine getCommandForNeko(@Nullable HaxeSdkData sdkData, HaxeModuleSettings settings) throws ExecutionException {
    if (sdkData == null || sdkData.getNekoBinPath() == null || sdkData.getNekoBinPath().isEmpty()) {
      throw new ExecutionException(HaxeBundle.message("haxe.run.bad.neko.bin.path"));
    }

    GeneralCommandLine commandLine = new GeneralCommandLine();

    commandLine.setExePath(sdkData.getNekoBinPath());
    commandLine.setWorkDirectory(module.getModuleDirPath());

    if (customFileToLaunch != null) {
      commandLine.addParameter(customFileToLaunch);
    }
    else {
      final VirtualFile outputDirectory = CompilerPathsImpl.getModuleOutputDirectory(module, false);
      final VirtualFile fileToLaunch = outputDirectory != null ? outputDirectory.findChild(settings.getOutputFileName()) : null;
      if (fileToLaunch != null) {
        commandLine.addParameter(fileToLaunch.getPath());
      }
    }

    final TextConsoleBuilder consoleBuilder = TextConsoleBuilderFactory.getInstance().createBuilder(module.getProject());
    setConsoleBuilder(consoleBuilder);
    return commandLine;
  }
}
