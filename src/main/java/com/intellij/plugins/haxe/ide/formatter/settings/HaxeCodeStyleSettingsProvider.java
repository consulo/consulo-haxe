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
package com.intellij.plugins.haxe.ide.formatter.settings;

import com.intellij.plugins.haxe.HaxeBundle;
import consulo.annotation.component.ExtensionImpl;
import consulo.configurable.Configurable;
import consulo.language.codeStyle.CodeStyleSettings;
import consulo.language.codeStyle.CustomCodeStyleSettings;
import consulo.language.codeStyle.setting.CodeStyleSettingsProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author: Fedor.Korotkov
 */
@ExtensionImpl
public class HaxeCodeStyleSettingsProvider extends CodeStyleSettingsProvider {
  @Override
  public String getConfigurableDisplayName() {
    return HaxeBundle.message("haxe.title");
  }

  @Nonnull
  @Override
  public Configurable createSettingsPage(CodeStyleSettings settings, CodeStyleSettings originalSettings) {
    return new HaxeCodeStyleConfigurable(settings, originalSettings);
  }

  @Nullable
  @Override
  public CustomCodeStyleSettings createCustomSettings(CodeStyleSettings settings) {
    return new HaxeCodeStyleSettings(settings);
  }
}
