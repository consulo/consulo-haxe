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
package com.intellij.plugins.haxe.lang.psi;

import consulo.annotation.component.ComponentScope;
import consulo.annotation.component.ServiceAPI;
import consulo.annotation.component.ServiceImpl;
import consulo.application.progress.ProgressIndicatorProvider;
import consulo.ide.ServiceManager;
import consulo.language.psi.AnyPsiChangeListener;
import consulo.project.Project;
import consulo.util.collection.ContainerUtil;
import consulo.util.collection.HashingStrategy;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

/**
 * @author: Fedor.Korotkov
 */
@Singleton
@ServiceAPI(ComponentScope.PROJECT)
@ServiceImpl
public class HaxeClassResolveCache {
  private final Map<HaxeClass, HaxeClassResolveResult> myMap = createWeakMap();

  public static HaxeClassResolveCache getInstance(Project project) {
    ProgressIndicatorProvider.checkCanceled(); // We hope this method is being called often enough to cancel daemon processes smoothly
    return ServiceManager.getService(project, HaxeClassResolveCache.class);
  }

  @Inject
  public HaxeClassResolveCache(@Nonnull Project project) {
    project.getMessageBus().connect().subscribe(AnyPsiChangeListener.class, new AnyPsiChangeListener() {
      @Override
      public void beforePsiChanged(boolean isPhysical) {
        myMap.clear();
      }

      @Override
      public void afterPsiChanged(boolean isPhysical) {
      }
    });
  }

  private static <K, V> Map<K, V> createWeakMap() {
    return ContainerUtil.<K, V>createWeakMap(7, 0.75f, HashingStrategy.canonical());
  }

  public void put(@Nonnull HaxeClass haxeClass, @Nonnull HaxeClassResolveResult result) {
    myMap.put(haxeClass, result);
  }

  @Nullable
  public HaxeClassResolveResult get(HaxeClass haxeClass) {
    return myMap.get(haxeClass);
  }
}
