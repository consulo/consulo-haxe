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
package com.intellij.plugins.haxe.config.sdk;

import java.util.Collection;
import java.util.Collections;

import javax.swing.Icon;

import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.PathChooserDialog;
import com.intellij.openapi.projectRoots.AdditionalDataConfigurable;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkAdditionalData;
import com.intellij.openapi.projectRoots.SdkModel;
import com.intellij.openapi.projectRoots.SdkModificator;
import com.intellij.openapi.projectRoots.SdkType;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.plugins.haxe.HaxeBundle;
import com.intellij.util.xmlb.XmlSerializer;
import consulo.roots.types.BinariesOrderRootType;
import consulo.roots.types.DocumentationOrderRootType;
import consulo.roots.types.SourcesOrderRootType;
import icons.HaxeIcons;

public class HaxeSdkType extends SdkType
{
	@NotNull
	public static HaxeSdkType getInstance()
	{
		return EP_NAME.findExtension(HaxeSdkType.class);
	}

	public HaxeSdkType()
	{
		super("HAXE_SDK");
	}

	@Override
	public Icon getIcon()
	{
		return HaxeIcons.Haxe;
	}

	@Nullable
	@Override
	public Icon getGroupIcon()
	{
		return HaxeIcons.Haxe;
	}

	@NotNull
	@Override
	public String getPresentableName()
	{
		return HaxeBundle.message("haxe.sdk.name.presentable");
	}

	@Override
	public String suggestSdkName(String currentSdkName, String sdkHome)
	{
		return HaxeBundle.message("haxe.sdk.name.suggest", getVersionString(sdkHome));
	}

	@Override
	public String getVersionString(String sdkHome)
	{
		final HaxeSdkData haxeSdkData = HaxeSdkUtil.testHaxeSdk(sdkHome);
		return haxeSdkData != null ? haxeSdkData.getVersion() : null;
	}

	@NotNull
	@Override
	public Collection<String> suggestHomePaths()
	{
		String result = System.getenv("HAXEPATH");
		if(result == null && !SystemInfo.isWindows)
		{
			final String candidate = "/usr/lib/haxe";
			if(VirtualFileManager.getInstance().findFileByUrl(candidate) != null)
			{
				result = candidate;
			}
		}
		if(result != null)
		{
			return Collections.singletonList(result);
		}
		return Collections.emptyList();
	}

	@Override
	public boolean canCreatePredefinedSdks()
	{
		return true;
	}

	@Override
	public boolean isValidSdkHome(String path)
	{
		return HaxeSdkUtil.testHaxeSdk(path) != null;
	}

	@Override
	public AdditionalDataConfigurable createAdditionalDataConfigurable(SdkModel sdkModel, SdkModificator sdkModificator)
	{
		return new HaxeAdditionalConfigurable();
	}

	@Override
	public boolean isRootTypeApplicable(OrderRootType type)
	{
		return type == SourcesOrderRootType.getInstance() || type == BinariesOrderRootType.getInstance() || type == DocumentationOrderRootType
				.getInstance();
	}

	@Override
	public void setupSdkPaths(Sdk sdk)
	{
		final SdkModificator modificator = sdk.getSdkModificator();

		SdkAdditionalData data = sdk.getSdkAdditionalData();
		if(data == null)
		{
			data = HaxeSdkUtil.testHaxeSdk(sdk.getHomePath());
			modificator.setSdkAdditionalData(data);
		}

		VirtualFile homeDirectory = sdk.getHomeDirectory();
		if(homeDirectory != null)
		{
			final VirtualFile stdRoot = homeDirectory.findChild("std");
			if(stdRoot != null)
			{
				modificator.addRoot(stdRoot, BinariesOrderRootType.getInstance());
				modificator.addRoot(stdRoot, SourcesOrderRootType.getInstance());
			}
			final VirtualFile docRoot = homeDirectory.findChild("doc");
			if(docRoot != null)
			{
				modificator.addRoot(docRoot, DocumentationOrderRootType.getInstance());
			}
		}

		modificator.commitChanges();
	}

	@Override
	public SdkAdditionalData loadAdditionalData(Sdk sdk, Element additional)
	{
		return XmlSerializer.deserialize(additional, HaxeSdkData.class);
	}

	@Override
	public void saveAdditionalData(SdkAdditionalData additionalData, Element additional)
	{
		if(additionalData instanceof HaxeSdkData)
		{
			XmlSerializer.serializeInto(additionalData, additional);
		}
	}

	@Override
	public FileChooserDescriptor getHomeChooserDescriptor()
	{
		final FileChooserDescriptor result = super.getHomeChooserDescriptor();
		if(SystemInfo.isMac)
		{
			result.putUserData(PathChooserDialog.NATIVE_MAC_CHOOSER_SHOW_HIDDEN_FILES, Boolean.TRUE);
		}
		return result;
	}
}
