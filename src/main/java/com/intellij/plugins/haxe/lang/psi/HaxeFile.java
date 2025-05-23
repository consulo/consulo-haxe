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
package com.intellij.plugins.haxe.lang.psi;

import com.intellij.plugins.haxe.HaxeLanguage;
import com.intellij.plugins.haxe.util.HaxeResolveUtil;
import consulo.language.file.FileViewProvider;
import consulo.language.impl.psi.PsiFileBase;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiReference;
import consulo.language.util.IncorrectOperationException;
import consulo.util.io.FileUtil;

import jakarta.annotation.Nonnull;

public class HaxeFile extends PsiFileBase {
  public HaxeFile(@Nonnull FileViewProvider viewProvider) {
    super(viewProvider, HaxeLanguage.INSTANCE);
  }

  @Override
  public String toString() {
    return "haXe File";
  }

  @Override
  public PsiReference findReferenceAt(int offset) {
    return super.findReferenceAt(offset);
  }

  @Override
  public PsiElement setName(@Nonnull String newName) throws IncorrectOperationException {
    final String oldName = FileUtil.getNameWithoutExtension(getName());
    final PsiElement result = super.setName(newName);
    final HaxeClass haxeClass = HaxeResolveUtil.findComponentDeclaration(this, oldName);
    if (haxeClass != null) {
      haxeClass.setName(FileUtil.getNameWithoutExtension(newName));
    }
    return result;
  }
}
