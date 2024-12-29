/*
 * Copyright 2000-2013 JetBrains s.r.o.
 * Copyright 2014-2014 AS3Boyan
 * Copyright 2014-2014 Elias Ku
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
package com.intellij.plugins.haxe.ide.inspections;

import com.intellij.plugins.haxe.HaxeBundle;
import com.intellij.plugins.haxe.HaxeLanguage;
import com.intellij.plugins.haxe.ide.HaxeImportOptimizer;
import com.intellij.plugins.haxe.lang.psi.HaxeImportStatementRegular;
import com.intellij.plugins.haxe.lang.psi.HaxeImportStatementWithInSupport;
import com.intellij.plugins.haxe.lang.psi.HaxeImportStatementWithWildcard;
import com.intellij.plugins.haxe.util.HaxeImportUtil;
import consulo.annotation.component.ExtensionImpl;
import consulo.application.ApplicationManager;
import consulo.document.util.TextRange;
import consulo.language.Language;
import consulo.language.editor.inspection.LocalInspectionTool;
import consulo.language.editor.inspection.LocalQuickFix;
import consulo.language.editor.inspection.ProblemDescriptor;
import consulo.language.editor.inspection.ProblemHighlightType;
import consulo.language.editor.inspection.scheme.InspectionManager;
import consulo.language.editor.rawHighlight.HighlightDisplayLevel;
import consulo.language.editor.refactoring.ImportOptimizer;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.project.Project;
import consulo.undoRedo.CommandProcessor;
import consulo.util.collection.ArrayUtil;
import org.jetbrains.annotations.Nls;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fedorkorotkov.
 */
@ExtensionImpl
public class HaxeUnusedImportInspection extends LocalInspectionTool {
  @Override
  @Nonnull
  public String getGroupDisplayName() {
    return "General";
  }

  @Nullable
  @Override
  public Language getLanguage() {
    return HaxeLanguage.INSTANCE;
  }

  @Nls
  @Nonnull
  @Override
  public String getDisplayName() {
    return HaxeBundle.message("haxe.inspection.unused.import.name");
  }

  @Override
  public boolean isEnabledByDefault() {
    return true;
  }

  @Nonnull
  @Override
  public String getShortName() {
    return "HaxeUnusedImport";
  }

  @Nonnull
  @Override
  public HighlightDisplayLevel getDefaultLevel() {
    return HighlightDisplayLevel.WARNING;
  }

  @Nullable
  @Override
  public ProblemDescriptor[] checkFile(@Nonnull PsiFile file, @Nonnull InspectionManager manager, boolean isOnTheFly) {
    List<HaxeImportStatementRegular> unusedImports = HaxeImportUtil.findUnusedImports(file);
    List<HaxeImportStatementWithInSupport> unusedInImports = HaxeImportUtil.findUnusedInImports(file);
    List<HaxeImportStatementWithWildcard> unusedImportsWithWildcard = HaxeImportUtil.findUnusedInImportsWithWildcards(file);
    if (unusedImports.isEmpty() && unusedInImports.isEmpty() && unusedImportsWithWildcard.isEmpty()) {
      return ProblemDescriptor.EMPTY_ARRAY;
    }
    final List<ProblemDescriptor> result = new ArrayList<ProblemDescriptor>();
    for (HaxeImportStatementRegular haxeImportStatement : unusedImports) {
      result.add(manager.createProblemDescriptor(haxeImportStatement, TextRange.from(0, haxeImportStatement.getTextLength()),
          getDisplayName(), ProblemHighlightType.LIKE_UNUSED_SYMBOL, isOnTheFly, OPTIMIZE_IMPORTS_FIX));
    }

    for (HaxeImportStatementWithInSupport haxeImportStatement : unusedInImports) {
      result.add(manager.createProblemDescriptor(haxeImportStatement, TextRange.from(0, haxeImportStatement.getTextLength()),
          getDisplayName(), ProblemHighlightType.LIKE_UNUSED_SYMBOL, isOnTheFly, OPTIMIZE_IMPORTS_FIX));
    }

    for (HaxeImportStatementWithWildcard haxeImportStatement : unusedImportsWithWildcard) {
      result.add(manager.createProblemDescriptor(haxeImportStatement, TextRange.from(0, haxeImportStatement.getTextLength()),
          getDisplayName(), ProblemHighlightType.LIKE_UNUSED_SYMBOL, isOnTheFly, OPTIMIZE_IMPORTS_FIX));
    }

    return ArrayUtil.toObjectArray(result, ProblemDescriptor.class);
  }

  private static final LocalQuickFix OPTIMIZE_IMPORTS_FIX = new LocalQuickFix() {
    @Nonnull
    @Override
    public String getName() {
      return HaxeBundle.message("haxe.fix.optimize.imports");
    }

    @Override
    @Nonnull
    public String getFamilyName() {
      return getName();
    }

    @Override
    public void applyFix(@Nonnull Project project, @Nonnull ProblemDescriptor descriptor) {
      PsiElement psiElement = descriptor.getPsiElement();
      invoke(project, psiElement.getContainingFile());
    }

    public void invoke(@Nonnull final Project project, PsiFile file) {
      ImportOptimizer optimizer = new HaxeImportOptimizer();
      final Runnable runnable = optimizer.processFile(file);
      ApplicationManager.getApplication().runWriteAction(new Runnable() {
        @Override
        public void run() {
          CommandProcessor.getInstance().executeCommand(project, runnable, getFamilyName(), this);
        }
      });
    }
  };
}
