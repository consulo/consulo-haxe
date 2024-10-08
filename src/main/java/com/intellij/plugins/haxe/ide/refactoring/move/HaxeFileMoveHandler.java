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
package com.intellij.plugins.haxe.ide.refactoring.move;

import com.intellij.plugins.haxe.lang.psi.HaxeFile;
import com.intellij.plugins.haxe.lang.psi.HaxePackageStatement;
import com.intellij.plugins.haxe.util.HaxeElementGenerator;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.refactoring.move.MoveFileHandler;
import consulo.language.psi.PsiDirectory;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.language.util.IncorrectOperationException;
import consulo.module.content.DirectoryIndex;
import consulo.usage.UsageInfo;
import consulo.util.dataholder.Key;

import java.util.List;
import java.util.Map;

/**
 * @author: Fedor.Korotkov
 */
@ExtensionImpl
public class HaxeFileMoveHandler extends MoveFileHandler {
  public static final Key<String> destinationPackageKey = Key.create("haxe.destination.package.key");

  @Override
  public boolean canProcessElement(PsiFile element) {
    return element instanceof HaxeFile;
  }

  @Override
  public void prepareMovedFile(PsiFile file, PsiDirectory moveDestination, Map<PsiElement, PsiElement> oldToNewMap) {
    file.putUserData(destinationPackageKey, DirectoryIndex.getInstance(file.getProject()).getPackageName(moveDestination.getVirtualFile()));
  }

  @Override
  public List<UsageInfo> findUsages(PsiFile psiFile, PsiDirectory newParent, boolean searchInComments, boolean searchInNonJavaFiles) {
    return null;
  }

  @Override
  public void retargetUsages(List<UsageInfo> usageInfos, Map<PsiElement, PsiElement> oldToNewMap) {
  }

  @Override
  public void updateMovedFile(PsiFile file) throws IncorrectOperationException {
    final HaxeFile haxeFile = (HaxeFile) file;
    final PsiElement firstChild = haxeFile.getFirstChild();
    final HaxePackageStatement packageStatement = PsiTreeUtil.getChildOfType(haxeFile, HaxePackageStatement.class);
    final HaxePackageStatement newPackageStatement =
        HaxeElementGenerator.createPackageStatementFromPath(haxeFile.getProject(), file.getUserData(destinationPackageKey));
    assert newPackageStatement != null;
    if (packageStatement == null && firstChild == null) {
      haxeFile.add(newPackageStatement);
    } else if (packageStatement == null && firstChild != null) {
      haxeFile.addBefore(newPackageStatement, firstChild);
    } else {
      packageStatement.replace(newPackageStatement);
    }
  }
}
