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
package com.intellij.plugins.haxe.ide.highlight;

import static com.intellij.plugins.haxe.lang.lexer.HaxeTokenTypeSets.BAD_TOKENS;
import static com.intellij.plugins.haxe.lang.lexer.HaxeTokenTypeSets.CONDITIONALLY_NOT_COMPILED;
import static com.intellij.plugins.haxe.lang.lexer.HaxeTokenTypeSets.DOC_COMMENT;
import static com.intellij.plugins.haxe.lang.lexer.HaxeTokenTypeSets.KEYWORDS;
import static com.intellij.plugins.haxe.lang.lexer.HaxeTokenTypeSets.MML_COMMENT;
import static com.intellij.plugins.haxe.lang.lexer.HaxeTokenTypeSets.MSL_COMMENT;
import static com.intellij.plugins.haxe.lang.lexer.HaxeTokenTypeSets.OPERATORS;
import static com.intellij.plugins.haxe.lang.lexer.HaxeTokenTypes.*;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.plugins.haxe.lang.lexer.HaxeLexer;
import com.intellij.psi.tree.IElementType;

public class HaxeSyntaxHighlighter extends SyntaxHighlighterBase {
  private static final Map<IElementType, TextAttributesKey> ATTRIBUTES = new HashMap<IElementType, TextAttributesKey>();

  static {
    safeMap(ATTRIBUTES, KEYWORDS, HaxeSyntaxHighlighterColors.KEYWORD);
    safeMap(ATTRIBUTES, OPERATORS, HaxeSyntaxHighlighterColors.OPERATION_SIGN);

    ATTRIBUTES.put(LITINT, HaxeSyntaxHighlighterColors.NUMBER);
    ATTRIBUTES.put(LITHEX, HaxeSyntaxHighlighterColors.NUMBER);
    ATTRIBUTES.put(LITOCT, HaxeSyntaxHighlighterColors.NUMBER);
    ATTRIBUTES.put(KFALSE, HaxeSyntaxHighlighterColors.NUMBER);
    ATTRIBUTES.put(KTRUE, HaxeSyntaxHighlighterColors.NUMBER);
    ATTRIBUTES.put(LITFLOAT, HaxeSyntaxHighlighterColors.NUMBER);

    ATTRIBUTES.put(OPEN_QUOTE, HaxeSyntaxHighlighterColors.STRING);
    ATTRIBUTES.put(CLOSING_QUOTE, HaxeSyntaxHighlighterColors.STRING);
    ATTRIBUTES.put(REGULAR_STRING_PART, HaxeSyntaxHighlighterColors.STRING);

    ATTRIBUTES.put(PLPAREN, HaxeSyntaxHighlighterColors.PARENTHS);
    ATTRIBUTES.put(PRPAREN, HaxeSyntaxHighlighterColors.PARENTHS);

    ATTRIBUTES.put(PLCURLY, HaxeSyntaxHighlighterColors.BRACES);
    ATTRIBUTES.put(PRCURLY, HaxeSyntaxHighlighterColors.BRACES);

    ATTRIBUTES.put(PLBRACK, HaxeSyntaxHighlighterColors.BRACKETS);
    ATTRIBUTES.put(PRBRACK, HaxeSyntaxHighlighterColors.BRACKETS);

    ATTRIBUTES.put(OCOMMA, HaxeSyntaxHighlighterColors.COMMA);
    ATTRIBUTES.put(ODOT, HaxeSyntaxHighlighterColors.DOT);
    ATTRIBUTES.put(OSEMI, HaxeSyntaxHighlighterColors.SEMICOLON);

    ATTRIBUTES.put(MML_COMMENT, HaxeSyntaxHighlighterColors.BLOCK_COMMENT);
    ATTRIBUTES.put(MSL_COMMENT, HaxeSyntaxHighlighterColors.LINE_COMMENT);
    ATTRIBUTES.put(DOC_COMMENT, HaxeSyntaxHighlighterColors.DOC_COMMENT);

    fillMap(ATTRIBUTES, BAD_TOKENS, HaxeSyntaxHighlighterColors.BAD_CHARACTER);
	fillMap(ATTRIBUTES, CONDITIONALLY_NOT_COMPILED, HaxeSyntaxHighlighterColors.CONDITIONALLY_NOT_COMPILED);
  }

  @Nonnull
  public Lexer getHighlightingLexer() {
    return new HaxeLexer();
  }

  @Nonnull
  public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
    return pack(ATTRIBUTES.get(tokenType));
  }
}

