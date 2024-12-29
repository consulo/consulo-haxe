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
package com.intellij.plugins.haxe.ide.refactoring;

import consulo.language.editor.PsiEquivalenceUtil;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.resolve.ResolveState;
import consulo.navigation.NavigationItem;
import consulo.codeEditor.Editor;
import com.intellij.plugins.haxe.lang.psi.*;
import com.intellij.plugins.haxe.lang.psi.impl.ComponentNameScopeProcessor;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.project.Project;
import consulo.util.collection.ContainerUtil;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.*;

/**
 * @author: Fedor.Korotkov
 */
public class HaxeRefactoringUtil {
  public static Set<String> collectUsedNames(HaxePsiCompositeElement context) {
    final Set<HaxeComponentName> usedComponentNames = new HashSet<HaxeComponentName>();
    PsiTreeUtil.treeWalkUp(new ComponentNameScopeProcessor(usedComponentNames), context, null, new ResolveState());
    return new HashSet<String>(ContainerUtil.map(usedComponentNames, NavigationItem::getName));
  }

  @Nullable
  public static HaxeExpression getSelectedExpression(@Nonnull final Project project,
                                                     @Nonnull PsiFile file,
                                                     @Nonnull final PsiElement element1,
                                                     @Nonnull final PsiElement element2) {
    PsiElement parent = PsiTreeUtil.findCommonParent(element1, element2);
    if (parent == null) {
      return null;
    }
    if (parent instanceof HaxeExpression) {
      return (HaxeExpression)parent;
    }
    return PsiTreeUtil.getParentOfType(parent, HaxeExpression.class);
  }

  @Nonnull
  public static List<PsiElement> getOccurrences(@Nonnull final PsiElement pattern, @Nullable final PsiElement context) {
    if (context == null) {
      return Collections.emptyList();
    }
    final List<PsiElement> occurrences = new ArrayList<PsiElement>();
    final HaxeVisitor visitor = new HaxeVisitor() {
      public void visitElement(@Nonnull final PsiElement element) {
        if (element instanceof HaxeParameter) {
          return;
        }
        if (PsiEquivalenceUtil.areElementsEquivalent(element, pattern)) {
          occurrences.add(element);
          return;
        }
        element.acceptChildren(this);
      }
    };
    context.acceptChildren(visitor);
    return occurrences;
  }

  @Nullable
  public static PsiElement findOccurrenceUnderCaret(List<PsiElement> occurrences, Editor editor) {
    if (occurrences.isEmpty()) {
      return null;
    }
    int offset = editor.getCaretModel().getOffset();
    for (PsiElement occurrence : occurrences) {
      if (occurrence.getTextRange().contains(offset)) {
        return occurrence;
      }
    }
    int line = editor.getDocument().getLineNumber(offset);
    for (PsiElement occurrence : occurrences) {
      if (occurrence.isValid() && editor.getDocument().getLineNumber(occurrence.getTextRange().getStartOffset()) == line) {
        return occurrence;
      }
    }
    for (PsiElement occurrence : occurrences) {
      if (occurrence.isValid()) {
        return occurrence;
      }
    }
    return null;
  }
}
