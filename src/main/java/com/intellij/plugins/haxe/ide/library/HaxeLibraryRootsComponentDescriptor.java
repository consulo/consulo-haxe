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

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.ProjectBundle;
import com.intellij.openapi.projectRoots.ui.Util;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.roots.libraries.ui.AttachRootButtonDescriptor;
import com.intellij.openapi.roots.libraries.ui.LibraryRootsComponentDescriptor;
import com.intellij.openapi.roots.libraries.ui.OrderRootTypePresentation;
import com.intellij.openapi.roots.libraries.ui.RootDetector;
import com.intellij.openapi.roots.ui.configuration.libraryEditor.DefaultLibraryRootsComponentDescriptor;
import com.intellij.openapi.roots.ui.configuration.libraryEditor.LibraryEditor;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.plugins.haxe.HaxeBundle;
import javax.annotation.Nonnull;

import javax.annotation.Nullable;
import javax.swing.*;
import java.util.Arrays;
import java.util.List;

/**
 * @author: Fedor.Korotkov
 */
public class HaxeLibraryRootsComponentDescriptor extends LibraryRootsComponentDescriptor {
  @Override
  public OrderRootTypePresentation getRootTypePresentation(@Nonnull OrderRootType type) {
    return DefaultLibraryRootsComponentDescriptor.getDefaultPresentation(type);
  }

  @Nonnull
  @Override
  public List<? extends RootDetector> getRootDetectors() {
    return Arrays.asList(
      new HaxeLibRootDetector(OrderRootType.SOURCES, HaxeBundle.message("sources.root.detector.sources.name")),
      new HaxeLibRootDetector(OrderRootType.CLASSES, HaxeBundle.message("sources.root.detector.classes.name"))
    );
  }

  @Nonnull
  @Override
  public List<? extends AttachRootButtonDescriptor> createAttachButtons() {
    return Arrays.asList(new AttachUrlJavadocDescriptor());
  }

  private static class AttachUrlJavadocDescriptor extends AttachRootButtonDescriptor {
    private AttachUrlJavadocDescriptor() {
      super(OrderRootType.DOCUMENTATION, ProjectBundle.message("module.libraries.javadoc.url.button"));
    }

    @Override
    public VirtualFile[] selectFiles(@Nonnull JComponent parent,
                                     @Nullable VirtualFile initialSelection,
                                     @Nullable Module contextModule,
                                     @Nonnull LibraryEditor libraryEditor) {
      final VirtualFile vFile = Util.showSpecifyJavadocUrlDialog(parent);
      if (vFile != null) {
        return new VirtualFile[]{vFile};
      }
      return VirtualFile.EMPTY_ARRAY;
    }
  }
}
