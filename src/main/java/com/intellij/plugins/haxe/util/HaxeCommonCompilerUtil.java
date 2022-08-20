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
package com.intellij.plugins.haxe.util;

import com.intellij.plugins.haxe.HaxeCommonBundle;
import com.intellij.plugins.haxe.config.HaxeTarget;
import com.intellij.plugins.haxe.config.NMETarget;
import com.intellij.plugins.haxe.module.HaxeModuleSettingsBase;
import consulo.process.ExecutionException;
import consulo.process.ProcessHandler;
import consulo.process.cmd.GeneralCommandLine;
import consulo.process.event.ProcessAdapter;
import consulo.process.event.ProcessEvent;
import consulo.process.local.ProcessHandlerFactory;
import consulo.util.dataholder.Key;
import consulo.util.io.FileUtil;
import consulo.util.lang.ref.Ref;
import consulo.util.lang.text.StringTokenizer;

import javax.annotation.Nonnull;
import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @author: Fedor.Korotkov
 */
public class HaxeCommonCompilerUtil {
  public interface CompilationContext {
    @Nonnull
    HaxeModuleSettingsBase getModuleSettings();

    String getModuleName();

    void errorHandler(String message);

    void log(String message);

    String getSdkHomePath();

    public String getHaxelibPath();

    boolean isDebug();

    String getSdkName();

    List<String> getSourceRoots();

    String getCompileOutputPath();

    void handleOutput(String[] lines);
  }

  public static boolean compile(final CompilationContext context) {
    HaxeModuleSettingsBase settings = context.getModuleSettings();
    if (settings.isExcludeFromCompilation()) {
      context.log("Module " + context.getModuleName() + " is excluded from compilation.");
      return true;
    }
    final String mainClass = settings.getMainClass();
    final String fileName = settings.getOutputFileName();

    if (settings.isUseUserPropertiesToBuild()) {
      if (mainClass == null || mainClass.length() == 0) {
        context.errorHandler(HaxeCommonBundle.message("no.main.class.for.module", context.getModuleName()));
        return false;
      }
      if (fileName == null || fileName.length() == 0) {
        context.errorHandler(HaxeCommonBundle.message("no.output.file.name.for.module", context.getModuleName()));
        return false;
      }
    }

    final HaxeTarget target = settings.getHaxeTarget();
    final NMETarget nmeTarget = settings.getNmeTarget();
    if (target == null && !settings.isUseNmmlToBuild()) {
      context.errorHandler(HaxeCommonBundle.message("no.target.for.module", context.getModuleName()));
      return false;
    }
    if (nmeTarget == null && settings.isUseNmmlToBuild()) {
      context.errorHandler(HaxeCommonBundle.message("no.target.for.module", context.getModuleName()));
      return false;
    }

    if (context.getSdkHomePath() == null) {
      context.errorHandler(HaxeCommonBundle.message("no.sdk.for.module", context.getModuleName()));
      return false;
    }

    final String sdkExePath = HaxeSdkUtilBase.getCompilerPathByFolderPath(context.getSdkHomePath());

    if (sdkExePath == null || sdkExePath.isEmpty()) {
      context.errorHandler(HaxeCommonBundle.message("invalid.haxe.sdk.for.module", context.getModuleName()));
      return false;
    }

    final String haxelibPath = context.getHaxelibPath();
    if (settings.isUseNmmlToBuild() && (haxelibPath == null || haxelibPath.isEmpty())) {
      context.errorHandler(HaxeCommonBundle.message("no.haxelib.for.sdk", context.getSdkName()));
      return false;
    }

    final GeneralCommandLine commandLine = new GeneralCommandLine();
    commandLine.setCharset(Charset.defaultCharset());

    if (settings.isUseNmmlToBuild()) {
      commandLine.setExePath(haxelibPath);
    } else {
      commandLine.setExePath(sdkExePath);
    }

    String workingPath = context.getCompileOutputPath() + "/" + (context.isDebug() ? "debug" : "release");
    if (settings.isUseNmmlToBuild()) {
      setupNME(commandLine, context);
    } else if (settings.isUseHxmlToBuild()) {
      String hxmlPath = settings.getHxmlPath();
      commandLine.addParameter(FileUtil.toSystemDependentName(hxmlPath));
      final int endIndex = hxmlPath.lastIndexOf('/');
      if (endIndex > 0) {
        workingPath = hxmlPath.substring(0, endIndex);
      }
      if (context.isDebug() && settings.getHaxeTarget() == HaxeTarget.FLASH) {
        commandLine.addParameter("-D");
        commandLine.addParameter("fdb");
        commandLine.addParameter("-debug");
      }
    } else {
      setupUserProperties(commandLine, context);
    }

    final Ref<Boolean> hasErrors = consulo.util.lang.ref.Ref.create(Boolean.FALSE);

    try {
      final File workingDirectory = new File(FileUtil.toSystemDependentName(workingPath));
      if (!workingDirectory.exists()) {
        workingDirectory.mkdir();
      }

      commandLine.setWorkDirectory(workingDirectory);

      ProcessHandler handler = ProcessHandlerFactory.getInstance().createProcessHandler(commandLine);

      handler.addProcessListener(new ProcessAdapter() {
        @Override
        public void onTextAvailable(ProcessEvent event, Key outputType) {
          context.handleOutput(event.getText().split("\\n"));
        }

        @Override
        public void processTerminated(ProcessEvent event) {
          hasErrors.set(event.getExitCode() != 0);
          super.processTerminated(event);
        }
      });

      handler.startNotify();
      handler.waitFor();
    } catch (ExecutionException e) {
      context.errorHandler("process throw exception: " + e.getMessage());
      return false;
    }

    return !hasErrors.get();
  }

