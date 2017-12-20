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
package com.intellij.plugins.haxe;

import javax.swing.Icon;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.openapi.vfs.VirtualFile;

public class HaxeFileType extends LanguageFileType {
  public static final HaxeFileType HAXE_FILE_TYPE = new HaxeFileType();

  @NonNls
  public static final String DEFAULT_EXTENSION = "hx";

  private HaxeFileType() {
    super(HaxeLanguage.INSTANCE);
  }

  @Override
  @NotNull
  @NonNls
  public String getName() {
    return "HAXE";
  }

  @Override
  @NonNls
  @NotNull
  public String getDescription() {
    return HaxeBundle.message("haxe.file.type.description");
  }

  @Override
  @NotNull
  @NonNls
  public String getDefaultExtension() {
    return DEFAULT_EXTENSION;
  }

  @Override
  public Icon getIcon() {
    return icons.HaxeIcons.Haxe;
  }

  @Override
  public String getCharset(@NotNull VirtualFile file, byte[] content) {
    return CharsetToolkit.UTF8;
  }
}
