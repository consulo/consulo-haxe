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
package com.intellij.plugins.haxe.lang.psi.impl;

import com.intellij.plugins.haxe.lang.psi.HaxeRegularExpression;
import com.intellij.plugins.haxe.lang.psi.HaxeVarDeclarationPart;
import com.intellij.plugins.haxe.lang.psi.HaxeVarInit;
import com.intellij.plugins.haxe.util.HaxeElementGenerator;
import consulo.document.util.TextRange;
import consulo.language.ast.ASTNode;
import consulo.language.psi.LiteralTextEscaper;
import consulo.language.psi.PsiLanguageInjectionHost;
import org.intellij.lang.regexp.DefaultRegExpPropertiesProvider;
import org.intellij.lang.regexp.psi.RegExpChar;
import org.intellij.lang.regexp.psi.RegExpGroup;
import org.intellij.lang.regexp.psi.RegExpNamedGroupRef;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Fedor.Korotkov
 */
public class HaxeRegularExpressionImpl extends HaxeReferenceImpl implements HaxeRegularExpression {
  private final DefaultRegExpPropertiesProvider myPropertiesProvider;

  public HaxeRegularExpressionImpl(@Nonnull ASTNode node) {
    super(node);
    myPropertiesProvider = DefaultRegExpPropertiesProvider.getInstance();
  }

  @Override
  public boolean isValidHost() {
    return true;
  }

  @Override
  public PsiLanguageInjectionHost updateText(@Nonnull String text) {
    ASTNode node = getNode();
    ASTNode parent = node.getTreeParent();
    final HaxeVarDeclarationPart varDeclarationPart = HaxeElementGenerator.createVarDeclarationPart(getProject(), "a=" + text);
    final HaxeVarInit varInit = varDeclarationPart.getVarInit();
    final ASTNode outerNode = varInit == null ? null : varInit.getNode();
    assert outerNode != null;
    parent.replaceChild(node, outerNode);

    return (PsiLanguageInjectionHost) outerNode.getPsi();
  }

  @Nonnull
  @Override
  public LiteralTextEscaper<? extends PsiLanguageInjectionHost> createLiteralTextEscaper() {
    return new LiteralTextEscaper<HaxeRegularExpression>(this) {
      @Override
      public boolean decode(@Nonnull TextRange rangeInsideHost, @Nonnull StringBuilder outChars) {
        outChars.append(myHost.getText(), rangeInsideHost.getStartOffset(), rangeInsideHost.getEndOffset());
        return true;
      }

      @Override
      public int getOffsetInHost(int offsetInDecoded, @Nonnull TextRange rangeInsideHost) {
        int offset = offsetInDecoded + rangeInsideHost.getStartOffset();
        if (offset < rangeInsideHost.getStartOffset()) {
          offset = rangeInsideHost.getStartOffset();
        }
        if (offset > rangeInsideHost.getEndOffset()) {
          offset = rangeInsideHost.getEndOffset();
        }
        return offset;
      }

      @Override
      public boolean isOneLine() {
        return true;
      }
    };
  }

  @Nonnull
  @Override
  public Class getHostClass() {
    return getClass();
  }

  @Override
  public boolean characterNeedsEscaping(char c) {
    return false;
  }

  @Override
  public boolean supportsPerl5EmbeddedComments() {
    return false;
  }

  @Override
  public boolean supportsPossessiveQuantifiers() {
    return false;
  }

  @Override
  public boolean supportsPythonConditionalRefs() {
    return false;
  }

  @Override
  public boolean supportsNamedGroupSyntax(RegExpGroup group) {
    return false;
  }

  @Override
  public boolean supportsNamedGroupRefSyntax(RegExpNamedGroupRef regExpNamedGroupRef) {
    return false;
  }

  @Override
  public boolean supportsExtendedHexCharacter(RegExpChar regExpChar) {
    return false;
  }

  @Override
  public boolean isValidCategory(@Nonnull String category) {
    return myPropertiesProvider.isValidCategory(category);
  }

  @Nonnull
  @Override
  public String[][] getAllKnownProperties() {
    return myPropertiesProvider.getAllKnownProperties();
  }

  @Nullable
  @Override
  public String getPropertyDescription(@Nullable String name) {
    return myPropertiesProvider.getPropertyDescription(name);
  }

  @Nonnull
  @Override
  public String[][] getKnownCharacterClasses() {
    return myPropertiesProvider.getKnownCharacterClasses();
  }
}
