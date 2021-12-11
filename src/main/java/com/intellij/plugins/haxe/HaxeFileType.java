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

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.openapi.vfs.VirtualFile;
import consulo.haxe.localize.HaxeLocalize;
import consulo.localize.LocalizeValue;
import consulo.ui.image.Image;
import org.jetbrains.annotations.NonNls;

import javax.annotation.Nonnull;

public class HaxeFileType extends LanguageFileType {
  public static final HaxeFileType HAXE_FILE_TYPE = new HaxeFileType();

  @NonNls
  public static final String DEFAULT_EXTENSION = "hx";

  private HaxeFileType() {
    super(HaxeLanguage.INSTANCE);
  }

  @Override
  @Nonnull
  public String getId() {
    return "HAXE";
  }

  @Override
  @NonNls
  @Nonnull
  public LocalizeValue getDescription() {
    return HaxeLocalize.haxeFileTypeDescription();
  }

  @Override
  @Nonnull
  @NonNls
  public String getDefaultExtension() {
    return DEFAULT_EXTENSION;
  }

  @Override
  public Image getIcon() {
    return icons.HaxeIcons.Haxe;
  }

  @Override
  public String getCharset(@Nonnull VirtualFile file, byte[] content) {
    return CharsetToolkit.UTF8;
  }
}
