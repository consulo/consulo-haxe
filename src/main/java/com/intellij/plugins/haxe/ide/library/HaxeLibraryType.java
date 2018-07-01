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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.JComponent;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.libraries.DummyLibraryProperties;
import com.intellij.openapi.roots.libraries.LibraryType;
import com.intellij.openapi.roots.libraries.LibraryTypeService;
import com.intellij.openapi.roots.libraries.NewLibraryConfiguration;
import com.intellij.openapi.roots.libraries.PersistentLibraryKind;
import com.intellij.openapi.roots.libraries.ui.LibraryEditorComponent;
import com.intellij.openapi.roots.libraries.ui.LibraryPropertiesEditor;
import com.intellij.openapi.roots.libraries.ui.LibraryRootsComponentDescriptor;
import com.intellij.openapi.vfs.VirtualFile;
import consulo.ui.image.Image;
import icons.HaxeIcons;

/**
 * @author: Fedor.Korotkov
 */
public class HaxeLibraryType extends LibraryType<DummyLibraryProperties> {
  public static final PersistentLibraryKind<DummyLibraryProperties> HAXE_LIBRARY =
    new PersistentLibraryKind<DummyLibraryProperties>("haXe") {
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
    return LibraryType.EP_NAME.findExtension(HaxeLibraryType.class);
  }
}
