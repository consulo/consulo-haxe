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
package com.intellij.plugins.haxe.runner.ui;

import com.intellij.plugins.haxe.ide.module.HaxeModuleSettings;
import com.intellij.plugins.haxe.runner.HaxeApplicationConfiguration;
import consulo.compiler.ModuleCompilerPathsManager;
import consulo.configurable.ConfigurationException;
import consulo.execution.configuration.ui.SettingsEditor;
import consulo.fileChooser.FileChooserDescriptor;
import consulo.fileChooser.FileChooserTextBoxBuilder;
import consulo.haxe.localize.HaxeLocalize;
import consulo.haxe.module.extension.HaxeModuleExtension;
import consulo.language.content.ProductionContentFolderTypeProvider;
import consulo.language.util.ModuleUtilCore;
import consulo.module.Module;
import consulo.module.ModuleManager;
import consulo.platform.base.icon.PlatformIconGroup;
import consulo.project.Project;
import consulo.ui.CheckBox;
import consulo.ui.ComboBox;
import consulo.ui.Component;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.model.FlatDataModel;
import consulo.ui.model.MutableFlatDataModel;
import consulo.ui.util.FormBuilder;
import consulo.util.io.FileUtil;
import consulo.util.lang.StringUtil;
import consulo.virtualFileSystem.util.VirtualFileUtil;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class HaxeRunConfigurationEditorForm extends SettingsEditor<HaxeApplicationConfiguration> {
    private final MutableFlatDataModel<Module> myModuleModel = FlatDataModel.of(new ArrayList<>());

    private @Nullable ComboBox<Module> myComboModules;
    private @Nullable CheckBox myCustomPathCheckBox;
    private FileChooserTextBoxBuilder.@Nullable Controller myPathToFileTextField;
    private @Nullable CheckBox myAlternativeExecutable;
    private FileChooserTextBoxBuilder.@Nullable Controller myExecutableField;

    private String customPathToFile = "";
    private String customPathToExecutable = "";

    private final Project project;

    public HaxeRunConfigurationEditorForm(Project project) {
        this.project = project;
    }

    @Override
    @RequiredUIAccess
    protected Component createUIComponent() {
        ComboBox<Module> comboModules = ComboBox.create(myModuleModel);
        comboModules.setRenderer((presentation, item) -> {
            Module module = item.getValue();
            if (module != null) {
                presentation.withIcon(PlatformIconGroup.nodesModule());
                presentation.append(module.getName());
            }
        });
        comboModules.addValueListener(event -> {
            if (!isSelected(myCustomPathCheckBox)) {
                updateCustomPathToFile();
            }
        });
        myComboModules = comboModules;

        FileChooserTextBoxBuilder.Controller executableField = createPathField();
        myExecutableField = executableField;

        CheckBox alternativeExecutable = CheckBox.create(HaxeLocalize.haxeRunAlternativeExecutable());
        alternativeExecutable.addValueListener(event -> {
            if (!Boolean.TRUE.equals(event.getValue())) {
                customPathToExecutable = StringUtil.notNullize(executableField.getValue());
            }
            updateCustomPathToExecutable();
        });
        myAlternativeExecutable = alternativeExecutable;

        FileChooserTextBoxBuilder.Controller pathToFileTextField = createPathField();
        myPathToFileTextField = pathToFileTextField;

        CheckBox customPathCheckBox = CheckBox.create(HaxeLocalize.haxeRunFileToRun());
        customPathCheckBox.addValueListener(event -> {
            if (!Boolean.TRUE.equals(event.getValue())) {
                customPathToFile = StringUtil.notNullize(pathToFileTextField.getValue());
            }
            updateCustomPathToFile();
        });
        myCustomPathCheckBox = customPathCheckBox;

        updateCustomPathToExecutable();
        updateCustomPathToFile();

        return FormBuilder.create()
            .addLabeled(HaxeLocalize.haxeRunModule(), comboModules)
            .addLabeled(alternativeExecutable, executableField.getComponent())
            .addLabeled(customPathCheckBox, pathToFileTextField.getComponent())
            .build();
    }

    @RequiredUIAccess
    private FileChooserTextBoxBuilder.Controller createPathField() {
        return FileChooserTextBoxBuilder.create(project)
            .fileChooserDescriptor(new FileChooserDescriptor(true, false, false, true, false, false))
            .build();
    }

    @RequiredUIAccess
    @Override
    protected void resetEditorFrom(HaxeApplicationConfiguration configuration) {
        Module selectedModule = configuration.getConfigurationModule().getModule();

        List<Module> modules = new ArrayList<>();
        for (Module module : ModuleManager.getInstance(configuration.getProject()).getModules()) {
            if (ModuleUtilCore.getExtension(module, HaxeModuleExtension.class) != null) {
                modules.add(module);
            }
        }
        if (selectedModule != null && !modules.contains(selectedModule)) {
            modules.add(selectedModule);
        }
        myModuleModel.replaceAll(modules);

        customPathToFile = toSystemDependentPath(configuration.getCustomFileToLaunchPath());
        customPathToExecutable = toSystemDependentPath(configuration.getCustomExecutablePath());

        ComboBox<Module> comboModules = myComboModules;
        CheckBox customPathCheckBox = myCustomPathCheckBox;
        CheckBox alternativeExecutable = myAlternativeExecutable;
        if (comboModules == null || customPathCheckBox == null || alternativeExecutable == null) {
            return;
        }

        comboModules.setValue(selectedModule, false);
        customPathCheckBox.setValue(configuration.isCustomFileToLaunch(), false);
        alternativeExecutable.setValue(configuration.isCustomExecutable(), false);

        updateCustomPathToExecutable();
        updateCustomPathToFile();
    }

    @RequiredUIAccess
    private void updateCustomPathToFile() {
        FileChooserTextBoxBuilder.Controller pathToFileTextField = myPathToFileTextField;
        if (pathToFileTextField == null) {
            return;
        }

        boolean custom = isSelected(myCustomPathCheckBox);
        Module selectedModule = getSelectedModule();
        if (custom) {
            pathToFileTextField.setValue(customPathToFile, false);
        }
        else if (selectedModule != null) {
            HaxeModuleSettings settings = HaxeModuleSettings.getInstance(selectedModule);
            ModuleCompilerPathsManager compilerPathsManager = ModuleCompilerPathsManager.getInstance(selectedModule);

            String outputUrl = compilerPathsManager.getCompilerOutputUrl(ProductionContentFolderTypeProvider.getInstance());
            String url = outputUrl + "/" + settings.getOutputFileName();
            pathToFileTextField.setValue(FileUtil.toSystemDependentName(VirtualFileUtil.urlToPath(url)), false);
        }
        else {
            pathToFileTextField.setValue("", false);
        }
        pathToFileTextField.getComponent().setEnabled(custom);
    }

    @RequiredUIAccess
    private void updateCustomPathToExecutable() {
        FileChooserTextBoxBuilder.Controller executableField = myExecutableField;
        if (executableField == null) {
            return;
        }

        boolean alternative = isSelected(myAlternativeExecutable);
        executableField.setValue(alternative ? customPathToExecutable : "", false);
        executableField.getComponent().setEnabled(alternative);
    }

    @Override
    @RequiredUIAccess
    protected void applyEditorTo(HaxeApplicationConfiguration configuration) throws ConfigurationException {
        ComboBox<Module> comboModules = myComboModules;
        CheckBox customPathCheckBox = myCustomPathCheckBox;
        FileChooserTextBoxBuilder.Controller pathToFileTextField = myPathToFileTextField;
        CheckBox alternativeExecutable = myAlternativeExecutable;
        FileChooserTextBoxBuilder.Controller executableField = myExecutableField;
        if (comboModules == null
            || customPathCheckBox == null
            || pathToFileTextField == null
            || alternativeExecutable == null
            || executableField == null) {
            return;
        }

        boolean custom = isSelected(customPathCheckBox);
        boolean alternative = isSelected(alternativeExecutable);

        configuration.setModule(comboModules.getValue());
        configuration.setCustomFileToLaunch(custom);
        configuration.setCustomExecutable(alternative);
        if (custom) {
            configuration.setCustomFileToLaunchPath(toSystemIndependentPath(pathToFileTextField.getValue()));
        }
        if (alternative) {
            configuration.setCustomExecutablePath(toSystemIndependentPath(executableField.getValue()));
        }
    }

    @Nullable
    @RequiredUIAccess
    private Module getSelectedModule() {
        ComboBox<Module> comboModules = myComboModules;
        return comboModules == null ? null : comboModules.getValue();
    }

    @RequiredUIAccess
    private static boolean isSelected(@Nullable CheckBox checkBox) {
        return checkBox != null && Boolean.TRUE.equals(checkBox.getValue());
    }

    private static String toSystemDependentPath(@Nullable String path) {
        String value = StringUtil.notNullize(path);
        return value.contains("://") ? value : FileUtil.toSystemDependentName(value);
    }

    private static String toSystemIndependentPath(@Nullable String path) {
        String value = StringUtil.notNullize(path);
        return value.contains("://") ? value : FileUtil.toSystemIndependentName(value);
    }
}
