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

import com.intellij.plugins.haxe.lang.psi.*;
import consulo.language.ast.ASTNode;

import jakarta.annotation.Nonnull;

import java.util.Arrays;
import java.util.List;

/**
 * @author: Fedor.Korotkov
 */
public abstract class AnonymousHaxeTypeImpl extends AbstractHaxePsiClass implements HaxeAnonymousType {
  public AnonymousHaxeTypeImpl(@Nonnull ASTNode node) {
    super(node);
  }

  @Nonnull
  @Override
  public List<HaxeType> getExtendsList() {
    final HaxeTypeExtends typeExtends = getAnonymousTypeBody().getTypeExtends();
    if (typeExtends != null) {
      return Arrays.asList(typeExtends.getType());
    }
    return super.getExtendsList();
  }

  @Override
  public HaxeComponentName getComponentName() {
    return null;
  }

  @Override
  public HaxeGenericParam getGenericParam() {
    return null;
  }
}
