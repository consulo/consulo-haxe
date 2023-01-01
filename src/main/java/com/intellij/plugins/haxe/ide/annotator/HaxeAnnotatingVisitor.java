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
package com.intellij.plugins.haxe.ide.annotator;

import com.intellij.plugins.haxe.HaxeBundle;
import com.intellij.plugins.haxe.lang.lexer.HaxeTokenTypes;
import com.intellij.plugins.haxe.lang.psi.*;
import com.intellij.plugins.haxe.util.HaxeResolveUtil;
import consulo.language.psi.PsiElement;
import consulo.language.editor.annotation.AnnotationHolder;
import consulo.language.editor.annotation.Annotator;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author: Fedor.Korotkov
 */
public class HaxeAnnotatingVisitor extends HaxeVisitor implements Annotator
{
  private static final Set<String> BUILTIN = new HashSet<String>(Arrays.asList(
    "trace", "__call__", "__vmem_set__", "__vmem_get__", "__vmem_sign__", "__global__", "_global", "__foreach__"
  ));
  private AnnotationHolder myHolder = null;

  @Override
  public void annotate(@Nonnull PsiElement element, @Nonnull AnnotationHolder holder) {
    assert myHolder == null;
    myHolder = holder;
    try {
      element.accept(this);
    }
    finally {
      myHolder = null;
    }
  }

  @Override
  public void visitReferenceExpression(@Nonnull HaxeReferenceExpression reference) {
    if (reference.getTokenType() != HaxeTokenTypes.REFERENCE_EXPRESSION) {
      return; // call, array access, this, literal, etc
    }
    final HaxeReference leftSiblingReference = HaxeResolveUtil.getLeftReference(reference);
    final PsiElement referenceTarget = reference.resolve();
    if (referenceTarget != null) {
      return; // OK
    }

    if (BUILTIN.contains(reference.getText()) &&
        reference.getParent() instanceof HaxeCallExpression &&
        !(reference.getParent().getParent() instanceof HaxeReference)) {
      return;
    }

    if (!(reference.getParent() instanceof HaxeReference) && !(reference.getParent() instanceof HaxePackageStatement)) {
      // whole reference expression
      myHolder.createErrorAnnotation(reference, HaxeBundle.message("cannot.resolve.reference"));
    }
    final PsiElement leftSiblingReferenceTarget = leftSiblingReference == null ? null : leftSiblingReference.resolve();
    if (leftSiblingReference != null && leftSiblingReferenceTarget == null) {
      return; // already bad
    }

    // check all parents (ex. com.reference.Bar)
    PsiElement parent = reference.getParent();
    while (parent instanceof HaxeReference) {
      if (((HaxeReference)parent).resolve() != null) return;
      parent = parent.getParent();
    }

    if (parent instanceof HaxePackageStatement) {
      return; // package
    }

    myHolder.createErrorAnnotation(reference, HaxeBundle.message("cannot.resolve.reference"));
  }
}
