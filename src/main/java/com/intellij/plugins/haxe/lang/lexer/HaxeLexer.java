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
package com.intellij.plugins.haxe.lang.lexer;

import consulo.application.ApplicationManager;
import consulo.language.ast.IElementType;
import consulo.language.ast.TokenSet;
import consulo.language.lexer.Lexer;
import consulo.language.lexer.LexerPosition;
import consulo.language.lexer.LookAheadLexer;
import com.intellij.plugins.haxe.config.HaxeProjectSettings;
import consulo.language.lexer.MergingLexerAdapter;
import consulo.project.Project;
import consulo.util.dataholder.Key;

import jakarta.annotation.Nullable;
import java.util.*;

import static com.intellij.plugins.haxe.lang.lexer.HaxeTokenTypeSets.*;
import static com.intellij.plugins.haxe.lang.lexer.HaxeTokenTypes.*;

public class HaxeLexer extends LookAheadLexer {
  public static Key<Object> DEFINES_KEY = Key.create("haxe.test.defines");
  private static final TokenSet tokensToMerge = TokenSet.create(
    MSL_COMMENT,
    MML_COMMENT,
    WSNLS
  );

  private final static Set<String> SDK_DEFINES = new HashSet<String>(Arrays.asList(
    "macro"
  ));

  @Nullable
  private Project myProject;

  public HaxeLexer() {
    super(new MergingLexerAdapter(new _HaxeLexer(), tokensToMerge));
    myProject = null;
  }

  @Override
  protected void lookAhead(Lexer baseLexer) {
    if (baseLexer.getTokenType() == PPERROR) {
      final LexerPosition position = baseLexer.getCurrentPosition();
      baseLexer.advance();
      while (HaxeTokenTypeSets.WHITESPACES.contains(baseLexer.getTokenType()) ||
             HaxeTokenTypeSets.COMMENTS.contains(baseLexer.getTokenType())) {
        baseLexer.advance();
      }
      if (HaxeTokenTypeSets.STRINGS.contains(baseLexer.getTokenType())) {
        while (HaxeTokenTypeSets.STRINGS.contains(baseLexer.getTokenType())) {
          baseLexer.advance();
        }
      }
      else {
        baseLexer.restore(position);
      }
      advanceAs(baseLexer, PPERROR);
    }
    else if (baseLexer.getTokenType() == PPIF || baseLexer.getTokenType() == PPELSEIF) {
      advanceAs(baseLexer, PPIF);
      while (!lookAheadExpressionIsTrue(baseLexer)) {
        IElementType elementType = eatUntil(baseLexer, PPEND, PPELSE, PPELSEIF);
        if (elementType == PPELSEIF) {
          advanceAs(baseLexer, PPBODY);
          continue;
        }
        advanceAs(baseLexer, PPBODY);
        break;
      }
    }
    else if (baseLexer.getTokenType() == PPELSE) {
      eatUntil(baseLexer, PPEND);
      advanceAs(baseLexer, PPELSE);
    }
    else {
      super.lookAhead(baseLexer);
    }
  }

  @Nullable
  protected static IElementType eatUntil(Lexer baseLexer, IElementType... types) {
    final Set<IElementType> typeSet = new HashSet<IElementType>(Arrays.asList(types));
    IElementType type = null;
    int counter = 0;
    do {
      baseLexer.advance();
      type = baseLexer.getTokenType();
      if (type == PPIF) {
        ++counter;
      }
      if (counter > 0 && type == PPEND) {
        --counter;
        baseLexer.advance();
        type = baseLexer.getTokenType();
      }
    }
    while (type != null && (!typeSet.contains(type) || counter > 0));
    return type;
  }

