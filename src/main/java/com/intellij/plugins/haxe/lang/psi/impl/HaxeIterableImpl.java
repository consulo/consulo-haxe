// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

import javax.annotation.Nonnull;

import com.intellij.plugins.haxe.lang.psi.*;

public class HaxeIterableImpl extends HaxePsiCompositeElementImpl implements HaxeIterable {

  public HaxeIterableImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof HaxeVisitor) ((HaxeVisitor)visitor).visitIterable(this);
    else super.accept(visitor);
  }

  @Override
  @Nonnull
  public HaxeExpression getExpression() {
    return findNotNullChildByClass(HaxeExpression.class);
  }

}
