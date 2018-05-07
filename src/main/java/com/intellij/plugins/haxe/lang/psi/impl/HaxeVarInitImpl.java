// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

import javax.annotation.*;

import com.intellij.plugins.haxe.lang.psi.*;

public class HaxeVarInitImpl extends HaxePsiCompositeElementImpl implements HaxeVarInit {

  public HaxeVarInitImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof HaxeVisitor) ((HaxeVisitor)visitor).visitVarInit(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public HaxeExpression getExpression() {
    return findChildByClass(HaxeExpression.class);
  }

}
