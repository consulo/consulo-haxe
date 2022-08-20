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

import com.intellij.plugins.haxe.HaxeBundle;
import com.intellij.plugins.haxe.ide.HaxeFileTemplateUtil;
import consulo.application.AllIcons;
import consulo.dataContext.DataContext;
import consulo.fileTemplate.FileTemplate;
import consulo.fileTemplate.FileTemplateManager;
import consulo.fileTemplate.FileTemplateUtil;
import consulo.haxe.module.extension.HaxeModuleExtension;
import consulo.ide.IdeBundle;
import consulo.ide.action.CreateFileFromTemplateDialog;
import consulo.ide.action.CreateTemplateInPackageAction;
import consulo.language.editor.LangDataKeys;
import consulo.language.psi.PsiDirectory;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.util.IncorrectOperationException;
import consulo.language.util.ModuleUtilCore;
import consulo.module.Module;
import consulo.module.content.DirectoryIndex;
import consulo.project.Project;
import consulo.ui.image.Image;

import javax.annotation.Nonnull;
import java.util.Properties;

/**
 * @author: Fedor.Korotkov
 */
public class CreateClassAction extends CreateTemplateInPackageAction<PsiFile> {
  public CreateClassAction() {
    super(HaxeBundle.message("action.create.new.class"), HaxeBundle.message("action.create.new.class"), AllIcons.Nodes.Class, true);
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
  protected String getActionName(PsiDirectory directory, String newName, String templateName) {
    return HaxeBundle.message("progress.creating.class", newName);
  }

  @Override
  protected void buildDialog(Project project, PsiDirectory directory, CreateFileFromTemplateDialog.Builder builder) {
    builder.setTitle(IdeBundle.message("action.create.new.class"));
    for (FileTemplate fileTemplate : HaxeFileTemplateUtil.getApplicableTemplates()) {
      final String templateName = fileTemplate.getName();
      final String shortName = HaxeFileTemplateUtil.getTemplateShortName(templateName);
      final Image icon = HaxeFileTemplateUtil.getTemplateIcon(templateName);
      builder.addKind(shortName, icon, templateName);
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