  private static void setupUserProperties(GeneralCommandLine commandLine, CompilationContext context) {
    final HaxeModuleSettingsBase settings = context.getModuleSettings();
    commandLine.addParameter("-main");
    commandLine.addParameter(settings.getMainClass());

    final consulo.util.lang.text.StringTokenizer argumentsTokenizer = new StringTokenizer(settings.getArguments());
    while (argumentsTokenizer.hasMoreTokens()) {
      commandLine.addParameter(argumentsTokenizer.nextToken());
    }

    if (context.isDebug()) {
      commandLine.addParameter("-debug");
    }
    if (settings.getHaxeTarget() == HaxeTarget.FLASH && context.isDebug()) {
      commandLine.addParameter("-D");
      commandLine.addParameter("fdb");
    }

    for (String sourceRoot : context.getSourceRoots()) {
      commandLine.addParameter("-cp");
      commandLine.addParameter(sourceRoot);
    }

    commandLine.addParameter(settings.getHaxeTarget().getCompilerFlag());
    commandLine.addParameter(settings.getOutputFileName());
  }

  private static void setupNME(GeneralCommandLine commandLine, CompilationContext context) {
    final HaxeModuleSettingsBase settings = context.getModuleSettings();
    commandLine.addParameter("run");
    commandLine.addParameter("nme");
    commandLine.addParameter("build");
    commandLine.addParameter(settings.getNmmlPath());
    commandLine.addParameter(settings.getNmeTarget().getTargetFlag());
    if (context.isDebug()) {
      commandLine.addParameter("-debug");
      commandLine.addParameter("-Ddebug");
    }
    if (settings.getNmeTarget() == NMETarget.FLASH && context.isDebug()) {
      commandLine.addParameter("-Dfdb");
    }
    final consulo.util.lang.text.StringTokenizer flagsTokenizer = new StringTokenizer(settings.getNmeFlags());
    while (flagsTokenizer.hasMoreTokens()) {
      commandLine.addParameter(flagsTokenizer.nextToken());
    }
  }
}
