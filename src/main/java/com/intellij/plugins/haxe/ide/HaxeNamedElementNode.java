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

import com.intellij.plugins.haxe.lang.psi.HaxeClass;
import com.intellij.plugins.haxe.lang.psi.HaxeNamedComponent;
import consulo.component.util.Iconable;
import consulo.language.editor.generation.ClassMember;
import consulo.language.editor.generation.MemberChooserObject;
import consulo.language.editor.generation.PsiElementMemberChooserObject;
import consulo.language.icon.IconDescriptorUpdaters;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.navigation.ItemPresentation;

import jakarta.annotation.Nullable;

/**
 * @author: Fedor.Korotkov
 */
public class HaxeNamedElementNode extends PsiElementMemberChooserObject implements ClassMember {
  public HaxeNamedElementNode(final HaxeNamedComponent haxeNamedComponent) {
    super(haxeNamedComponent, buildPresentationText(haxeNamedComponent), IconDescriptorUpdaters.getIcon(haxeNamedComponent, Iconable.ICON_FLAG_VISIBILITY));
  }

  @Nullable
  private static String buildPresentationText(HaxeNamedComponent haxeNamedComponent) {
    final ItemPresentation presentation = haxeNamedComponent.getPresentation();
    if (presentation == null) {
      return haxeNamedComponent.getName();
    }
    final StringBuilder result = new StringBuilder();
    if (haxeNamedComponent instanceof HaxeClass) {
      result.append(haxeNamedComponent.getName());
      final String location = presentation.getLocationString();
      if (location != null && !location.isEmpty()) {
        result.append(" ").append(location);
      }
    }
    else {
      result.append(presentation.getPresentableText());
    }
    return result.toString();
  }

  @Nullable
  @Override
  public MemberChooserObject getParentNodeDelegate() {
    final HaxeNamedComponent result = PsiTreeUtil.getParentOfType(getPsiElement(), HaxeNamedComponent.class);
    return result == null ? null : new HaxeNamedElementNode(result);
  }
}
