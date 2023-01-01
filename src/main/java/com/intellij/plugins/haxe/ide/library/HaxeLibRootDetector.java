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

import com.intellij.plugins.haxe.HaxeFileType;
import consulo.application.progress.ProgressIndicator;
import consulo.content.OrderRootType;
import consulo.content.library.ui.RootDetector;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.archive.ArchiveFileSystem;
import consulo.virtualFileSystem.util.VirtualFileUtil;
import consulo.virtualFileSystem.util.VirtualFileVisitor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author: Fedor.Korotkov
 */
public class HaxeLibRootDetector extends RootDetector {
  protected HaxeLibRootDetector(OrderRootType rootType, String presentableRootTypeName) {
    super(rootType, false, presentableRootTypeName);
  }

  @Nonnull
  @Override
  public Collection<VirtualFile> detectRoots(@Nonnull VirtualFile rootCandidate, @Nonnull ProgressIndicator progressIndicator) {
    List<VirtualFile> result = new ArrayList<VirtualFile>();
    collectRoots(rootCandidate, result, progressIndicator);
    return result;
  }

  public static void collectRoots(VirtualFile file, final List<VirtualFile> result, @Nullable final ProgressIndicator progressIndicator) {
    if (file.getFileSystem() instanceof ArchiveFileSystem) {
      return;
    }
    VirtualFileUtil.visitChildrenRecursively(file, new VirtualFileVisitor() {
      @Override
      public boolean visitFile(@Nonnull VirtualFile file) {
        if (progressIndicator != null) {
          progressIndicator.checkCanceled();
        }
        if (!file.isDirectory()) return false;
        if (progressIndicator != null) {
          progressIndicator.setText2(file.getPresentableUrl());
        }

        if (file.findChild(".current") != null) {
          for (VirtualFile child : file.getChildren()) {
            if (child.isDirectory() && containsHaxeFiles(child)) {
              result.add(child);
            }
          }
          return false;
        }

        return true;
      }
    });
  }

  private static boolean containsHaxeFiles(final VirtualFile dir) {
    final VirtualFileVisitor.Result result = VirtualFileUtil.visitChildrenRecursively(dir, new VirtualFileVisitor() {
      @Nonnull
      @Override
      public Result visitFileEx(@Nonnull VirtualFile file) {
        return !file.isDirectory() && HaxeFileType.DEFAULT_EXTENSION.equalsIgnoreCase(file.getExtension()) ? skipTo(dir) : CONTINUE;
      }
    });
    return result.skipToParent != null;
  }
}
