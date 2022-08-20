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
package com.intellij.plugins.haxe.ide.index;

import com.intellij.plugins.haxe.lang.psi.HaxeClass;
import com.intellij.plugins.haxe.util.HaxeResolveUtil;
import consulo.annotation.component.ExtensionImpl;
import consulo.application.util.function.Processor;
import consulo.index.io.DataIndexer;
import consulo.index.io.EnumeratorStringDescriptor;
import consulo.index.io.ID;
import consulo.index.io.KeyDescriptor;
import consulo.language.psi.PsiFile;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.language.psi.stub.FileBasedIndex;
import consulo.language.psi.stub.FileContent;
import consulo.language.psi.stub.ScalarIndexExtension;
import consulo.virtualFileSystem.VirtualFile;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * @author: Fedor.Korotkov
 */
@ExtensionImpl
public class HaxeComponentFileNameIndex extends ScalarIndexExtension<String> {
  public static final ID<String, Void> HAXE_COMPONENT_FILE_NAME_INDEX = ID.create("HaxeComponentFileNameIndex");
  private static final int INDEX_VERSION = 4;
  private DataIndexer<String, Void, FileContent> myDataIndexer = new MyDataIndexer();

  @Nonnull
  @Override
  public ID<String, Void> getName() {
    return HAXE_COMPONENT_FILE_NAME_INDEX;
  }

  @Nonnull
  @Override
  public DataIndexer<String, Void, FileContent> getIndexer() {
    return myDataIndexer;
  }

  @Override
  public int getVersion() {
    return INDEX_VERSION;
  }

  @Override
  public boolean dependsOnFileContent() {
    return true;
  }

  @Override
  public KeyDescriptor<String> getKeyDescriptor() {
    return new EnumeratorStringDescriptor();
  }

  @Override
  public FileBasedIndex.InputFilter getInputFilter() {
    return HaxeSdkInputFilter.INSTANCE;
  }

  @Nonnull
  public static List<VirtualFile> getFilesNameByQName(@Nonnull String qName, @Nonnull final GlobalSearchScope filter) {
    final List<VirtualFile> result = new ArrayList<VirtualFile>();
    getFileNames(qName, new Processor<VirtualFile>() {
      @Override
      public boolean process(VirtualFile file) {
        result.add(file);
        return true;
      }
    }, filter);
    return result;
  }

  public static boolean getFileNames(@Nonnull String qName,
                                     @Nonnull Processor<VirtualFile> processor,
                                     @Nonnull final GlobalSearchScope filter) {
    return FileBasedIndex.getInstance()
        .getFilesWithKey(HAXE_COMPONENT_FILE_NAME_INDEX, Collections.<String>singleton(qName), processor, filter);
  }

  private static class MyDataIndexer implements DataIndexer<String, Void, FileContent> {
    @Override
    @Nonnull
    public Map<String, Void> map(final FileContent inputData) {
      final PsiFile psiFile = inputData.getPsiFile();
      final List<HaxeClass> classes = HaxeResolveUtil.findComponentDeclarations(psiFile);
      if (classes.isEmpty()) {
        return Collections.emptyMap();
      }
      final Map<String, Void> result = new HashMap<String, Void>(classes.size());
      for (HaxeClass haxeClass : classes) {
        final String className = haxeClass.getName();
        if (className != null) {
          result.put(haxeClass.getQualifiedName(), null);
        }
      }
      return result;
    }
  }
}
