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
package com.intellij.plugins.haxe.ide.index;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.intellij.plugins.haxe.HaxeComponentType;
import consulo.ui.image.Image;

/**
 * @author: Fedor.Korotkov
 */
public class HaxeClassInfo {
  @Nonnull
  private final String value;
  @Nullable private final HaxeComponentType type;

  public HaxeClassInfo(@Nonnull String name, @Nullable HaxeComponentType type) {
    value = name;
    this.type = type;
  }

  @Nonnull
  public String getValue() {
    return value;
  }

  @Nullable
  public HaxeComponentType getType() {
    return type;
  }

  @Nullable
  public Image getIcon() {
    return type == null ? null : type.getIcon();
  }
}
