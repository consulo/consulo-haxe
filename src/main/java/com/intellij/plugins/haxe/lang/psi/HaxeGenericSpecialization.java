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

import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Fedor.Korotkov
 */
public class HaxeGenericSpecialization implements Cloneable {
  public static final HaxeGenericSpecialization EMPTY = new HaxeGenericSpecialization();
  final Map<String, HaxeClassResolveResult> map;

  public HaxeGenericSpecialization() {
    this(new HashMap<String, HaxeClassResolveResult>());
  }

  @Override
  protected HaxeGenericSpecialization clone() {
    final Map<String, HaxeClassResolveResult> clonedMap = new HashMap<String, HaxeClassResolveResult>();
    for (String key : map.keySet()) {
      clonedMap.put(key, map.get(key));
    }
    return new HaxeGenericSpecialization(clonedMap);
  }

  protected HaxeGenericSpecialization(Map<String, HaxeClassResolveResult> map) {
    this.map = map;
  }

  public void put(PsiElement element, String genericName, HaxeClassResolveResult resolveResult) {
    map.put(getGenericKey(element, genericName), resolveResult);
  }

  public boolean containsKey(@Nullable PsiElement element, String genericName) {
    return map.containsKey(getGenericKey(element, genericName));
  }

  public HaxeClassResolveResult get(@Nullable PsiElement element, String genericName) {
    return map.get(getGenericKey(element, genericName));
  }

  public HaxeGenericSpecialization getInnerSpecialization(PsiElement element) {
    final String prefixToRemove = getGenericKey(element, "");
    final Map<String, HaxeClassResolveResult> result = new HashMap<String, HaxeClassResolveResult>();
    for (String key : map.keySet()) {
      final HaxeClassResolveResult value = map.get(key);
      String newKey = key;
      if (newKey.startsWith(prefixToRemove)) {
        newKey = newKey.substring(prefixToRemove.length());
      }
      result.put(newKey, value);
    }
    return new HaxeGenericSpecialization(result);
  }

  public static String getGenericKey(@Nullable PsiElement element, @Nonnull String genericName) {
    final StringBuilder result = new StringBuilder();
    final HaxeNamedComponent namedComponent = PsiTreeUtil.getParentOfType(element, HaxeNamedComponent.class, false);
    if (namedComponent instanceof HaxeClass) {
      result.append(((HaxeClass)namedComponent).getQualifiedName());
    }
    else if (namedComponent != null) {
      HaxeClass haxeClass = PsiTreeUtil.getParentOfType(namedComponent, HaxeClass.class);
      if (haxeClass instanceof HaxeAnonymousType) {
        // class -> typeOrAnonymous -> anonymous
        final PsiElement parent = haxeClass.getParent().getParent();
        haxeClass = parent instanceof HaxeClass ? (HaxeClass)parent : haxeClass;
      }
      if (haxeClass != null) {
        result.append(haxeClass.getQualifiedName());
      }
      if (PsiTreeUtil.getChildOfType(namedComponent, HaxeGenericParam.class) != null) {
        // generic method
        result.append(":");
        result.append(namedComponent.getName());
      }
    }
    if (result.length() > 0) {
      result.append("-");
    }
    result.append(genericName);
    return result.toString();
  }
}
