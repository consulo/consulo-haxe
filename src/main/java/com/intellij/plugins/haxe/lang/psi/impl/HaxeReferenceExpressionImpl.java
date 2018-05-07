// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

import javax.annotation.Nonnull;

import com.intellij.plugins.haxe.lang.psi.*;

public class HaxeReferenceExpressionImpl extends HaxeReferenceImpl implements HaxeReferenceExpression {

  public HaxeReferenceExpressionImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof HaxeVisitor) ((HaxeVisitor)visitor).visitReferenceExpression(this);
    else super.accept(visitor);
  }

  @Override
  @Nonnull
  public HaxeIdentifier getIdentifier() {
    return findNotNullChildByClass(HaxeIdentifier.class);
  }

}
