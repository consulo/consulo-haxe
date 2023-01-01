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

import com.intellij.plugins.haxe.ide.index.HaxeComponentIndex;
import com.intellij.plugins.haxe.lang.psi.HaxeComponent;
import consulo.annotation.component.ExtensionImpl;
import consulo.application.util.function.Processor;
import consulo.content.scope.SearchScope;
import consulo.ide.navigation.GotoClassOrTypeContributor;
import consulo.language.psi.search.FindSymbolParameters;
import consulo.language.psi.stub.FileBasedIndex;
import consulo.language.psi.stub.IdFilter;
import consulo.navigation.NavigationItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author: Fedor.Korotkov
 */
@ExtensionImpl
public class HaxeClassContributor implements GotoClassOrTypeContributor {
  @Override
  public void processNames(@Nonnull Processor<String> processor, @Nonnull SearchScope searchScope, @Nullable IdFilter idFilter) {
    FileBasedIndex.getInstance().processAllKeys(HaxeComponentIndex.HAXE_COMPONENT_INDEX, processor, searchScope, idFilter);
  }

  @Override
  public void processElementsWithName(@Nonnull String name, @Nonnull Processor<NavigationItem> processor, @Nonnull FindSymbolParameters findSymbolParameters) {
    List<HaxeComponent> itemsByName = HaxeComponentIndex.getItemsByName(name, findSymbolParameters.getProject(), findSymbolParameters.getSearchScope());
    for (HaxeComponent haxeComponent : itemsByName) {
      if (!processor.process(haxeComponent)) {
        break;
      }
    }
  }
}
