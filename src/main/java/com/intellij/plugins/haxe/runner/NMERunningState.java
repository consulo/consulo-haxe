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

import javax.annotation.Nonnull;

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
import com.intellij.plugins.haxe.HaxeCommonBundle;
import com.intellij.plugins.haxe.config.sdk.HaxeSdkData;
import com.intellij.plugins.haxe.ide.module.HaxeModuleSettings;
import com.intellij.util.text.StringTokenizer;
import consulo.haxe.module.extension.HaxeModuleExtension;

/**
 * @author: Fedor.Korotkov
 */
public class NMERunningState extends CommandLineState {
  private final Module module;
  private final boolean myRunInTest;
  private final boolean myDebug;

  public NMERunningState(ExecutionEnvironment env, Module module, boolean runInTest) {
    this(env, module, runInTest, false);
  }

  public NMERunningState(ExecutionEnvironment env, Module module, boolean runInTest, boolean debug) {
    super(env);
    this.module = module;
    myRunInTest = runInTest;
    myDebug = debug;
  }

  @Nonnull
  @Override
  protected ProcessHandler startProcess() throws ExecutionException {
    final HaxeModuleSettings settings = HaxeModuleSettings.getInstance(module);
    final Sdk sdk = ModuleUtilCore.getSdk(module, HaxeModuleExtension.class);
    assert sdk != null;

    GeneralCommandLine commandLine = getCommandForNeko(sdk, settings);

    return new OSProcessHandler(commandLine.createProcess(), commandLine.getCommandLineString());
  }

  private GeneralCommandLine getCommandForNeko(Sdk sdk, HaxeModuleSettings settings) throws ExecutionException {
    final HaxeSdkData sdkData = sdk.getSdkAdditionalData() instanceof HaxeSdkData ? (HaxeSdkData)sdk.getSdkAdditionalData() : null;
    if (sdkData == null) {
      throw new ExecutionException(HaxeCommonBundle.message("invalid.haxe.sdk"));
    }
    final GeneralCommandLine commandLine = new GeneralCommandLine();

    commandLine.setWorkDirectory(module.getModuleDirPath());
    final String haxelibPath = sdkData.getHaxelibPath();
    if (haxelibPath == null || haxelibPath.isEmpty()) {
      throw new ExecutionException(HaxeCommonBundle.message("no.haxelib.for.sdk", sdk.getName()));
    }
    commandLine.setExePath(haxelibPath);
    commandLine.addParameter("run");
    commandLine.addParameter("nme");
    commandLine.addParameter(myRunInTest ? "test" : "run");
    commandLine.addParameter(settings.getNmmlPath());
    for (String flag : settings.getNmeTarget().getFlags()) {
      commandLine.addParameter(flag);
    }
    if (myDebug) {
      commandLine.addParameter("-debug");
      commandLine.addParameter("-Ddebug");
    }

    final StringTokenizer flagsTokenizer = new StringTokenizer(settings.getNmeFlags());
    while (flagsTokenizer.hasMoreTokens()) {
      commandLine.addParameter(flagsTokenizer.nextToken());
    }

    final TextConsoleBuilder consoleBuilder = TextConsoleBuilderFactory.getInstance().createBuilder(module.getProject());
    setConsoleBuilder(consoleBuilder);
    return commandLine;
  }
}