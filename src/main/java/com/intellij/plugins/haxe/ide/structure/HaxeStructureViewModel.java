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

import com.intellij.plugins.haxe.HaxeComponentType;
import com.intellij.plugins.haxe.lang.psi.HaxeClass;
import com.intellij.plugins.haxe.lang.psi.HaxeNamedComponent;
import consulo.application.AllIcons;
import consulo.fileEditor.structureView.StructureViewModel;
import consulo.fileEditor.structureView.StructureViewTreeElement;
import consulo.fileEditor.structureView.tree.ActionPresentation;
import consulo.fileEditor.structureView.tree.ActionPresentationData;
import consulo.fileEditor.structureView.tree.Filter;
import consulo.fileEditor.structureView.tree.TreeElement;
import consulo.ide.IdeBundle;
import consulo.language.editor.structureView.StructureViewModelBase;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import org.jetbrains.annotations.NonNls;

import javax.annotation.Nonnull;

/**
 * @author: Fedor.Korotkov
 */
public class HaxeStructureViewModel extends StructureViewModelBase implements StructureViewModel.ElementInfoProvider {
  public HaxeStructureViewModel(@Nonnull PsiFile psiFile) {
    super(psiFile, new HaxeStructureViewElement(psiFile));
   // withSorters(Sorter.ALPHA_SORTER, VisibilitySorter.INSTANCE);
    withSuitableClasses(HaxeNamedComponent.class, HaxeClass.class);
  }

  @Override
  public boolean isAlwaysShowsPlus(StructureViewTreeElement element) {
    return false;
  }

  @Nonnull
  @Override
  public Filter[] getFilters() {
    return new Filter[]{ourFieldsFilter};
  }

  @Override
  public boolean isAlwaysLeaf(StructureViewTreeElement element) {
    final Object value = element.getValue();
    return value instanceof HaxeNamedComponent && !(value instanceof HaxeClass);
  }

  @Override
  public boolean shouldEnterElement(Object element) {
    return element instanceof HaxeClass;
  }


  private static final Filter ourFieldsFilter = new Filter() {
    @NonNls public static final String ID = "SHOW_FIELDS";

    public boolean isVisible(TreeElement treeNode) {
      if (!(treeNode instanceof HaxeStructureViewElement)) return true;
      final PsiElement element = ((HaxeStructureViewElement)treeNode).getRealElement();

      if (HaxeComponentType.typeOf(element) == HaxeComponentType.FIELD) {
        return false;
      }

      return true;
    }

    public boolean isReverted() {
      return true;
    }

    @Nonnull
    public ActionPresentation getPresentation() {
      return new ActionPresentationData(IdeBundle.message("action.structureview.show.fields"),  null, AllIcons.Nodes.Field);
    }

    @Nonnull
    public String getName() {
      return ID;
    }
  };
}
