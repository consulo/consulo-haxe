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
package com.intellij.plugins.haxe.nmml;

import com.intellij.xml.XmlSchemaProvider;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.psi.PsiFile;
import consulo.module.Module;
import consulo.util.io.ClassPathUtil;
import consulo.util.io.FileUtil;
import consulo.virtualFileSystem.LocalFileSystem;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.archive.ArchiveVfsUtil;
import consulo.xml.psi.xml.XmlFile;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * @author: Fedor.Korotkov
 */
@ExtensionImpl
public class NMMLSchemaProvider extends XmlSchemaProvider {
  @Override
  public XmlFile getSchema(@Nonnull String url, @Nullable Module module, @Nonnull PsiFile baseFile) {
    String jarFilePath = ClassPathUtil.getJarPathForClass(NMMLSchemaProvider.class);
    VirtualFile jarFile = LocalFileSystem.getInstance().findFileByPath(jarFilePath);
    if (jarFile != null) {
      VirtualFile archiveRoot = ArchiveVfsUtil.getArchiveRootForLocalFile(jarFile);
      if (archiveRoot != null) {
        VirtualFile nmmlXsd = archiveRoot.findFileByRelativePath("/com/intellij/plugins/haxe/nmml/nmml.xsd");
        if (nmmlXsd != null) {
          PsiFile result = baseFile.getManager().findFile(nmmlXsd);
          if (result instanceof XmlFile) {
            return (XmlFile) result.copy();
          }
        }
      }
    }
    return null;
  }

  @Override
  public boolean isAvailable(final @Nonnull XmlFile file) {
    return FileUtil.extensionEquals(file.getName(), NMMLFileType.INSTANCE.getDefaultExtension());
  }
}
