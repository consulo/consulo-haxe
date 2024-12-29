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
package com.intellij.plugins.haxe.config;

import consulo.annotation.component.ExtensionImpl;
import consulo.configurable.ConfigurationException;
import consulo.configurable.NonDefaultProjectConfigurable;
import consulo.configurable.ProjectConfigurable;
import consulo.configurable.SearchableConfigurable;
import com.intellij.plugins.haxe.HaxeBundle;
import com.intellij.plugins.haxe.config.ui.HaxeSettingsForm;
import com.intellij.plugins.haxe.util.HaxeUtil;
import consulo.disposer.Disposable;
import consulo.project.Project;
import consulo.ui.annotation.RequiredUIAccess;
import jakarta.inject.Inject;

import jakarta.annotation.Nonnull;

import javax.swing.*;

/**
 * @author: Fedor.Korotkov
 */
@ExtensionImpl
public class HaxeSettingsConfigurable implements SearchableConfigurable, NonDefaultProjectConfigurable, ProjectConfigurable {
  private HaxeSettingsForm mySettingsPane;
  private final Project myProject;

  @Inject
  public HaxeSettingsConfigurable(Project project) {
    myProject = project;
  }

  public String getDisplayName() {
    return HaxeBundle.message("haxe.settings.name");
  }

  @Nonnull
  public String getId() {
    return "haxe.settings";
  }

  public String getHelpTopic() {
    return null;
  }

  @RequiredUIAccess
  public JComponent createComponent(@Nonnull Disposable uiDisposable) {
    if (mySettingsPane == null) {
      mySettingsPane = new HaxeSettingsForm();
    }
    reset();
    return mySettingsPane.getPanel();
  }

  @RequiredUIAccess
  public boolean isModified() {
    return mySettingsPane != null && mySettingsPane.isModified(getSettings());
  }

  @RequiredUIAccess
  public void apply() throws ConfigurationException {
    if (mySettingsPane != null) {
      final boolean modified = isModified();
      mySettingsPane.applyEditorTo(getSettings());
      if (modified) {
        HaxeUtil.reparseProjectFiles(myProject);
      }
    }
  }

  @RequiredUIAccess
  public void reset() {
    if (mySettingsPane != null) {
      mySettingsPane.resetEditorFrom(getSettings());
    }
  }

  private HaxeProjectSettings getSettings() {
    return HaxeProjectSettings.getInstance(myProject);
  }

  @RequiredUIAccess
  public void disposeUIResources() {
    mySettingsPane = null;
  }

  public Runnable enableSearch(String option) {
    return null;
  }
}
