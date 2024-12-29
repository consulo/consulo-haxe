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
package com.intellij.plugins.haxe.lang.parser;

import com.intellij.plugins.haxe.HaxeLanguage;
import com.intellij.plugins.haxe.lang.lexer.HaxeLexer;
import com.intellij.plugins.haxe.lang.lexer.HaxeTokenTypeSets;
import com.intellij.plugins.haxe.lang.lexer.HaxeTokenTypes;
import com.intellij.plugins.haxe.lang.psi.HaxeFile;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.ast.ASTNode;
import consulo.language.ast.IFileElementType;
import consulo.language.ast.TokenSet;
import consulo.language.file.FileViewProvider;
import consulo.language.lexer.Lexer;
import consulo.language.parser.ParserDefinition;
import consulo.language.parser.PsiParser;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.version.LanguageVersion;

import jakarta.annotation.Nonnull;

@ExtensionImpl
public class HaxeParserDefinition implements ParserDefinition
{
  @Nonnull
  @Override
  public Language getLanguage() {
    return HaxeLanguage.INSTANCE;
  }

  @Nonnull
  public Lexer createLexer(LanguageVersion languageVersion) {
    return new HaxeLexer();
  }

  public PsiParser createParser(LanguageVersion languageVersion) {
    return new HaxeParser();
  }

  public IFileElementType getFileNodeType() {
    return HaxeTokenTypeSets.HAXE_FILE;
  }

  @Nonnull
  public TokenSet getWhitespaceTokens(LanguageVersion languageVersion) {
    return HaxeTokenTypeSets.WHITESPACES;
  }

  @Nonnull
  public TokenSet getCommentTokens(LanguageVersion languageVersion) {
    return HaxeTokenTypeSets.COMMENTS;
  }

  @Nonnull
  public TokenSet getStringLiteralElements(LanguageVersion languageVersion) {
    return HaxeTokenTypeSets.STRINGS;
  }

  @Nonnull
  public PsiElement createElement(ASTNode node) {
    return HaxeTokenTypes.Factory.createElement(node);
  }

  public PsiFile createFile(FileViewProvider viewProvider) {
    return new HaxeFile(viewProvider);
  }

  public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right) {
    return SpaceRequirements.MAY;
  }
}
