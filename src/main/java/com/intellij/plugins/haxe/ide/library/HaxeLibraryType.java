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
package com.intellij.plugins.haxe.ide.library;

import com.intellij.plugins.haxe.HaxeIcons;
import consulo.annotation.component.ExtensionImpl;
import consulo.application.Application;
import consulo.content.library.*;
import consulo.content.library.ui.LibraryEditorComponent;
import consulo.content.library.ui.LibraryPropertiesEditor;
import consulo.content.library.ui.LibraryRootsComponentDescriptor;
import consulo.project.Project;
import consulo.ui.image.Image;
import consulo.virtualFileSystem.VirtualFile;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import javax.swing.*;

/**
 * @author Fedor.Korotkov
 */
@ExtensionImpl
public class HaxeLibraryType extends LibraryType<DummyLibraryProperties> {
    public static final PersistentLibraryKind<DummyLibraryProperties> HAXE_LIBRARY = new PersistentLibraryKind<>("haXe") {
        @Nonnull
        @Override
        public DummyLibraryProperties createDefaultProperties() {
            return new DummyLibraryProperties();
        }
    };

    public HaxeLibraryType() {
        super(HAXE_LIBRARY);
    }

    @Nonnull
    @Override
    public String getCreateActionName() {
        return "haXe";
    }

    @Override
    public NewLibraryConfiguration createNewLibrary(@Nonnull JComponent parentComponent,
                                                    @Nullable VirtualFile contextDirectory,
                                                    @Nonnull Project project) {

        return LibraryTypeService.getInstance()
            .createLibraryFromFiles(createLibraryRootsComponentDescriptor(), parentComponent, contextDirectory, this, project);
    }

    @Nonnull
    @Override
    public LibraryRootsComponentDescriptor createLibraryRootsComponentDescriptor() {
        return new HaxeLibraryRootsComponentDescriptor();
    }

    @Override
    public LibraryPropertiesEditor createPropertiesEditor(@Nonnull LibraryEditorComponent<DummyLibraryProperties> component) {
        return null;
    }

    @Override
    public Image getIcon() {
        return HaxeIcons.Haxe;
    }

    public static HaxeLibraryType getInstance() {
        return Application.get().getExtensionPoint(LibraryType.class).findExtension(HaxeLibraryType.class);
    }
}
