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

import com.intellij.plugins.haxe.HaxeIcons;
import consulo.annotation.component.ExtensionImpl;
import consulo.application.Application;
import consulo.application.util.SystemInfo;
import consulo.content.OrderRootType;
import consulo.content.base.BinariesOrderRootType;
import consulo.content.base.DocumentationOrderRootType;
import consulo.content.base.SourcesOrderRootType;
import consulo.content.bundle.*;
import consulo.haxe.localize.HaxeLocalize;
import consulo.ui.image.Image;
import consulo.util.xml.serializer.XmlSerializer;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.VirtualFileManager;
import jakarta.annotation.Nonnull;
import org.jdom.Element;

import java.util.Collection;
import java.util.Collections;

@ExtensionImpl
public class HaxeSdkType extends SdkType {
    @Nonnull
    public static HaxeSdkType getInstance() {
        return Application.get().getExtensionPoint(SdkType.class).findExtensionOrFail(HaxeSdkType.class);
    }

    public HaxeSdkType() {
        super("HAXE_SDK");
    }

    @Override
    public Image getIcon() {
        return HaxeIcons.Haxe;
    }

    @Nonnull
    @Override
    public String getPresentableName() {
        return HaxeLocalize.haxeSdkNamePresentable().get();
    }

    @Override
    public String suggestSdkName(String currentSdkName, String sdkHome) {
        return HaxeLocalize.haxeSdkNameSuggest(getVersionString(sdkHome)).get();
    }

    @Override
    public String getVersionString(String sdkHome) {
        final HaxeSdkData haxeSdkData = HaxeSdkUtil.testHaxeSdk(sdkHome);
        return haxeSdkData != null ? haxeSdkData.getVersion() : null;
    }

    @Nonnull
    @Override
    public Collection<String> suggestHomePaths() {
        String result = System.getenv("HAXEPATH");
        if (result == null && !SystemInfo.isWindows) {
            final String candidate = "/usr/lib/haxe";
            if (VirtualFileManager.getInstance().findFileByUrl(candidate) != null) {
                result = candidate;
            }
        }
        if (result != null) {
            return Collections.singletonList(result);
        }
        return Collections.emptyList();
    }

    @Override
    public boolean canCreatePredefinedSdks() {
        return true;
    }

    @Override
    public boolean isValidSdkHome(String path) {
        return HaxeSdkUtil.testHaxeSdk(path) != null;
    }

    @Override
    public AdditionalDataConfigurable createAdditionalDataConfigurable(SdkModel sdkModel, SdkModificator sdkModificator) {
        return new HaxeAdditionalConfigurable();
    }

    @Override
    public boolean isRootTypeApplicable(OrderRootType type) {
        return type == SourcesOrderRootType.getInstance() || type == BinariesOrderRootType.getInstance() || type == DocumentationOrderRootType.getInstance();
    }

    @Override
    public void setupSdkPaths(Sdk sdk) {
        final SdkModificator modificator = sdk.getSdkModificator();

        SdkAdditionalData data = sdk.getSdkAdditionalData();
        if (data == null) {
            data = HaxeSdkUtil.testHaxeSdk(sdk.getHomePath());
            modificator.setSdkAdditionalData(data);
        }

        VirtualFile homeDirectory = sdk.getHomeDirectory();
        if (homeDirectory != null) {
            final VirtualFile stdRoot = homeDirectory.findChild("std");
            if (stdRoot != null) {
                modificator.addRoot(stdRoot, BinariesOrderRootType.getInstance());
                modificator.addRoot(stdRoot, SourcesOrderRootType.getInstance());
            }
            final VirtualFile docRoot = homeDirectory.findChild("doc");
            if (docRoot != null) {
                modificator.addRoot(docRoot, DocumentationOrderRootType.getInstance());
            }
        }

        modificator.commitChanges();
    }

    @Override
    public SdkAdditionalData loadAdditionalData(Sdk sdk, Element additional) {
        return XmlSerializer.deserialize(additional, HaxeSdkData.class);
    }

    @Override
    public void saveAdditionalData(SdkAdditionalData additionalData, Element additional) {
        if (additionalData instanceof HaxeSdkData) {
            XmlSerializer.serializeInto(additionalData, additional);
        }
    }
}
