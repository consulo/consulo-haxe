/*
 * Copyright 2000-2013 JetBrains s.r.o.
 * Copyright 2014-2014 AS3Boyan
 * Copyright 2014-2014 Elias Ku
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

import com.intellij.plugins.haxe.HaxeFileType;
import com.intellij.plugins.haxe.lang.psi.*;
import com.intellij.plugins.haxe.lang.psi.impl.HaxeExpressionCodeFragmentImpl;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiFileFactory;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.project.Project;

import jakarta.annotation.Nullable;
import java.util.List;

/**
 * @author Fedor.Korotkov
 */
public class HaxeElementGenerator {

  public static PsiElement createExpressionFromText(Project myProject, String text) {
    PsiElement fromText = createStatementFromText(myProject, "var test = " + text + ";");
    if (fromText instanceof HaxeVarDeclaration) {
      List<HaxeVarDeclarationPart> partList = ((HaxeVarDeclaration) fromText).getVarDeclarationPartList();
      HaxeVarDeclarationPart declarationPart = partList.isEmpty() ? null : partList.iterator().next();
      HaxeVarInit varInit = declarationPart != null ? declarationPart.getVarInit() : null;
      return varInit != null ? varInit.getExpression() : null;
    }
    return null;
  }

  public static PsiElement createStatementFromText(Project myProject, String text) {
    final PsiFile dummyFile = createDummyFile(myProject, HaxeCodeGenerateUtil.wrapStatement(text).getFirst());
    final HaxeClass haxeClass = PsiTreeUtil.getChildOfType(dummyFile, HaxeClass.class);
    assert haxeClass != null;
    final HaxeFunctionDeclarationWithAttributes mainMethod = (HaxeFunctionDeclarationWithAttributes) haxeClass.getMethods().iterator().next();
    final HaxeBlockStatement statement = mainMethod.getBlockStatement();
    assert statement != null;
    return statement.getChildren()[0];
  }

  public static HaxeVarDeclarationPart createVarDeclarationPart(Project myProject, String text) {
    final PsiFile dummyFile = createDummyFile(myProject, HaxeCodeGenerateUtil.wrapFunction(text).getFirst());
    final HaxeClass haxeClass = PsiTreeUtil.getChildOfType(dummyFile, HaxeClass.class);
    assert haxeClass != null;
    return (HaxeVarDeclarationPart) haxeClass.getFields().iterator().next();
  }

  public static HaxeVarDeclaration createVarDeclaration(Project myProject, String text) {
    final PsiFile dummyFile = createDummyFile(myProject, HaxeCodeGenerateUtil.wrapFunction(text).getFirst());
    final HaxeClass haxeClass = PsiTreeUtil.getChildOfType(dummyFile, HaxeClass.class);
    assert haxeClass != null;
    String haxeClassText = haxeClass.getText();
    return (HaxeVarDeclaration) haxeClass.getVarDeclarations().iterator().next();
  }

  public static List<HaxeNamedComponent> createNamedSubComponentsFromText(Project myProject, String text) {
    final PsiFile dummyFile = createDummyFile(myProject, HaxeCodeGenerateUtil.wrapFunction(text).getFirst());
    final HaxeClass haxeClass = PsiTreeUtil.getChildOfType(dummyFile, HaxeClass.class);
    assert haxeClass != null;
    return HaxeResolveUtil.findNamedSubComponents(haxeClass);
  }

  @Nullable
  public static HaxeIdentifier createIdentifierFromText(Project myProject, String name) {
    return createImportAndFindChild(myProject, name, HaxeIdentifier.class);
  }

  @Nullable
  public static HaxeReference createReferenceFromText(Project myProject, String name) {
    return createImportAndFindChild(myProject, name, HaxeReference.class);
  }

  @Nullable
  private static <T extends PsiElement> T createImportAndFindChild(Project myProject, String name, Class<T> aClass) {
    final HaxeImportStatementRegular importStatement = createImportStatementFromPath(myProject, name);
    if (importStatement == null) {
      return null;
    }
    return PsiTreeUtil.findChildOfType(importStatement, aClass);
  }

  @Nullable
  public static HaxeImportStatementRegular createImportStatementFromPath(Project myProject, String path) {
    final PsiFile dummyFile = createDummyFile(myProject, "import " + path + ";");
    return PsiTreeUtil.getChildOfType(dummyFile, HaxeImportStatementRegular.class);
  }

  @Nullable
  public static HaxePackageStatement createPackageStatementFromPath(Project myProject, String path) {
    final PsiFile dummyFile = createDummyFile(myProject, "package " + path + ";");
    return PsiTreeUtil.getChildOfType(dummyFile, HaxePackageStatement.class);
  }

  public static PsiFile createDummyFile(Project myProject, String text) {
    final PsiFileFactory factory = PsiFileFactory.getInstance(myProject);
    final String name = "dummy." + HaxeFileType.HAXE_FILE_TYPE.getDefaultExtension();
    final PsiFile psiFile = factory.createFileFromText(name,HaxeFileType.HAXE_FILE_TYPE, text);
    return psiFile;
  }

  public static PsiFile createExpressionCodeFragment(Project myProject, String text, PsiElement context, boolean resolveScope) {
    final String name = "dummy." + HaxeFileType.HAXE_FILE_TYPE.getDefaultExtension();
    HaxeExpressionCodeFragmentImpl codeFragment = new HaxeExpressionCodeFragmentImpl(myProject, name, text, true);
    codeFragment.setContext(context);
    return codeFragment;
  }
}
