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
package com.intellij.plugins.haxe.util;

import com.intellij.plugins.haxe.HaxeComponentType;
import com.intellij.plugins.haxe.lang.psi.HaxeComponentName;
import com.intellij.plugins.haxe.lang.psi.HaxeNamedComponent;
import consulo.language.psi.PsiElement;
import consulo.language.psi.resolve.PsiScopeProcessor;
import consulo.language.psi.resolve.ResolveState;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.util.dataholder.Key;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author: Fedor.Korotkov
 */
public class HaxeMacroUtil {
  public static Set<HaxeComponentName> findVariables(@Nullable PsiElement at) {
    if (at == null) {
      return Collections.emptySet();
    }
    final Set<HaxeComponentName> result = new HashSet<HaxeComponentName>();
    PsiTreeUtil.treeWalkUp(new PsiScopeProcessor() {
      @Override
      public boolean execute(@Nonnull PsiElement element, ResolveState state) {
        if (element instanceof HaxeNamedComponent) {
          final HaxeNamedComponent haxeNamedComponent = (HaxeNamedComponent)element;
          if (haxeNamedComponent.getComponentName() != null && HaxeComponentType.isVariable(HaxeComponentType.typeOf(haxeNamedComponent))) {
            result.add(haxeNamedComponent.getComponentName());
          }
        }
        return true;
      }

      @Override
      public <T> T getHint(@Nonnull Key<T> hintKey) {
        return null;
      }

      @Override
      public void handleEvent(Event event, @Nullable Object associated) {
      }
    }, at, null, ResolveState.initial());
    return result;
  }
}
