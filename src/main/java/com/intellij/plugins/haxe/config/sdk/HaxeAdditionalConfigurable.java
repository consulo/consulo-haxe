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

import consulo.application.ApplicationManager;
import consulo.configurable.ConfigurationException;
import consulo.content.bundle.AdditionalDataConfigurable;
import consulo.content.bundle.Sdk;
import consulo.content.bundle.SdkModificator;
import consulo.disposer.Disposable;
import consulo.fileChooser.FileChooserDescriptorFactory;
import consulo.fileChooser.FileChooserTextBoxBuilder;
import consulo.haxe.localize.HaxeLocalize;
import consulo.ui.Component;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.util.FormBuilder;
import consulo.util.io.FileUtil;
import jakarta.annotation.Nullable;

/**
 * @author: Fedor.Korotkov
 */
public class HaxeAdditionalConfigurable implements AdditionalDataConfigurable {
    private Sdk mySdk;

    @Nullable
    private Component myComponent;
    @Nullable
    private FileChooserTextBoxBuilder.Controller myNekoBox;
    @Nullable
    private FileChooserTextBoxBuilder.Controller myHaxelibBox;

    @Override
    public void setSdk(Sdk sdk) {
        mySdk = sdk;
    }

    @RequiredUIAccess
    @Override
    public Component createUIComponent(Disposable parentDisposable) {
        if (myComponent == null) {
            myNekoBox = createExecutableBox(parentDisposable);
            myHaxelibBox = createExecutableBox(parentDisposable);

            FormBuilder builder = FormBuilder.create();
            builder.addLabeled(HaxeLocalize.nekoExecutable(), myNekoBox.getComponent());
            builder.addLabeled(HaxeLocalize.haxelibExecutable(), myHaxelibBox.getComponent());
            myComponent = builder.build();
        }
        return myComponent;
    }

    @RequiredUIAccess
    private static FileChooserTextBoxBuilder.Controller createExecutableBox(Disposable parentDisposable) {
        FileChooserTextBoxBuilder builder = FileChooserTextBoxBuilder.create(null);
        builder.fileChooserDescriptor(FileChooserDescriptorFactory.createSingleFileNoJarsDescriptor());
        builder.uiDisposable(parentDisposable);
        return builder.build();
    }

    @RequiredUIAccess
    @Override
    public boolean isModified() {
        if (myNekoBox == null || myHaxelibBox == null) {
            return false;
        }

        final HaxeSdkData haxeSdkData = getHaxeSdkData();
        return haxeSdkData == null ||
            !getPath(myNekoBox).equals(haxeSdkData.getNekoBinPath()) ||
            !getPath(myHaxelibBox).equals(haxeSdkData.getHaxelibPath());
    }

    @RequiredUIAccess
    @Override
    public void apply() throws ConfigurationException {
        final HaxeSdkData haxeSdkData = getHaxeSdkData();
        if (haxeSdkData == null || myNekoBox == null || myHaxelibBox == null) {
            return;
        }

        final HaxeSdkData newData = new HaxeSdkData(haxeSdkData.getHomePath(), haxeSdkData.getVersion());
        newData.setNekoBinPath(getPath(myNekoBox));
        newData.setHaxelibPath(getPath(myHaxelibBox));

        final SdkModificator modificator = mySdk.getSdkModificator();
        modificator.setSdkAdditionalData(newData);
        ApplicationManager.getApplication().runWriteAction(modificator::commitChanges);
    }

    private static String getPath(FileChooserTextBoxBuilder.Controller box) {
        return FileUtil.toSystemIndependentName(box.getValue());
    }

    @Nullable
    private HaxeSdkData getHaxeSdkData() {
        return mySdk.getSdkAdditionalData() instanceof HaxeSdkData ? (HaxeSdkData) mySdk.getSdkAdditionalData() : null;
    }

    @RequiredUIAccess
    @Override
    public void reset() {
        final HaxeSdkData haxeSdkData = getHaxeSdkData();
        if (haxeSdkData != null && myNekoBox != null && myHaxelibBox != null) {
            final String nekoBinPath = haxeSdkData.getNekoBinPath();
            myNekoBox.setValue(FileUtil.toSystemDependentName(nekoBinPath == null ? "" : nekoBinPath));
            final String haxelibPath = haxeSdkData.getHaxelibPath();
            myHaxelibBox.setValue(FileUtil.toSystemDependentName(haxelibPath == null ? "" : haxelibPath));
        }
    }

    @RequiredUIAccess
    @Override
    public void disposeUIResources() {
        myComponent = null;
        myNekoBox = null;
        myHaxelibBox = null;
    }
}
