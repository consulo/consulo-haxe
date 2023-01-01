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
package com.intellij.plugins.haxe.util;

import com.intellij.plugins.haxe.HaxeBundle;
import com.intellij.plugins.haxe.HaxeFileType;
import consulo.application.ApplicationManager;
import consulo.application.progress.ProgressIndicator;
import consulo.application.progress.ProgressManager;
import consulo.application.progress.Task;
import consulo.content.ContentIterator;
import consulo.document.util.FileContentUtilCore;
import consulo.language.psi.stub.FileBasedIndex;
import consulo.project.Project;
import consulo.virtualFileSystem.VirtualFile;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author: Fedor.Korotkov
 */
public class HaxeUtil {
  public static void reparseProjectFiles(@Nonnull final Project project) {
    Task.Backgroundable task = new Task.Backgroundable(project, HaxeBundle.message("haxe.project.reparsing"), false) {
      public void run(@Nonnull ProgressIndicator indicator) {
        final Collection<VirtualFile> haxeFiles = new ArrayList<VirtualFile>();
        final VirtualFile baseDir = project.getBaseDir();
        if (baseDir != null) {
          FileBasedIndex.getInstance().iterateIndexableFiles(new ContentIterator() {
            public boolean processFile(VirtualFile file) {
              if (HaxeFileType.HAXE_FILE_TYPE == file.getFileType()) {
                haxeFiles.add(file);
              }
              return true;
            }
          }, project, indicator);
        }
        ApplicationManager.getApplication().invokeAndWait(new Runnable() {
          public void run() {
            FileContentUtilCore.reparseFiles(haxeFiles);
          }
        }, project.getApplication().getNoneModalityState());
      }
    };
    ProgressManager.getInstance().run(task);
  }
}
