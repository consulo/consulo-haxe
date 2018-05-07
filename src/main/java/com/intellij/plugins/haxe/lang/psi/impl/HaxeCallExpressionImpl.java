// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

import javax.annotation.*;

import com.intellij.plugins.haxe.lang.psi.*;

public class HaxeCallExpressionImpl extends HaxeReferenceImpl implements HaxeCallExpression {

  public HaxeCallExpressionImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof HaxeVisitor) ((HaxeVisitor)visitor).visitCallExpression(this);
    else super.accept(visitor);
  }

  @Override
  @Nonnull
  public HaxeExpression getExpression() {
    return findNotNullChildByClass(HaxeExpression.class);
  }

  @Override
  @Nullable
  public HaxeExpressionList getExpressionList() {
    return findChildByClass(HaxeExpressionList.class);
  }

}
