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

import com.intellij.plugins.haxe.lang.psi.HaxeComponentName;
import com.intellij.plugins.haxe.lang.psi.HaxeNamedComponent;
import consulo.language.psi.PsiElement;
import consulo.language.psi.resolve.PsiScopeProcessor;
import consulo.language.psi.resolve.ResolveState;
import consulo.util.dataholder.Key;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.Set;

/**
 * @author: Fedor.Korotkov
 */
public class ComponentNameScopeProcessor implements PsiScopeProcessor
{
  private final Set<HaxeComponentName> result;

  public ComponentNameScopeProcessor(Set<HaxeComponentName> result) {
    this.result = result;
  }

  @Override
  public boolean execute(@Nonnull PsiElement element, ResolveState state) {
    if (element instanceof HaxeNamedComponent) {
      final HaxeNamedComponent haxeNamedComponent = (HaxeNamedComponent)element;
      if (haxeNamedComponent.getComponentName() != null) {
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
}
