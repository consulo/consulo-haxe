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

import com.intellij.lang.ASTNode;
import com.intellij.plugins.haxe.lang.psi.*;
import com.intellij.plugins.haxe.util.HaxeResolveUtil;
import com.intellij.util.containers.ContainerUtil;

import javax.annotation.Nonnull;

import java.util.List;

/**
 * @author: Fedor.Korotkov
 */
public abstract class AbstractHaxeTypeDefImpl extends AbstractHaxePsiClass implements HaxeTypedefDeclaration {
  public AbstractHaxeTypeDefImpl(@Nonnull ASTNode node) {
    super(node);
  }

  public HaxeClassResolveResult getTargetClass() {
    return getTargetClass(new HaxeGenericSpecialization());
  }

  public HaxeClassResolveResult getTargetClass(HaxeGenericSpecialization specialization) {
    final HaxeTypeOrAnonymous haxeTypeOrAnonymous = ContainerUtil.getFirstItem(getTypeOrAnonymousList());
    if (haxeTypeOrAnonymous == null) {
      // cause parse error
      return HaxeClassResolveResult.create(null);
    }
    if (haxeTypeOrAnonymous.getAnonymousType() != null) {
      return HaxeClassResolveResult.create(haxeTypeOrAnonymous.getAnonymousType(), specialization);
    }
    return HaxeResolveUtil.getHaxeClassResolveResult(haxeTypeOrAnonymous.getType(), specialization);
  }

  @Nonnull
  @Override
  public List<HaxeType> getExtendsList() {
    final HaxeClass targetHaxeClass = getTargetClass().getHaxeClass();
    if (targetHaxeClass != null) {
      return targetHaxeClass.getExtendsList();
    }
    return super.getExtendsList();
  }

  @Nonnull
  @Override
  public List<HaxeType> getImplementsList() {
    final HaxeClass targetHaxeClass = getTargetClass().getHaxeClass();
    if (targetHaxeClass != null) {
      return targetHaxeClass.getImplementsList();
    }
    return super.getImplementsList();
  }

  @Override
  public boolean isInterface() {
    final HaxeClass targetHaxeClass = getTargetClass().getHaxeClass();
    if (targetHaxeClass != null) {
      return targetHaxeClass.isInterface();
    }
    return super.isInterface();
  }

  @Nonnull
  @Override
  public List<HaxeNamedComponent> getMethods() {
    final HaxeClass targetHaxeClass = getTargetClass().getHaxeClass();
    if (targetHaxeClass != null) {
      return targetHaxeClass.getMethods();
    }
    return super.getMethods();
  }

  @Nonnull
  @Override
  public List<HaxeNamedComponent> getFields() {
    final HaxeClass targetHaxeClass = getTargetClass().getHaxeClass();
    if (targetHaxeClass != null) {
      return targetHaxeClass.getFields();
    }
    return super.getFields();
  }

  @Override
  public HaxeNamedComponent findFieldByName(@Nonnull String name) {
    final HaxeClass targetHaxeClass = getTargetClass().getHaxeClass();
    if (targetHaxeClass != null) {
      return targetHaxeClass.findFieldByName(name);
    }
    return super.findFieldByName(name);
  }

  @Override
  public HaxeNamedComponent findMethodByName(@Nonnull String name) {
    final HaxeClass targetHaxeClass = getTargetClass().getHaxeClass();
    if (targetHaxeClass != null) {
      return targetHaxeClass.findMethodByName(name);
    }
    return super.findMethodByName(name);
  }
}
