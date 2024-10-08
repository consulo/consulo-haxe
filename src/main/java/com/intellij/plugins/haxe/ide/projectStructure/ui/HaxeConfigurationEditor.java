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
package com.intellij.plugins.haxe.ide.projectStructure.ui;

import com.intellij.plugins.haxe.HaxeBundle;
import com.intellij.plugins.haxe.HaxeFileType;
import com.intellij.plugins.haxe.config.HaxeProjectSettings;
import com.intellij.plugins.haxe.config.HaxeSettingsConfigurable;
import com.intellij.plugins.haxe.config.HaxeTarget;
import com.intellij.plugins.haxe.config.NMETarget;
import com.intellij.plugins.haxe.ide.module.HaxeModuleSettings;
import com.intellij.uiDesigner.core.GridConstraints;
import consulo.compiler.ModuleCompilerPathsManager;
import consulo.configurable.ConfigurationException;
import consulo.configurable.UnnamedConfigurable;
import consulo.execution.ui.awt.RawCommandLineEditor;
import consulo.fileChooser.FileChooserDescriptor;
import consulo.fileChooser.FileChooserDescriptorFactory;
import consulo.fileChooser.IdeaFileChooser;
import consulo.ide.setting.ShowSettingsUtil;
import consulo.language.content.ProductionContentFolderTypeProvider;
import consulo.language.editor.ui.TreeFileChooser;
import consulo.language.editor.ui.TreeFileChooserFactory;
import consulo.language.psi.PsiFile;
import consulo.module.Module;
import consulo.module.content.DirectoryIndex;
import consulo.project.Project;
import consulo.ui.ex.awt.JBRadioButton;
import consulo.ui.ex.awt.TextFieldWithBrowseButton;
import consulo.util.io.FileUtil;
import consulo.util.lang.StringUtil;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.util.VirtualFileUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Fedor.Korotkov
 */
public class HaxeConfigurationEditor {
  private JPanel myMainPanel;
  private TextFieldWithBrowseButton myMainClassFieldWithButton;
  private RawCommandLineEditor myAppArguments;
  private JComboBox myTargetComboBox;
  private JCheckBox myExcludeFromCompilationCheckBox;
  private JLabel myTargetLabel;
  private JLabel myMainClassLabel;
  private JLabel myParametersLabel;
  private JTextField myOutputFileNameTextField;
  private TextFieldWithBrowseButton myFolderTextField;
  private JLabel myFolderLabel;
  private JPanel myAdditionalComponentPanel;
  private TextFieldWithBrowseButton myHxmlFileChooserTextField;
  private JBRadioButton myHxmlFileRadioButton;
  private JBRadioButton myNmmlFileRadioButton;
  private JBRadioButton myUserPropertiesRadioButton;
  private JPanel myCompilerOptions;
  private JPanel myCommonPanel;
  private JTextField myDefinedMacroses;
  private JButton myEditMacrosesButton;
  private JPanel myHxmlFileChooserPanel;
  private RawCommandLineEditor myNMEArguments;
  private TextFieldWithBrowseButton myNMEFileChooserTextField;
  private JPanel myNMEFilePanel;
  private JPanel myBuildFilePanel;
  private JPanel myCompilerOprionsWrapper;

  private HaxeTarget selectedHaxeTarget = HaxeTarget.NEKO;
  private NMETarget selectedNmeTarget = NMETarget.FLASH;

  private final Module myModule;

  private final List<UnnamedConfigurable> configurables = new ArrayList<UnnamedConfigurable>();

  public HaxeConfigurationEditor(Module module) {
    myModule = module;
    addActionListeners();

    myMainClassLabel.setLabelFor(myMainClassFieldWithButton.getTextField());
    myParametersLabel.setLabelFor(myAppArguments.getTextField());
    myFolderLabel.setLabelFor(myFolderTextField.getTextField());

    ButtonGroup group = new ButtonGroup();
    group.add(myHxmlFileRadioButton);
    group.add(myNmmlFileRadioButton);
    group.add(myUserPropertiesRadioButton);
  }

