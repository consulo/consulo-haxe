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
package com.intellij.plugins.haxe.ide.structure;

import consulo.fileEditor.structureView.StructureViewTreeElement;
import consulo.fileEditor.structureView.tree.TreeElement;
import consulo.language.psi.PsiElement;
import consulo.navigation.ItemPresentation;
import consulo.navigation.NavigationItem;
import com.intellij.plugins.haxe.lang.psi.HaxeClass;
import com.intellij.plugins.haxe.lang.psi.HaxeFile;
import com.intellij.plugins.haxe.lang.psi.HaxeNamedComponent;
import com.intellij.plugins.haxe.util.HaxeResolveUtil;
import consulo.language.psi.PsiFile;
import consulo.fileEditor.structureView.tree.SortableTreeElement;

import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.Nullable;

/**
 * @author: Fedor.Korotkov
 */
public class HaxeStructureViewElement implements StructureViewTreeElement/*, AccessLevelProvider*/, SortableTreeElement
{
  private final PsiElement myElement;

  public HaxeStructureViewElement(final PsiElement element) {
    myElement = element;
  }

  @Override
  public Object getValue() {
    return myElement;
  }

  @Override
  public void navigate(boolean requestFocus) {
    if (myElement instanceof NavigationItem) {
      ((NavigationItem)myElement).navigate(requestFocus);
    }
  }

  @Override
  public boolean canNavigate() {
    return myElement instanceof NavigationItem && ((NavigationItem)myElement).canNavigate();
  }

  @Override
  public boolean canNavigateToSource() {
    return myElement instanceof NavigationItem && ((NavigationItem)myElement).canNavigateToSource();
  }

  @Nullable
  @Override
  public ItemPresentation getPresentation() {
    return myElement instanceof NavigationItem ? ((NavigationItem)myElement).getPresentation() : null;
  }

  @Override
  public TreeElement[] getChildren() {
    if (!myElement.isValid()) {
      return new TreeElement[0];
    }
    final List<TreeElement> result = new ArrayList<TreeElement>();
    if (myElement instanceof HaxeFile) {
      for (HaxeNamedComponent subNamedComponent : HaxeResolveUtil.findComponentDeclarations((PsiFile)myElement)) {
        result.add(new HaxeStructureViewElement(subNamedComponent));
      }
    }
    else if (myElement instanceof HaxeClass) {
      final HaxeClass haxeClass = (HaxeClass)myElement;
      for (HaxeClass superClass : HaxeResolveUtil.tyrResolveClassesByQName(haxeClass.getExtendsList())) {
        result.add(new HaxeStructureViewElement(superClass));
      }
      for (HaxeClass superInterface : HaxeResolveUtil.tyrResolveClassesByQName(haxeClass.getImplementsList())) {
        result.add(new HaxeStructureViewElement(superInterface));
      }
      for (HaxeNamedComponent subNamedComponent : HaxeResolveUtil.getNamedSubComponentsInOrder(haxeClass)) {
        result.add(new HaxeStructureViewElement(subNamedComponent));
      }
    }
    return result.toArray(new TreeElement[result.size()]);
  }

 /* @Override
  public int getAccessLevel() {
    HaxeNamedComponent namedComponent = null;
    if (myElement instanceof HaxeNamedComponent) {
      namedComponent = (HaxeNamedComponent)myElement;
    }
    else if (myElement.getParent() instanceof HaxeNamedComponent) {
      namedComponent = (HaxeNamedComponent)myElement.getParent();
    }
    return namedComponent == null || !namedComponent.isPublic() ? PsiUtil.ACCESS_LEVEL_PROTECTED : PsiUtil.ACCESS_LEVEL_PUBLIC;
  }

  @Override
  public int getSubLevel() {
    return 0;
  }   */

  @Override
  public String getAlphaSortKey() {
    final String result = myElement instanceof NavigationItem ? ((NavigationItem)myElement).getName() : null;
    return result == null ? "" : result;
  }

  public PsiElement getRealElement() {
    return myElement;
  }
}