  protected boolean lookAheadExpressionIsTrue(Lexer baseLexer) {
    IElementType type = null;
    // reverse polish notation
    final LinkedList<IElementType> stack = new LinkedList<IElementType>();
    final LinkedList<String> rpn = new LinkedList<String>();
    final int expressionStartPosition = baseLexer.getTokenStart();
    do {
      final LexerPosition position = baseLexer.getCurrentPosition();
      type = baseLexer.getTokenType();
      while (HaxeTokenTypeSets.WHITESPACES.contains(type) || HaxeTokenTypeSets.ONLY_COMMENTS.contains(type)) {
        baseLexer.advance();
        type = baseLexer.getTokenType();
      }
      final String tokenText = baseLexer.getTokenText();
      if (type == ID) {
        if (canCalculate(rpn, stack)) {
          //revert
          baseLexer.restore(position);
          break;
        }
        rpn.addFirst(tokenText);
      }
      else if (type == PRPAREN) {
        do {
          IElementType typeOnStack = stack.pollLast();
          if (typeOnStack == PLPAREN) {
            break;
          }
          rpn.addFirst(typeOnStack.toString());
        }
        while (!stack.isEmpty());
        while (!stack.isEmpty() && stack.getLast() == ONOT) {
          rpn.addFirst(stack.pollLast().toString());
        }
      }
      else if (type == OCOND_AND || type == OCOND_OR) {
        while (!stack.isEmpty() && (stack.getLast() == OCOND_AND || stack.getLast() == OCOND_OR)) {
          rpn.addFirst(stack.pollLast().toString());
        }
        stack.add(type);
      }
      else if (type == ONOT || type == PLPAREN) {
        stack.add(type);
      }
      else {
        baseLexer.restore(position);
        break;
      }
    }
    while (advanceAndContinue(baseLexer));
    // not empty token
    if (expressionStartPosition != baseLexer.getTokenStart()) {
      addToken(PPEXPRESSION);
    }
    try {
      return calculate(rpn, stack);
    }
    catch (CalculationException e) {
      return false;
    }
  }

  private static boolean advanceAndContinue(Lexer baseLexer) {
    baseLexer.advance();
    return true;
  }

  private boolean canCalculate(LinkedList<String> rpn, LinkedList<IElementType> stack) {
    try {
      calculate(rpn, stack);
      return true;
    }
    catch (CalculationException e) {
      return false;
    }
  }

  private boolean calculate(LinkedList<String> rpn, LinkedList<IElementType> stack) throws CalculationException {
    final LinkedList<String> list = new LinkedList<String>();
    for (IElementType type : stack) {
      if (type == PLPAREN) {
        throw new CalculationException("Balance error");
      }
      list.add(type.toString());
    }
    Collections.reverse(list);
    list.addAll(rpn);
    return calculateImpl(list);
  }

  private boolean calculateImpl(LinkedList<String> list) throws CalculationException {
    if (list.isEmpty()) {
      throw new CalculationException("Incorrect expression");
    }
    final String token = list.pollFirst();
    if ("!".equals(token)) {
      return !calculateImpl(list);
    }
    else if ("&&".equals(token)) {
      boolean a = calculateImpl(list);
      boolean b = calculateImpl(list);
      return a && b;
    }
    else if ("||".equals(token)) {
      boolean a = calculateImpl(list);
      boolean b = calculateImpl(list);
      return a || b;
    }
    else {
      return isDefined(token);
    }
  }

  protected boolean isDefined(String name) {
    if (name == null) {
      return false;
    }
    if (myProject == null) {
      return SDK_DEFINES.contains(name);
    }
    String[] definitions = null;
    if (ApplicationManager.getApplication().isUnitTestMode()) {
      final Object userData = myProject.getUserData(DEFINES_KEY);
      if (userData instanceof String) {
        definitions = ((String)userData).split(",");
      }
    }
    else {
      definitions = HaxeProjectSettings.getInstance(myProject).getCompilerDefinitions();
    }
    return definitions != null && Arrays.asList(definitions).contains(name);
  }

  private static class CalculationException extends Exception {
    private CalculationException(String message) {
      super(message);
    }
  }
}