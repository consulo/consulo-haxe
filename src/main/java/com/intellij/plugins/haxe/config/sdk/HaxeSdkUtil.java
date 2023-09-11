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
package com.intellij.plugins.haxe.config.sdk;

import com.intellij.plugins.haxe.util.HaxeSdkUtilBase;
import consulo.application.util.SystemInfo;
import consulo.logging.Logger;
import consulo.process.ExecutionException;
import consulo.process.cmd.GeneralCommandLine;
import consulo.process.util.CapturingProcessUtil;
import consulo.process.util.ProcessOutput;
import consulo.util.io.FileUtil;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.VirtualFileManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HaxeSdkUtil extends HaxeSdkUtilBase {
  private static final Logger LOG = Logger.getInstance(HaxeSdkUtil.class);
  private static final Pattern VERSION_MATCHER = Pattern.compile("(\\d+(\\.\\d+)+)");
  private static final String COMPILER_EXECUTABLE_NAME = "haxe";
  private static final String HAXELIB_EXECUTABLE_NAME = "haxelib";

  @Nullable
  public static HaxeSdkData testHaxeSdk(String path) {
    final String exePath = getCompilerPathByFolderPath(path);

    if (exePath == null) {
      return null;
    }

    final GeneralCommandLine command = new GeneralCommandLine();
    command.setExePath(exePath);
    command.addParameter("-help");
    command.setWorkDirectory(path);
    command.withCharset(StandardCharsets.UTF_8);

    try {
      final ProcessOutput output = CapturingProcessUtil.execAndGetOutput(command);

      if (output.getExitCode() != 0) {
        LOG.error("haXe compiler exited with invalid exit code: " + output.getExitCode());
        return null;
      }

      final String outputString = output.getStdout();

      String haxeVersion = "NA";
      final Matcher matcher = VERSION_MATCHER.matcher(outputString);
      if (matcher.find()) {
        haxeVersion = matcher.group(1);
      }
      final HaxeSdkData haxeSdkData = new HaxeSdkData(path, haxeVersion);
      haxeSdkData.setHaxelibPath(getHaxelibPathByFolderPath(path));
      haxeSdkData.setNekoBinPath(suggestNekoBinPath(path));
      return haxeSdkData;
    }
    catch (ExecutionException e) {
      LOG.info("Exception while executing the process:", e);
      return null;
    }
  }

  @Nullable
  private static String suggestNekoBinPath(@Nonnull String path) {
    String result = System.getenv("NEKOPATH");
    if (result == null) {
      result = System.getenv("NEKO_INSTPATH");
    }
    if (result == null && !SystemInfo.isWindows) {
      final VirtualFile candidate = VirtualFileManager.getInstance().findFileByUrl("/usr/bin/neko");
      if (candidate != null && candidate.exists()) {
        return FileUtil.toSystemIndependentName(candidate.getPath());
      }
    }
    if (result == null) {
      final String parentPath = new File(path).getParent();
      result = new File(parentPath, "neko").getAbsolutePath();
    }
    if (result != null) {
      result = new File(result, getExecutableName("neko")).getAbsolutePath();
    }
    if (result != null && new File(result).exists()) {
      return FileUtil.toSystemIndependentName(result);
    }
    return null;
  }
}
