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
package com.intellij.plugins.haxe.ide.actions;

import com.intellij.plugins.haxe.HaxeIcons;
import com.intellij.plugins.haxe.ide.HaxeFileTemplateUtil;
import consulo.application.dumb.DumbAware;
import consulo.dataContext.DataContext;
import consulo.fileTemplate.FileTemplate;
import consulo.haxe.localize.HaxeLocalize;
import consulo.haxe.module.extension.HaxeModuleExtension;
import consulo.ide.action.CreateFileFromTemplateAction;
import consulo.ide.action.CreateFileFromTemplateDialog;
import consulo.language.editor.LangDataKeys;
import consulo.language.psi.PsiDirectory;
import consulo.language.util.ModuleUtilCore;
import consulo.localize.LocalizeValue;
import consulo.module.Module;
import consulo.project.Project;

/**
 * @author Fedor.Korotkov
 */
public class CreateNMMLFileAction extends CreateFileFromTemplateAction implements DumbAware {
  public CreateNMMLFileAction() {
    super(HaxeLocalize.createNmmlFileAction(), HaxeLocalize.createNmmlFileActionDescription(), HaxeIcons.Nmml_16);
  }

  @Override
  protected boolean isAvailable(DataContext dataContext) {
    final Module module = dataContext.getData(LangDataKeys.MODULE);
    return super.isAvailable(dataContext) && module != null && ModuleUtilCore.getExtension(module, HaxeModuleExtension.class) != null;
  }

  @Override
  protected void buildDialog(Project project, PsiDirectory directory, CreateFileFromTemplateDialog.Builder builder) {
    builder.setTitle(HaxeLocalize.createNmmlFileAction());
    for (FileTemplate fileTemplate : HaxeFileTemplateUtil.getNMMLTemplates()) {
      final String templateName = fileTemplate.getName();
      final String shortName = HaxeFileTemplateUtil.getTemplateShortName(templateName);
      builder.addKind(LocalizeValue.of(shortName), HaxeIcons.Nmml_16, templateName);
    }
  }

  @Override
  protected LocalizeValue getActionName(PsiDirectory directory, String newName, String templateName) {
    return HaxeLocalize.createNmmlFileAction();
  }
}
