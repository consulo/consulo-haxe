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

import java.util.Properties;

import consulo.awt.TargetAWT;
import consulo.haxe.module.extension.HaxeModuleExtension;
import javax.annotation.Nonnull;
import com.intellij.icons.AllIcons;
import com.intellij.ide.IdeBundle;
import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import com.intellij.ide.actions.CreateTemplateInPackageAction;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.FileTemplateUtil;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.impl.DirectoryIndex;
import com.intellij.plugins.haxe.HaxeBundle;
import com.intellij.plugins.haxe.ide.HaxeFileTemplateUtil;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import consulo.ide.IconDescriptor;
import consulo.ui.image.Image;
import consulo.ui.image.ImageEffects;
import icons.HaxeIcons;

/**
 * @author: Fedor.Korotkov
 */
public class CreateClassAction extends CreateTemplateInPackageAction<PsiFile>
{
	public CreateClassAction()
	{
		super(HaxeBundle.message("action.create.new.class"), HaxeBundle.message("action.create.new.class"), TargetAWT.to(ImageEffects.layered(AllIcons.Nodes.Class, HaxeIcons.HaxeLang)), true);
	}

	@Override
	protected boolean isAvailable(DataContext dataContext)
	{
		final Module module = dataContext.getData(LangDataKeys.MODULE);
		return super.isAvailable(dataContext) && module != null && ModuleUtilCore.getExtension(module, HaxeModuleExtension.class) != null;
	}

	@Override
	protected PsiElement getNavigationElement(@Nonnull PsiFile createdElement)
	{
		return createdElement.getNavigationElement();
	}

	@Override
	protected boolean checkPackageExists(PsiDirectory directory)
	{
		return DirectoryIndex.getInstance(directory.getProject()).getPackageName(directory.getVirtualFile()) != null;
	}

	@Override
	protected String getActionName(PsiDirectory directory, String newName, String templateName)
	{
		return HaxeBundle.message("progress.creating.class", newName);
	}

	@Override
	protected void buildDialog(Project project, PsiDirectory directory, CreateFileFromTemplateDialog.Builder builder)
	{
		builder.setTitle(IdeBundle.message("action.create.new.class"));
		for(FileTemplate fileTemplate : HaxeFileTemplateUtil.getApplicableTemplates())
		{
			final String templateName = fileTemplate.getName();
			final String shortName = HaxeFileTemplateUtil.getTemplateShortName(templateName);
			final Image icon = HaxeFileTemplateUtil.getTemplateIcon(templateName);
			builder.addKind(shortName, TargetAWT.to(icon), templateName);
		}
	}

	@Override
	protected PsiFile doCreate(@Nonnull PsiDirectory dir, String className, String templateName) throws IncorrectOperationException
	{
		String packageName = DirectoryIndex.getInstance(dir.getProject()).getPackageName(dir.getVirtualFile());
		try
		{
			return createClass(className, packageName, dir, templateName).getContainingFile();
		}
		catch(Exception e)
		{
			throw new IncorrectOperationException(e.getMessage(), e);
		}
	}

	private static PsiElement createClass(String className, String packageName, @Nonnull PsiDirectory directory,
			final String templateName) throws Exception
	{
		final Properties props = new Properties(FileTemplateManager.getInstance(directory.getProject()).getDefaultProperties(directory.getProject()));
		props.setProperty(FileTemplate.ATTRIBUTE_NAME, className);
		props.setProperty(FileTemplate.ATTRIBUTE_PACKAGE_NAME, packageName);

		final FileTemplate template = FileTemplateManager.getInstance().getInternalTemplate(templateName);

		return FileTemplateUtil.createFromTemplate(template, className, props, directory, CreateClassAction.class.getClassLoader());
	}
}