  private void addActionListeners() {
    myMainClassFieldWithButton.getButton().addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        TreeFileChooser fileChooser = TreeFileChooserFactory.getInstance(myModule.getProject()).createFileChooser(HaxeBundle.message("choose.haxe.main.class"), null,
            HaxeFileType.HAXE_FILE_TYPE, null);

        fileChooser.showDialog();

        PsiFile selectedFile = fileChooser.getSelectedFile();
        if (selectedFile != null) {
          setChosenFile(selectedFile.getVirtualFile());
        }
      }
    });

    myFolderTextField.getButton().addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        final VirtualFile folder = IdeaFileChooser.chooseFile(FileChooserDescriptorFactory.createSingleFolderDescriptor(), myModule.getProject(), null);
        if (folder != null) {
          myFolderTextField.setText(FileUtil.toSystemDependentName(folder.getPath()));
        }
      }
    });

    myTargetComboBox.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (myTargetComboBox.getSelectedItem() instanceof HaxeTarget) {
          selectedHaxeTarget = (HaxeTarget) myTargetComboBox.getSelectedItem();
        }
        if (myTargetComboBox.getSelectedItem() instanceof NMETarget) {
          selectedNmeTarget = (NMETarget) myTargetComboBox.getSelectedItem();
        }
        if (!myOutputFileNameTextField.getText().isEmpty()) {
          myOutputFileNameTextField.setText(getCurrentExtension(FileUtil.getNameWithoutExtension(myOutputFileNameTextField.getText())));
        }
      }
    });

    final ActionListener listener = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        updateComponents();
        updateTargetCombo();
      }
    };
    myHxmlFileRadioButton.addActionListener(listener);
    myNmmlFileRadioButton.addActionListener(listener);
    myUserPropertiesRadioButton.addActionListener(listener);

    ActionListener fileChooserListener = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        final VirtualFile moduleDir = myModule.getModuleDir();
        assert moduleDir != null;
        final boolean isNMML = myNmmlFileRadioButton.isSelected();
        final FileChooserDescriptor descriptor = new FileChooserDescriptor(true, false, false, true, false, false) {
          public boolean isFileVisible(VirtualFile file, boolean showHiddenFiles) {
            return super.isFileVisible(file, showHiddenFiles) && (file.isDirectory() || (isNMML ? "nmml" : "hxml").equalsIgnoreCase(file.getExtension()));
          }
        };
        final VirtualFile file = IdeaFileChooser.chooseFile(descriptor, getMainPanel(), null, moduleDir);
        if (file != null) {
          String path = FileUtil.toSystemIndependentName(file.getPath());
          if (isNMML) {
            myNMEFileChooserTextField.setText(path);
          } else {
            myHxmlFileChooserTextField.setText(path);
          }
          updateComponents();
        }
      }
    };

    myHxmlFileChooserTextField.getButton().addActionListener(fileChooserListener);
    myNMEFileChooserTextField.getButton().addActionListener(fileChooserListener);

    myEditMacrosesButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        final Project project = myModule.getProject();
        ShowSettingsUtil.getInstance().showAndSelect(project, HaxeSettingsConfigurable.class);
        updateMacroses();
      }
    });
  }

  private void updateComponents() {
    updateUserProperties();
    updateFileChooser();
    updateMacroses();
  }

  private void updateMacroses() {
    final String[] userCompilerDefinitions = HaxeProjectSettings.getInstance(myModule.getProject()).getCompilerDefinitions();
    myDefinedMacroses.setText(StringUtil.join(userCompilerDefinitions, ","));
  }

  private void updateFileChooser() {
    boolean containsHxml = false;
    boolean containsNME = false;
    Component[] components = myBuildFilePanel.getComponents();
    for (Component component : components) {
      if (component == myHxmlFileChooserPanel) {
        containsHxml = true;
      }
      if (component == myNMEFilePanel) {
        containsNME = true;
      }
    }
    if (!myHxmlFileRadioButton.isSelected() && containsHxml) {
      myBuildFilePanel.remove(myHxmlFileChooserPanel);
    }
    if (!myNmmlFileRadioButton.isSelected() && containsNME) {
      myBuildFilePanel.remove(myNMEFilePanel);
    }
    final GridConstraints constraints = new GridConstraints();
    constraints.setRow(0);
    constraints.setFill(GridConstraints.FILL_HORIZONTAL);
    if (myHxmlFileRadioButton.isSelected() && !containsHxml) {
      myBuildFilePanel.add(myHxmlFileChooserPanel, constraints);
    }
    if (myNmmlFileRadioButton.isSelected() && !containsNME) {
      myBuildFilePanel.add(myNMEFilePanel, constraints);
    }
  }

  private void updateUserProperties() {
    boolean contains = false;
    Component[] components = myCompilerOprionsWrapper.getComponents();
    for (Component component : components) {
      if (component == myCompilerOptions) {
        contains = true;
        break;
      }
    }
    if (myUserPropertiesRadioButton.isSelected() && !contains) {
      final GridConstraints constraints = new GridConstraints();
      constraints.setRow(0);
      constraints.setFill(GridConstraints.FILL_HORIZONTAL);
      myCompilerOprionsWrapper.add(myCompilerOptions, constraints);
    } else if (!myUserPropertiesRadioButton.isSelected() && contains) {
      myCompilerOprionsWrapper.remove(myCompilerOptions);
    }
  }

  private void updateTargetCombo() {
    ((DefaultComboBoxModel) myTargetComboBox.getModel()).removeAllElements();
    if (myNmmlFileRadioButton.isSelected()) {
      NMETarget.initCombo((DefaultComboBoxModel) myTargetComboBox.getModel());
      myTargetComboBox.setSelectedItem(selectedNmeTarget);
    } else {
      HaxeTarget.initCombo((DefaultComboBoxModel) myTargetComboBox.getModel());
      myTargetComboBox.setSelectedItem(selectedHaxeTarget);
    }
  }

  private void setChosenFile(VirtualFile virtualFile) {
    VirtualFile parent = virtualFile.getParent();
    String qualifier = parent == null ? null : DirectoryIndex.getInstance(myModule.getProject()).getPackageName(parent);
    qualifier = qualifier != null && qualifier.length() != 0 ? qualifier + '.' : "";
    myMainClassFieldWithButton.setText(qualifier + FileUtil.getNameWithoutExtension(virtualFile.getName()));
  }

  private String getCurrentExtension(String fileName) {
    if (selectedHaxeTarget != null) {
      return selectedHaxeTarget.getTargetFileNameWithExtension(fileName);
    }
    return fileName;
  }

  public boolean isModified() {
    final HaxeModuleSettings settings = HaxeModuleSettings.getInstance(myModule);
    assert settings != null;

    ModuleCompilerPathsManager manager = ModuleCompilerPathsManager.getInstance(myModule);
    final String url = manager.getCompilerOutputUrl(ProductionContentFolderTypeProvider.getInstance());
    final String urlCandidate = VirtualFileUtil.pathToUrl(myFolderTextField.getText());
    boolean result = !urlCandidate.equals(url);

    result = result || settings.getNmeTarget() != selectedNmeTarget;
    result = result || !FileUtil.toSystemIndependentName(myNMEFileChooserTextField.getText()).equals(settings.getNmmlPath());

    result = result || !settings.getMainClass().equals(myMainClassFieldWithButton.getText());
    result = result || settings.getHaxeTarget() != selectedHaxeTarget;

    result = result || !FileUtil.toSystemIndependentName(myHxmlFileChooserTextField.getText()).equals(settings.getHxmlPath());
    result = result || !FileUtil.toSystemIndependentName(myNMEFileChooserTextField.getText()).equals(settings.getNmmlPath());
    result = result || !settings.getArguments().equals(myAppArguments.getText());
    result = result || !settings.getNmeFlags().equals(myNMEArguments.getText());
    result = result || (settings.isExcludeFromCompilation() ^ myExcludeFromCompilationCheckBox.isSelected());
    result = result || !settings.getOutputFileName().equals(myOutputFileNameTextField.getText());

    result = result || getCurrentBuildConfig() != settings.getBuildConfig();

    for (UnnamedConfigurable configurable : configurables) {
      result = result || configurable.isModified();
    }

    return result;
  }

  public void reset() {
    final HaxeModuleSettings settings = HaxeModuleSettings.getInstance(myModule);
    assert settings != null;
    myMainClassFieldWithButton.setText(settings.getMainClass());
    myAppArguments.setText(settings.getArguments());
    selectedHaxeTarget = settings.getHaxeTarget();
    selectedNmeTarget = settings.getNmeTarget();
    myExcludeFromCompilationCheckBox.setSelected(settings.isExcludeFromCompilation());
    myOutputFileNameTextField.setText(settings.getOutputFileName());
    for (UnnamedConfigurable configurable : configurables) {
      configurable.reset();
    }

    ModuleCompilerPathsManager manager = ModuleCompilerPathsManager.getInstance(myModule);
    final String url = manager.getCompilerOutputUrl(ProductionContentFolderTypeProvider.getInstance());
    myFolderTextField.setText(VirtualFileUtil.urlToPath(url));
    myHxmlFileChooserTextField.setText(settings.getHxmlPath());
    myNMEFileChooserTextField.setText(settings.getNmmlPath());
    myNMEArguments.setText(settings.getNmeFlags());

    myHxmlFileRadioButton.setSelected(settings.isUseHxmlToBuild());
    myNmmlFileRadioButton.setSelected(settings.isUseNmmlToBuild());
    myUserPropertiesRadioButton.setSelected(settings.isUseUserPropertiesToBuild());
    updateComponents();
    updateTargetCombo();
  }

  public void apply() {
    final HaxeModuleSettings settings = HaxeModuleSettings.getInstance(myModule);
    assert settings != null;
    settings.setMainClass(myMainClassFieldWithButton.getText());
    settings.setArguments(myAppArguments.getText());
    settings.setNmeFlags(myNMEArguments.getText());
    if (myNmmlFileRadioButton.isSelected()) {
      settings.setNmeTarget((NMETarget) myTargetComboBox.getSelectedItem());
    } else {
      settings.setHaxeTarget((HaxeTarget) myTargetComboBox.getSelectedItem());
    }
    settings.setExcludeFromCompilation(myExcludeFromCompilationCheckBox.isSelected());
    settings.setOutputFileName(myOutputFileNameTextField.getText());

    settings.setHxmlPath(FileUtil.toSystemIndependentName(myHxmlFileChooserTextField.getText()));
    settings.setNmmlPath(FileUtil.toSystemIndependentName(myNMEFileChooserTextField.getText()));

    settings.setBuildConfig(getCurrentBuildConfig());
    for (UnnamedConfigurable configurable : configurables) {
      try {
        configurable.apply();
      } catch (ConfigurationException ignored) {
      }
    }

    ModuleCompilerPathsManager manager = ModuleCompilerPathsManager.getInstance(myModule);
    final String url = manager.getCompilerOutputUrl(ProductionContentFolderTypeProvider.getInstance());

    final String urlCandidate = VirtualFileUtil.pathToUrl(myFolderTextField.getText());

    if (!urlCandidate.equals(url)) {
      manager.setCompilerOutputUrl(ProductionContentFolderTypeProvider.getInstance(), urlCandidate);
    }
  }

  private int getCurrentBuildConfig() {
    int buildConfig = HaxeModuleSettings.USE_PROPERTIES;
    if (myHxmlFileRadioButton.isSelected()) {
      buildConfig = HaxeModuleSettings.USE_HXML;
    } else if (myNmmlFileRadioButton.isSelected()) {
      buildConfig = HaxeModuleSettings.USE_NMML;
    }
    return buildConfig;
  }

  public JComponent getMainPanel() {
    return myMainPanel;
  }
}
