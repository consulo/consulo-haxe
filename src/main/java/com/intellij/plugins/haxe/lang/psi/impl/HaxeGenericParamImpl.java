// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi.impl;

import java.util.List;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;

import javax.annotation.Nonnull;

import com.intellij.plugins.haxe.lang.psi.*;

public class HaxeGenericParamImpl extends HaxePsiCompositeElementImpl implements HaxeGenericParam {

  public HaxeGenericParamImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof HaxeVisitor) ((HaxeVisitor)visitor).visitGenericParam(this);
    else super.accept(visitor);
  }

  @Override
  @Nonnull
  public List<HaxeGenericListPart> getGenericListPartList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaxeGenericListPart.class);
  }

}
