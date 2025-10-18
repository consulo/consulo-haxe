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
package com.intellij.plugins.haxe.ide.library;

import consulo.content.base.BinariesOrderRootType;
import consulo.content.base.SourcesOrderRootType;
import consulo.content.library.ui.DefaultLibraryRootsComponentDescriptor;
import consulo.content.library.ui.RootDetector;
import consulo.haxe.localize.HaxeLocalize;
import jakarta.annotation.Nonnull;

import java.util.Arrays;
import java.util.List;

/**
 * @author: Fedor.Korotkov
 */
public class HaxeLibraryRootsComponentDescriptor extends DefaultLibraryRootsComponentDescriptor {
  @Nonnull
  @Override
  public List<? extends RootDetector> getRootDetectors() {
    return Arrays.asList(
        new HaxeLibRootDetector(SourcesOrderRootType.getInstance(), HaxeLocalize.sourcesRootDetectorSourcesName()),
        new HaxeLibRootDetector(BinariesOrderRootType.getInstance(), HaxeLocalize.sourcesRootDetectorClassesName())
    );
  }
}
