// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

import javax.annotation.*;

import com.intellij.plugins.haxe.lang.psi.*;

public class HaxeGenericListPartImpl extends AbstractHaxeNamedComponent implements HaxeGenericListPart {

  public HaxeGenericListPartImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof HaxeVisitor) ((HaxeVisitor)visitor).visitGenericListPart(this);
    else super.accept(visitor);
  }

  @Override
  @Nonnull
  public HaxeComponentName getComponentName() {
    return findNotNullChildByClass(HaxeComponentName.class);
  }

  @Override
  @Nullable
  public HaxeTypeList getTypeList() {
    return findChildByClass(HaxeTypeList.class);
  }

  @Override
  @Nullable
  public HaxeTypeListPart getTypeListPart() {
    return findChildByClass(HaxeTypeListPart.class);
  }

}
