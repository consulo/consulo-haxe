// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

import javax.annotation.*;

import com.intellij.plugins.haxe.lang.psi.*;

public class HaxeShiftOperatorImpl extends HaxePsiCompositeElementImpl implements HaxeShiftOperator {

  public HaxeShiftOperatorImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof HaxeVisitor) ((HaxeVisitor)visitor).visitShiftOperator(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public HaxeShiftRightOperator getShiftRightOperator() {
    return findChildByClass(HaxeShiftRightOperator.class);
  }

  @Override
  @Nullable
  public HaxeUnsignedShiftRightOperator getUnsignedShiftRightOperator() {
    return findChildByClass(HaxeUnsignedShiftRightOperator.class);
  }

}
