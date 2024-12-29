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

import com.intellij.plugins.haxe.HaxeLanguage;
import com.intellij.plugins.haxe.lang.lexer.HaxeTokenTypeSets;
import com.intellij.plugins.haxe.lang.lexer.HaxeTokenTypes;
import com.intellij.plugins.haxe.lang.psi.*;
import com.intellij.plugins.haxe.util.HaxeCodeGenerateUtil;
import com.intellij.plugins.haxe.util.UsefulPsiTreeUtil;
import consulo.annotation.component.ExtensionImpl;
import consulo.document.util.TextRange;
import consulo.language.Language;
import consulo.language.ast.IElementType;
import consulo.language.editor.completion.*;
import consulo.language.editor.completion.lookup.LookupElementBuilder;
import consulo.language.impl.ast.TreeUtil;
import consulo.language.impl.parser.GeneratedParserUtilBase;
import consulo.language.pattern.PsiElementPattern;
import consulo.language.pattern.StandardPatterns;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiErrorElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiFileFactory;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.language.util.ProcessingContext;
import consulo.util.lang.Pair;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.*;

import static consulo.language.pattern.PlatformPatterns.psiElement;

/**
 * @author: Fedor.Korotkov
 */
@ExtensionImpl
public class HaxeKeywordCompletionContributor extends CompletionContributor {
  private static final Set<String> allowedKeywords = new HashSet<String>() {
    {
      for (IElementType elementType : HaxeTokenTypeSets.KEYWORDS.getTypes()) {
        add(elementType.toString());
      }
      add(HaxeTokenTypes.ONEW.toString());
    }
  };

  public HaxeKeywordCompletionContributor() {
    final PsiElementPattern.Capture<PsiElement> idInExpression =
        psiElement().withSuperParent(1, HaxeIdentifier.class).withSuperParent(2, HaxeReference.class);
    final PsiElementPattern.Capture<PsiElement> inComplexExpression = psiElement().withSuperParent(3, HaxeReference.class);

    final PsiElementPattern.Capture<PsiElement> inheritPattern =
        psiElement().inFile(StandardPatterns.instanceOf(HaxeFile.class)).withSuperParent(1, PsiErrorElement.class).
            and(psiElement().withSuperParent(2, HaxeInheritList.class));
    extend(CompletionType.BASIC,
        psiElement().andOr(psiElement().withSuperParent(1, PsiErrorElement.class),
            psiElement().withSuperParent(1, GeneratedParserUtilBase.DummyBlock.class)).
            andOr(psiElement().withSuperParent(2, HaxeClassBody.class), psiElement().withSuperParent(2, HaxeInheritList.class)),
        new CompletionProvider() {
          @Override
          public void addCompletions(@Nonnull CompletionParameters parameters,
                                     ProcessingContext context,
                                     @Nonnull CompletionResultSet result) {
            result.addElement(LookupElementBuilder.create("extends"));
            result.addElement(LookupElementBuilder.create("implements"));
          }
        });
    // foo.b<caret> - bad
    // i<caret> - good
    extend(CompletionType.BASIC,
        psiElement().inFile(StandardPatterns.instanceOf(HaxeFile.class)).andNot(idInExpression.and(inComplexExpression))
            .andNot(inheritPattern),
        new CompletionProvider() {
          @Override
          public void addCompletions(@Nonnull CompletionParameters parameters,
                                     ProcessingContext context,
                                     @Nonnull CompletionResultSet result) {
            final Collection<String> suggestedKeywords = suggestKeywords(parameters.getPosition());
            suggestedKeywords.retainAll(allowedKeywords);
            for (String keyword : suggestedKeywords) {
              result.addElement(LookupElementBuilder.create(keyword));
            }
          }
        });
  }

  private static Collection<String> suggestKeywords(PsiElement position) {
    final TextRange posRange = position.getTextRange();
    final HaxeFile posFile = (HaxeFile) position.getContainingFile();

    final List<PsiElement> pathToBlockStatement = UsefulPsiTreeUtil.getPathToParentOfType(position, HaxeBlockStatement.class);

    final HaxePsiCompositeElement classInterfaceEnum =
        PsiTreeUtil.getParentOfType(position, HaxeClassBody.class, HaxeInterfaceBody.class, HaxeEnumBody.class);

    final String text;
    final int offset;
    if (pathToBlockStatement != null) {
      final Pair<String, Integer> pair = HaxeCodeGenerateUtil.wrapStatement(posRange.substring(posFile.getText()));
      text = pair.getFirst();
      offset = pair.getSecond();
    } else if (classInterfaceEnum != null) {
      final Pair<String, Integer> pair = HaxeCodeGenerateUtil.wrapFunction(posRange.substring(posFile.getText()));
      text = pair.getFirst();
      offset = pair.getSecond();
    } else {
      text = posFile.getText().substring(0, posRange.getStartOffset());
      offset = 0;
    }

    final List<String> result = new ArrayList<String>();
    if (pathToBlockStatement != null && pathToBlockStatement.size() > 1) {
      final PsiElement blockChild = pathToBlockStatement.get(pathToBlockStatement.size() - 2);
      result.addAll(suggestBySibling(UsefulPsiTreeUtil.getPrevSiblingSkipWhiteSpacesAndComments(blockChild, true)));
    }

    PsiFile file = PsiFileFactory.getInstance(posFile.getProject()).createFileFromText("a.hx", HaxeLanguage.INSTANCE, text, true, false);
    GeneratedParserUtilBase.CompletionState state = new GeneratedParserUtilBase.CompletionState(text.length() - offset);
    file.putUserData(GeneratedParserUtilBase.COMPLETION_STATE_KEY, state);
    TreeUtil.ensureParsed(file.getNode());
    result.addAll(state.items);

    // always
    result.add(HaxeTokenTypes.PPIF.toString());
    result.add(HaxeTokenTypes.PPELSE.toString());
    result.add(HaxeTokenTypes.PPELSEIF.toString());
    result.add(HaxeTokenTypes.PPERROR.toString());
    return result;
  }

  @Nonnull
  private static Collection<? extends String> suggestBySibling(@Nullable PsiElement sibling) {
    if (HaxeIfStatement.class.isInstance(sibling)) {
      return Arrays.asList(HaxeTokenTypes.KELSE.toString());
    } else if (HaxeTryStatement.class.isInstance(sibling) || HaxeCatchStatement.class.isInstance(sibling)) {
      return Arrays.asList(HaxeTokenTypes.KCATCH.toString());
    }

    return Collections.emptyList();
  }

  @Nonnull
  @Override
  public Language getLanguage() {
    return Language.ANY;
  }
}
