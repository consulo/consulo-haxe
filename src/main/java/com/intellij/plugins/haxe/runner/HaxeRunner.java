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
import com.intellij.plugins.haxe.ide.module.HaxeModuleSettings;
import consulo.annotation.component.ExtensionImpl;
import consulo.compiler.ModuleCompilerPathsManager;
import consulo.document.FileDocumentManager;
import consulo.execution.ExecutionResult;
import consulo.execution.configuration.CommandLineState;
import consulo.execution.configuration.RunProfile;
import consulo.execution.configuration.RunProfileState;
import consulo.execution.executor.DefaultRunExecutor;
import consulo.execution.executor.Executor;
import consulo.execution.runner.DefaultProgramRunner;
import consulo.execution.runner.ExecutionEnvironment;
import consulo.execution.runner.ProgramRunner;
import consulo.execution.ui.RunContentDescriptor;
import consulo.execution.ui.console.TextConsoleBuilder;
import consulo.execution.ui.console.TextConsoleBuilderFactory;
import consulo.haxe.localize.HaxeLocalize;
import consulo.language.content.ProductionContentFolderTypeProvider;
import consulo.module.Module;
import consulo.platform.Platform;
import consulo.process.ExecutionException;
import consulo.process.ProcessHandler;
import consulo.process.cmd.GeneralCommandLine;
import consulo.process.local.ProcessHandlerFactory;
import consulo.util.io.FileUtil;
import jakarta.annotation.Nonnull;

/**
 * @author: Fedor.Korotkov
 */
@ExtensionImpl
public class HaxeRunner extends DefaultProgramRunner {
  public static final String HAXE_RUNNER_ID = "HaxeRunner";

  public static final RunProfileState EMPTY_RUN_STATE = new RunProfileState() {
    public ExecutionResult execute(final Executor executor, @Nonnull final ProgramRunner runner) throws ExecutionException {
      return null;
    }
  };

  @Nonnull
  @Override
  public String getRunnerId() {
    return HAXE_RUNNER_ID;
  }

  @Override
  public boolean canRun(@Nonnull String executorId, @Nonnull RunProfile profile) {
    return DefaultRunExecutor.EXECUTOR_ID.equals(executorId) && profile instanceof HaxeApplicationConfiguration;
  }

  @Override
  protected RunContentDescriptor doExecute(RunProfileState state,
                                           ExecutionEnvironment env) throws ExecutionException {
    final HaxeApplicationConfiguration configuration = (HaxeApplicationConfiguration) env.getRunProfile();
    final Module module = configuration.getConfigurationModule().getModule();

    if (module == null) {
      throw new ExecutionException(HaxeLocalize.noModuleForRunConfiguration(configuration.getName()).get());
    }

    final HaxeModuleSettings settings = HaxeModuleSettings.getInstance(module);

    if (settings.isUseNmmlToBuild()) {
      final NMERunningState nmeRunningState = new NMERunningState(env, module, false);
      return super.doExecute(nmeRunningState, env);
    }

    if (configuration.isCustomFileToLaunch() && FileUtil.extensionEquals(configuration.getCustomFileToLaunchPath(), "n")) {
      final NekoRunningState nekoRunningState = new NekoRunningState(env, module, configuration.getCustomFileToLaunchPath());
      return super.doExecute(nekoRunningState, env);
    }

    if (configuration.isCustomExecutable()) {
      final String filePath = configuration.isCustomFileToLaunch()
          ? configuration.getCustomFileToLaunchPath()
          : getOutputFilePath(module, settings);
      return super.doExecute(new CommandLineState(env) {
        @Nonnull
        @Override
        protected ProcessHandler startProcess() throws ExecutionException {
          final GeneralCommandLine commandLine = new GeneralCommandLine();
          commandLine.setWorkDirectory(module.getModuleDirPath());
          commandLine.setExePath(configuration.getCustomExecutablePath());
          commandLine.addParameter(filePath);

          final TextConsoleBuilder consoleBuilder = TextConsoleBuilderFactory.getInstance().createBuilder(module.getProject());
          setConsoleBuilder(consoleBuilder);

          return ProcessHandlerFactory.getInstance().createProcessHandler(commandLine);
        }
      }, env);
    }

    if (configuration.isCustomFileToLaunch()) {
      Platform.current().openInBrowser(configuration.getCustomFileToLaunchPath());
      return null;
    }

    if (settings.getHaxeTarget() == HaxeTarget.FLASH) {
      Platform.current().openInBrowser(getOutputFilePath(module, settings));
      return null;
    }

    if (settings.getHaxeTarget() != HaxeTarget.NEKO) {
      throw new ExecutionException(HaxeLocalize.haxeRunWrongTarget(settings.getHaxeTarget()).get());
    }

    final NekoRunningState nekoRunningState = new NekoRunningState(env, module, null);
    return super.doExecute(nekoRunningState, env);
  }

  private static String getOutputFilePath(Module module, HaxeModuleSettings settings) {
    FileDocumentManager.getInstance().saveAllDocuments();
    final String url = ModuleCompilerPathsManager.getInstance(module).getCompilerOutputUrl(ProductionContentFolderTypeProvider.getInstance());
    assert url != null;
    return url + "/release/" + settings.getOutputFileName();
  }
}
