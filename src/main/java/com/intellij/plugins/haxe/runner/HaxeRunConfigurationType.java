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
package com.intellij.plugins.haxe.runner;

import javax.annotation.Nonnull;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.project.Project;
import com.intellij.plugins.haxe.HaxeBundle;
import com.intellij.util.containers.ContainerUtil;
import consulo.haxe.module.extension.HaxeModuleExtension;
import consulo.module.extension.ModuleExtensionHelper;
import consulo.ui.image.Image;
import icons.HaxeIcons;

public class HaxeRunConfigurationType implements ConfigurationType
{
	private final HaxeFactory configurationFactory;

	public HaxeRunConfigurationType()
	{
		configurationFactory = new HaxeFactory(this);
	}

	public static HaxeRunConfigurationType getInstance()
	{
		return ContainerUtil.findInstance(Extensions.getExtensions(CONFIGURATION_TYPE_EP), HaxeRunConfigurationType.class);
	}

	@Override
	public String getDisplayName()
	{
		return HaxeBundle.message("runner.configuration.name");
	}

	@Override
	public String getConfigurationTypeDescription()
	{
		return HaxeBundle.message("runner.configuration.name");
	}

	@Override
	public Image getIcon()
	{
		return HaxeIcons.Haxe;
	}

	@Override
	@Nonnull
	public String getId()
	{
		return "HaxeApplicationRunConfiguration";
	}

	@Override
	public ConfigurationFactory[] getConfigurationFactories()
	{
		return new ConfigurationFactory[]{configurationFactory};
	}

	public static class HaxeFactory extends ConfigurationFactory
	{
		public HaxeFactory(ConfigurationType type)
		{
			super(type);
		}

		@Override
		public boolean isApplicable(@Nonnull Project project)
		{
			return ModuleExtensionHelper.getInstance(project).hasModuleExtension(HaxeModuleExtension.class);
		}

		@Override
		public RunConfiguration createTemplateConfiguration(Project project)
		{
			final String name = HaxeBundle.message("runner.configuration.name");
			return new HaxeApplicationConfiguration(name, project, getInstance());
		}
	}
}
