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

import com.intellij.plugins.haxe.ide.HaxeFileTemplateUtil;
import consulo.application.AllIcons;
import consulo.dataContext.DataContext;
import consulo.fileTemplate.FileTemplate;
import consulo.fileTemplate.FileTemplateManager;
import consulo.fileTemplate.FileTemplateUtil;
import consulo.haxe.localize.HaxeLocalize;
import consulo.haxe.module.extension.HaxeModuleExtension;
import consulo.ide.action.CreateFileFromTemplateDialog;
import consulo.ide.action.CreateTemplateInPackageAction;
import consulo.language.editor.LangDataKeys;
import consulo.language.psi.PsiDirectory;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.util.IncorrectOperationException;
import consulo.language.util.ModuleUtilCore;
import consulo.localize.LocalizeValue;
import consulo.module.Module;
import consulo.module.content.DirectoryIndex;
import consulo.project.Project;
import consulo.ui.image.Image;
import jakarta.annotation.Nonnull;

import java.util.Properties;

/**
 * @author Fedor.Korotkov
 */
public class CreateClassAction extends CreateTemplateInPackageAction<PsiFile> {
  public CreateClassAction() {
    super(HaxeLocalize.actionCreateNewClass(), HaxeLocalize.actionCreateNewClass(), AllIcons.Nodes.Class, true);
  }

  @Override
  protected boolean isAvailable(DataContext dataContext) {
    final Module module = dataContext.getData(LangDataKeys.MODULE);
    return super.isAvailable(dataContext) && module != null && ModuleUtilCore.getExtension(module, HaxeModuleExtension.class) != null;
  }

  @Override
  protected PsiElement getNavigationElement(@Nonnull PsiFile createdElement) {
    return createdElement.getNavigationElement();
  }

  @Override
  protected boolean checkPackageExists(PsiDirectory directory) {
    return DirectoryIndex.getInstance(directory.getProject()).getPackageName(directory.getVirtualFile()) != null;
  }

  @Override
  protected LocalizeValue getActionName(PsiDirectory directory, String newName, String templateName) {
    return HaxeLocalize.progressCreatingClass(newName);
  }

  @Override
  protected void buildDialog(Project project, PsiDirectory directory, CreateFileFromTemplateDialog.Builder builder) {
    builder.setTitle(LocalizeValue.localizeTODO("Create New Class"));
    for (FileTemplate fileTemplate : HaxeFileTemplateUtil.getApplicableTemplates()) {
      final String templateName = fileTemplate.getName();
      final String shortName = HaxeFileTemplateUtil.getTemplateShortName(templateName);
      final Image icon = HaxeFileTemplateUtil.getTemplateIcon(templateName);
      builder.addKind(LocalizeValue.of(shortName), icon, templateName);
    }
  }

  @Override
  protected PsiFile doCreate(@Nonnull PsiDirectory dir, String className, String templateName) throws IncorrectOperationException {
    String packageName = DirectoryIndex.getInstance(dir.getProject()).getPackageName(dir.getVirtualFile());
    try {
      return createClass(className, packageName, dir, templateName).getContainingFile();
    } catch (Exception e) {
      throw new IncorrectOperationException(e.getMessage(), e);
    }
  }

  private static PsiElement createClass(String className, String packageName, @Nonnull PsiDirectory directory,
                                        final String templateName) throws Exception {
    final Properties props = new Properties(FileTemplateManager.getInstance(directory.getProject()).getDefaultProperties(directory.getProject()));
    props.setProperty(FileTemplate.ATTRIBUTE_NAME, className);
    props.setProperty(FileTemplate.ATTRIBUTE_PACKAGE_NAME, packageName);

    final FileTemplate template = FileTemplateManager.getInstance().getInternalTemplate(templateName);

    return FileTemplateUtil.createFromTemplate(template, className, props, directory, CreateClassAction.class.getClassLoader());
  }
}
