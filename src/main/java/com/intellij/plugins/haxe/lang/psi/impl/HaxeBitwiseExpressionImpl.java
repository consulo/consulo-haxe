// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi.impl;

import java.util.List;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;

import javax.annotation.Nonnull;

import com.intellij.plugins.haxe.lang.psi.*;

public class HaxeBitwiseExpressionImpl extends HaxeExpressionImpl implements HaxeBitwiseExpression {

  public HaxeBitwiseExpressionImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof HaxeVisitor) ((HaxeVisitor)visitor).visitBitwiseExpression(this);
    else super.accept(visitor);
  }

  @Override
  @Nonnull
  public HaxeBitOperation getBitOperation() {
    return findNotNullChildByClass(HaxeBitOperation.class);
  }

  @Override
  @Nonnull
  public List<HaxeExpression> getExpressionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaxeExpression.class);
  }

}
