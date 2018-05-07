// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

import javax.annotation.*;

import com.intellij.plugins.haxe.lang.psi.*;

public class HaxeCastExpressionImpl extends HaxeClassReferenceImpl implements HaxeCastExpression {

  public HaxeCastExpressionImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof HaxeVisitor) ((HaxeVisitor)visitor).visitCastExpression(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public HaxeExpression getExpression() {
    return findChildByClass(HaxeExpression.class);
  }

  @Override
  @Nullable
  public HaxeFunctionType getFunctionType() {
    return findChildByClass(HaxeFunctionType.class);
  }

  @Override
  @Nullable
  public HaxeTypeOrAnonymous getTypeOrAnonymous() {
    return findChildByClass(HaxeTypeOrAnonymous.class);
  }

}
