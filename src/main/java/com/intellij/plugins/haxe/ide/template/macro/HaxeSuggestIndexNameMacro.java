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
package com.intellij.plugins.haxe.ide.template.macro;

import com.intellij.plugins.haxe.HaxeBundle;
import com.intellij.plugins.haxe.lang.psi.HaxeComponentName;
import com.intellij.plugins.haxe.util.HaxeMacroUtil;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.template.Expression;
import consulo.language.editor.template.ExpressionContext;
import consulo.language.editor.template.Result;
import consulo.language.editor.template.TextResult;
import consulo.language.editor.template.macro.Macro;
import consulo.language.psi.PsiElement;
import consulo.navigation.NavigationItem;
import consulo.util.collection.ContainerUtil;

import jakarta.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

/**
 * @author: Fedor.Korotkov
 */
@ExtensionImpl
public class HaxeSuggestIndexNameMacro extends Macro {
  @Override
  public String getName() {
    return "haxeSuggestIndexName";
  }

  @Override
  public String getPresentableName() {
    return HaxeBundle.message("macro.haxe.index.name");
  }

  @Nonnull
  @Override
  public String getDefaultValue() {
    return "i";
  }

  @Override
  public Result calculateResult(@Nonnull Expression[] params, ExpressionContext context) {
    final PsiElement at = context.getPsiElementAtStartOffset();
    final Set<HaxeComponentName> variables = HaxeMacroUtil.findVariables(at);
    final Set<String> names = new HashSet<String>(ContainerUtil.map(variables, NavigationItem::getName));
    for (char i = 'i'; i < 'z'; ++i) {
      if (!names.contains(Character.toString(i))) {
        return new TextResult(Character.toString(i));
      }
    }
    return null;
  }
}
