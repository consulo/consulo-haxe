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
import com.intellij.plugins.haxe.ide.index.HaxeClassInfo;
import com.intellij.plugins.haxe.ide.index.HaxeComponentIndex;
import com.intellij.plugins.haxe.lang.psi.HaxeIdentifier;
import com.intellij.plugins.haxe.lang.psi.HaxePackage;
import com.intellij.plugins.haxe.lang.psi.HaxeReference;
import com.intellij.plugins.haxe.lang.psi.HaxeType;
import com.intellij.plugins.haxe.util.HaxeAddImportHelper;
import com.intellij.plugins.haxe.util.HaxeResolveUtil;
import consulo.annotation.component.ExtensionImpl;
import consulo.application.Result;
import consulo.application.util.function.Processor;
import consulo.language.Language;
import consulo.language.editor.WriteCommandAction;
import consulo.language.editor.completion.*;
import consulo.language.editor.completion.lookup.InsertHandler;
import consulo.language.editor.completion.lookup.InsertionContext;
import consulo.language.editor.completion.lookup.LookupElement;
import consulo.language.editor.completion.lookup.LookupElementBuilder;
import consulo.language.pattern.PsiElementPattern;
import consulo.language.pattern.StandardPatterns;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiReference;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.language.util.ProcessingContext;
import consulo.project.Project;
import consulo.util.lang.Pair;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import static consulo.language.pattern.PlatformPatterns.psiElement;

/**
 * @author: Fedor.Korotkov
 */
@ExtensionImpl
public class HaxeClassNameCompletionContributor extends CompletionContributor {
  public HaxeClassNameCompletionContributor() {
    final PsiElementPattern.Capture<PsiElement> idInExpression =
        psiElement().withSuperParent(1, HaxeIdentifier.class).withSuperParent(2, HaxeReference.class);
    final PsiElementPattern.Capture<PsiElement> inComplexExpression = psiElement().withSuperParent(3, HaxeReference.class);
    extend(CompletionType.BASIC,
        psiElement().andOr(StandardPatterns.instanceOf(HaxeType.class), idInExpression.andNot(inComplexExpression)),
        new CompletionProvider() {
          @Override
          public void addCompletions(@Nonnull CompletionParameters parameters,
                                     ProcessingContext context,
                                     @Nonnull CompletionResultSet result) {
            addVariantsFromIndex(result, parameters.getOriginalFile(), null, CLASS_INSERT_HANDLER);
          }
        });
    extend(CompletionType.BASIC,
        psiElement().and(inComplexExpression),
        new CompletionProvider() {
          @Override
          public void addCompletions(@Nonnull CompletionParameters parameters,
                                     ProcessingContext context,
                                     @Nonnull CompletionResultSet result) {
            HaxeReference leftReference =
                HaxeResolveUtil.getLeftReference(PsiTreeUtil.getParentOfType(parameters.getPosition(), HaxeReference.class));
            PsiElement leftTarget = leftReference != null ? leftReference.resolve() : null;
            if (leftTarget instanceof HaxePackage) {
              addVariantsFromIndex(result, parameters.getOriginalFile(), ((HaxePackage) leftTarget).getQualifiedName(), null);
            }
          }
        });
  }

  private static void addVariantsFromIndex(final CompletionResultSet resultSet,
                                           final PsiFile targetFile,
                                           @Nullable String prefixPackage,
                                           @Nullable final InsertHandler<LookupElement> insertHandler) {
    final Project project = targetFile.getProject();
    final GlobalSearchScope scope = HaxeResolveUtil.getScopeForElement(targetFile);
    HaxeComponentIndex.processAll(project, new MyProcessor(resultSet, prefixPackage, insertHandler), scope);
  }

  private static final InsertHandler<LookupElement> CLASS_INSERT_HANDLER = new InsertHandler<LookupElement>() {
    public void handleInsert(final InsertionContext context, final LookupElement item) {
      addImportForLookupElement(context, item, context.getTailOffset() - 1);
    }
  };

  private static void addImportForLookupElement(final InsertionContext context, final LookupElement item, final int tailOffset) {
    final PsiReference ref = context.getFile().findReferenceAt(tailOffset);
    if (ref == null || ref.resolve() != null) {
      // no import statement needed
      return;
    }
    new WriteCommandAction(context.getProject(), context.getFile()) {
      @Override
      protected void run(Result result) throws Throwable {
        final String importPath = (String) item.getObject();
        HaxeAddImportHelper.addImport(importPath, context.getFile());
      }
    }.execute();
  }

  @Nonnull
  @Override
  public Language getLanguage() {
    return HaxeLanguage.INSTANCE;
  }

  private static class MyProcessor implements Processor<Pair<String, HaxeClassInfo>> {
    private final CompletionResultSet myResultSet;
    @Nullable
    private final InsertHandler<LookupElement> myInsertHandler;
    @Nullable
    private final String myPrefixPackage;

    private MyProcessor(CompletionResultSet resultSet,
                        @Nullable String prefixPackage,
                        @Nullable InsertHandler<LookupElement> insertHandler) {
      myResultSet = resultSet;
      myPrefixPackage = prefixPackage;
      myInsertHandler = insertHandler;
    }

    @Override
    public boolean process(Pair<String, HaxeClassInfo> pair) {
      HaxeClassInfo info = pair.getSecond();
      if (myPrefixPackage == null || myPrefixPackage.equalsIgnoreCase(info.getValue())) {
        String name = pair.getFirst();
        final String qName = HaxeResolveUtil.joinQName(info.getValue(), name);
        myResultSet.addElement(LookupElementBuilder.create(qName, name)
            .withIcon(info.getIcon())
            .withTailText(" " + info.getValue(), true)
            .withInsertHandler(myInsertHandler));
      }
      return true;
    }
  }
}
