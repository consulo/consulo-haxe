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

import com.intellij.plugins.haxe.HaxeComponentType;
import com.intellij.plugins.haxe.config.HaxeProjectSettings;
import com.intellij.plugins.haxe.ide.highlight.HaxeSyntaxHighlighterColors;
import com.intellij.plugins.haxe.ide.intention.HaxeDefineIntention;
import com.intellij.plugins.haxe.lang.lexer.HaxeTokenTypeSets;
import com.intellij.plugins.haxe.lang.lexer.HaxeTokenTypes;
import com.intellij.plugins.haxe.lang.psi.*;
import com.intellij.plugins.haxe.util.HaxeResolveUtil;
import com.intellij.plugins.haxe.util.HaxeStringUtil;
import consulo.colorScheme.TextAttributesKey;
import consulo.document.util.TextRange;
import consulo.language.ast.ASTNode;
import consulo.language.ast.IElementType;
import consulo.language.editor.annotation.Annotation;
import consulo.language.editor.annotation.AnnotationHolder;
import consulo.language.editor.annotation.Annotator;
import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.util.lang.Pair;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.Set;

/**
 * @author: Fedor.Korotkov
 */
public class HaxeColorAnnotator implements Annotator {
  @Override
  public void annotate(@Nonnull PsiElement node, @Nonnull AnnotationHolder holder) {
    if (isNewOperator(node)) {
      holder.createInfoAnnotation(node, null).setTextAttributes(HaxeSyntaxHighlighterColors.KEYWORD);
    }

    PsiElement element = node;
    if (element instanceof HaxeReference) {
      final boolean chain = PsiTreeUtil.getChildOfType(element, HaxeReference.class) != null;
      if (chain) {
        tryAnnotateQName(node, holder);
        return;
      }
      element = ((HaxeReference) element).resolve();
    }
    if (element instanceof HaxeComponentName) {
      final boolean isStatic = checkStatic(element.getParent());
      final TextAttributesKey attribute = getAttributeByType(HaxeComponentType.typeOf(element.getParent()), isStatic);
      if (attribute != null) {
        holder.createInfoAnnotation(node, null).setTextAttributes(attribute);
      }
    }
    final ASTNode astNode = node.getNode();
    if (astNode != null) {
      IElementType tt = astNode.getElementType();

      if (tt == HaxeTokenTypeSets.PPEXPRESSION) {
        annotateCompilationExpression(node, holder);
      }
    }
  }

  private static void annotateCompilationExpression(PsiElement node, AnnotationHolder holder) {
    final Set<String> definitions = HaxeProjectSettings.getInstance(node.getProject()).getUserCompilerDefinitionsAsSet();
    final String nodeText = node.getText();
    for (Pair<String, Integer> pair : HaxeStringUtil.getWordsWithOffset(nodeText)) {
      final String word = pair.getFirst();
      final int offset = pair.getSecond();
      final int absoluteOffset = node.getTextOffset() + offset;
      final TextRange range = new TextRange(absoluteOffset, absoluteOffset + word.length());
      final Annotation annotation = holder.createInfoAnnotation(range, null);
      final TextAttributesKey attributeName = definitions.contains(word) ? HaxeSyntaxHighlighterColors.DEFINED_VAR
          : HaxeSyntaxHighlighterColors.UNDEFINED_VAR;
      annotation.setTextAttributes(attributeName);
      annotation.registerFix(new HaxeDefineIntention(word, definitions.contains(word)), range);
    }
  }

  private static boolean isNewOperator(PsiElement element) {
    return HaxeTokenTypes.ONEW.toString().equals(element.getText()) &&
        element.getParent() instanceof HaxeNewExpression;
  }

  private static void tryAnnotateQName(PsiElement node, AnnotationHolder holder) {
    // Maybe this is class name
    final HaxeClass resultClass = HaxeResolveUtil.tryResolveClassByQName(node);
    if (resultClass != null) {
      final TextAttributesKey attribute = getAttributeByType(HaxeComponentType.typeOf(resultClass), false);
      if (attribute != null) {
        holder.createInfoAnnotation(node, null).setTextAttributes(attribute);
      }
    }
  }

  private static boolean checkStatic(PsiElement parent) {
    if (parent instanceof HaxeNamedComponent) {
      return ((HaxeNamedComponent) parent).isStatic();
    }
    return false;
  }

  @Nullable
  private static TextAttributesKey getAttributeByType(@Nullable HaxeComponentType type, boolean isStatic) {
    if (type == null) {
      return null;
    }
    switch (type) {
      case CLASS:
      case ENUM:
      case TYPEDEF:
        return HaxeSyntaxHighlighterColors.CLASS;
      case INTERFACE:
        return HaxeSyntaxHighlighterColors.INTERFACE;
      case PARAMETER:
        return HaxeSyntaxHighlighterColors.PARAMETER;
      case VARIABLE:
        return HaxeSyntaxHighlighterColors.LOCAL_VARIABLE;
      case FIELD:
        if (isStatic) return HaxeSyntaxHighlighterColors.STATIC_MEMBER_VARIABLE;
        return HaxeSyntaxHighlighterColors.INSTANCE_MEMBER_VARIABLE;
      case METHOD:
        if (isStatic) return HaxeSyntaxHighlighterColors.STATIC_MEMBER_FUNCTION;
        return HaxeSyntaxHighlighterColors.INSTANCE_MEMBER_FUNCTION;
      default:
        return null;
    }
  }
}
