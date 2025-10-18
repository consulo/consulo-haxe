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
package com.intellij.plugins.haxe.config.ui;

import consulo.haxe.localize.HaxeLocalize;
import consulo.ui.ex.awt.DialogWrapper;
import jakarta.annotation.Nonnull;

import javax.swing.*;
import java.awt.*;

/**
 * @author: Fedor.Korotkov
 */
public class StringValueDialog extends DialogWrapper
{
  private JTextField myTextField;
  private JPanel myMainPanel;

  public StringValueDialog(@Nonnull Component parent, boolean canBeParent) {
    super(parent, canBeParent);

    setTitle(HaxeLocalize.haxeConditionalCompilationTitle());

    init();
  }

  @Override
  protected JComponent createCenterPanel() {
    return myMainPanel;
  }

  public String getStringValue() {
    return myTextField.getText();
  }

  @Override
  public JComponent getPreferredFocusedComponent() {
    return myTextField;
  }
}
