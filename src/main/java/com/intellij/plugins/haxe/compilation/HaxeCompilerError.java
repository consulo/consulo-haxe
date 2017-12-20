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
package com.intellij.plugins.haxe.compilation;

import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * @author: Fedor.Korotkov
 */
public class HaxeCompilerError {
  private final String errorMessage;
  private final String path;
  private final int line;

  public HaxeCompilerError(String errorMessage, String path, int line) {
    this.errorMessage = errorMessage;
    this.path = path;
    this.line = line;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public String getPath() {
    return path;
  }

  public int getLine() {
    return line;
  }

  @Nullable
  public static HaxeCompilerError create(@NotNull String rootPath, final String message) {
    return create(rootPath, message, true);
  }

  @Nullable
  public static HaxeCompilerError create(@NotNull String rootPath, final String message, boolean checkExistence) {
    final int index = message.indexOf(' ');
    if (index < 1) {
      return null;
    }
    String error = message.substring(0, index - 1);
    final int semicolonIndex = error.lastIndexOf(':');
    int line = -1;
    try {
      line = semicolonIndex == -1 ? -1 : Integer.parseInt(error.substring(semicolonIndex + 1));
    }
    catch (NumberFormatException ignored) {
    }

    final String path = semicolonIndex == -1 ? error : error.substring(0, semicolonIndex);

    final int semicolonIndex2 = message.indexOf(':', index);
    String errorMessage = null;
    if (semicolonIndex2 != -1) {
      errorMessage = message.substring(semicolonIndex2 + 1);
    }

    String filePath = FileUtil.toSystemIndependentName(path);
    if (!path.startsWith(rootPath)) {
      filePath = rootPath + "/" + filePath;
    }

    if (checkExistence && !(new File(FileUtil.toSystemDependentName(filePath)).exists())) {
      return null;
    }

    return new HaxeCompilerError(StringUtil.trimLeading(StringUtil.trimTrailing(StringUtil.notNullize(errorMessage))), filePath, line);
  }
}
