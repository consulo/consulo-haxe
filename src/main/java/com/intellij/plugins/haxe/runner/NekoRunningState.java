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

import com.intellij.plugins.haxe.config.HaxeTarget;
import com.intellij.plugins.haxe.config.sdk.HaxeSdkData;
import com.intellij.plugins.haxe.ide.module.HaxeModuleSettings;
import consulo.compiler.ModuleCompilerPathsManager;
import consulo.content.bundle.Sdk;
import consulo.execution.configuration.CommandLineState;
import consulo.execution.runner.ExecutionEnvironment;
import consulo.execution.ui.console.TextConsoleBuilder;
import consulo.execution.ui.console.TextConsoleBuilderFactory;
import consulo.haxe.localize.HaxeLocalize;
import consulo.haxe.module.extension.HaxeModuleExtension;
import consulo.language.content.ProductionContentFolderTypeProvider;
import consulo.language.util.ModuleUtilCore;
import consulo.module.Module;
import consulo.process.ExecutionException;
import consulo.process.ProcessHandler;
import consulo.process.cmd.GeneralCommandLine;
import consulo.process.local.ProcessHandlerFactory;
import org.jspecify.annotations.Nullable;

import java.nio.file.Files;
import java.nio.file.Path;

public class NekoRunningState extends CommandLineState {
    private final Module myModule;
    private final @Nullable String myCustomFileToLaunch;

    public NekoRunningState(ExecutionEnvironment env, Module module, @Nullable String fileToLaunch) {
        super(env);
        myModule = module;
        myCustomFileToLaunch = fileToLaunch;
    }

    @Override
    protected ProcessHandler startProcess() throws ExecutionException {
        HaxeModuleSettings settings = HaxeModuleSettings.getInstance(myModule);
        Sdk sdk = ModuleUtilCore.getSdk(myModule, HaxeModuleExtension.class);
        assert sdk != null;
        assert settings.getHaxeTarget() == HaxeTarget.NEKO;
        HaxeSdkData sdkData = sdk.getSdkAdditionalData() instanceof HaxeSdkData ? (HaxeSdkData) sdk.getSdkAdditionalData() : null;

        GeneralCommandLine commandLine = getCommandForNeko(sdkData, settings);

        return ProcessHandlerFactory.getInstance().createProcessHandler(commandLine);
    }

    private GeneralCommandLine getCommandForNeko(@Nullable HaxeSdkData sdkData, HaxeModuleSettings settings) throws ExecutionException {
        if (sdkData == null || sdkData.getNekoBinPath() == null || sdkData.getNekoBinPath().isEmpty()) {
            throw new ExecutionException(HaxeLocalize.haxeRunBadNekoBinPath().get());
        }

        GeneralCommandLine commandLine = new GeneralCommandLine();

        commandLine.setExePath(sdkData.getNekoBinPath());
        commandLine.setWorkDirectory(myModule.getModuleDirPath());

        if (myCustomFileToLaunch != null) {
            commandLine.addParameter(myCustomFileToLaunch);
        }
        else {
            Path outputDirectory = ModuleCompilerPathsManager.getInstance(myModule)
                .getCompilerOutputPath(ProductionContentFolderTypeProvider.getInstance());
            Path fileToLaunch = outputDirectory == null ? null : outputDirectory.resolve(settings.getOutputFileName());
            if (fileToLaunch != null && Files.exists(fileToLaunch)) {
                commandLine.addParameter(fileToLaunch.toString());
            }
        }

        TextConsoleBuilder consoleBuilder = TextConsoleBuilderFactory.getInstance().createBuilder(myModule.getProject());
        setConsoleBuilder(consoleBuilder);
        return commandLine;
    }
}
