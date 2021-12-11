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
package com.intellij.plugins.haxe.nmml;

import com.intellij.ide.highlighter.XmlLikeFileType;
import com.intellij.lang.xml.XMLLanguage;
import consulo.haxe.localize.HaxeLocalize;
import consulo.localize.LocalizeValue;
import consulo.ui.image.Image;

import javax.annotation.Nonnull;

/**
 * @author: Fedor.Korotkov
 */
public class NMMLFileType extends XmlLikeFileType {
  public static final NMMLFileType INSTANCE = new NMMLFileType();
  public static final String DEFAULT_EXTENSION = "nmml";

  public NMMLFileType() {
    super(XMLLanguage.INSTANCE);
  }

  @Nonnull
  @Override
  public String getId() {
    return "NMML";
  }

  @Nonnull
  @Override
  public LocalizeValue getDescription() {
    return HaxeLocalize.nmeNmmlDescription();
  }

  @Nonnull
  @Override
  public String getDefaultExtension() {
    return DEFAULT_EXTENSION;
  }

  @Override
  public Image getIcon() {
    return icons.HaxeIcons.Nmml_16;
  }
}
