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
package com.intellij.plugins.haxe.ide;

import consulo.annotation.component.ExtensionImpl;
import consulo.util.lang.Pair;
import com.intellij.plugins.haxe.ide.index.HaxeComponentIndex;
import com.intellij.plugins.haxe.lang.psi.HaxeClass;
import com.intellij.plugins.haxe.util.HaxeResolveUtil;
import consulo.language.psi.PsiElement;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.language.editor.testIntegration.TestFinder;

import jakarta.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/**
 * @author: Fedor.Korotkov
 */
@ExtensionImpl
public class HaxeTestFinder implements TestFinder {
  @Override
  public HaxeClass findSourceElement(@Nonnull PsiElement from) {
    return PsiTreeUtil.getParentOfType(from, HaxeClass.class);
  }

  @Nonnull
  @Override
  public Collection<PsiElement> findTestsForClass(@Nonnull PsiElement element) {
    final HaxeClass haxeClass = findSourceElement(element);
    if (haxeClass == null) {
      return Collections.emptyList();
    }
    final Collection<PsiElement> result = new HashSet<PsiElement>();
    final Pair<String, String> packageAndName = HaxeResolveUtil.splitQName(haxeClass.getQualifiedName());
    final GlobalSearchScope searchScope = GlobalSearchScope.projectScope(element.getProject());
    result.addAll(HaxeComponentIndex.getItemsByName(packageAndName.getSecond() + "Test", element.getProject(), searchScope));
    result.addAll(HaxeComponentIndex.getItemsByName("Test" + packageAndName.getSecond(), element.getProject(), searchScope));
    return result;
  }

  @Nonnull
  @Override
  public Collection<PsiElement> findClassesForTest(@Nonnull PsiElement element) {
    final HaxeClass haxeClass = findSourceElement(element);
    if (haxeClass == null) {
      return Collections.emptyList();
    }
    final Collection<PsiElement> result = new HashSet<PsiElement>();
    final Pair<String, String> packageAndName = HaxeResolveUtil.splitQName(haxeClass.getQualifiedName());
    final GlobalSearchScope searchScope = GlobalSearchScope.projectScope(element.getProject());
    final String className = packageAndName.getSecond();
    if (className.startsWith("Test")) {
      final String name = className.substring("Test".length());
      result.addAll(HaxeComponentIndex.getItemsByName(name, element.getProject(), searchScope));
    }
    if (className.endsWith("Test")) {
      final String name = className.substring(0, className.length() - "Test".length());
      result.addAll(HaxeComponentIndex.getItemsByName(name, element.getProject(), searchScope));
    }
    return result;
  }

  @Override
  public boolean isTest(@Nonnull PsiElement element) {
    final HaxeClass haxeClass = findSourceElement(element);
    if (haxeClass == null) {
      return false;
    }
    final String className = haxeClass.getName();
    return className != null && (className.startsWith("Test") || className.endsWith("Test"));
  }
}
