// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

import javax.annotation.*;

import com.intellij.plugins.haxe.lang.psi.*;

public class HaxeTypeImpl extends HaxePsiCompositeElementImpl implements HaxeType {

  public HaxeTypeImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof HaxeVisitor) ((HaxeVisitor)visitor).visitType(this);
    else super.accept(visitor);
  }

  @Override
  @Nonnull
  public HaxeReferenceExpression getReferenceExpression() {
    return findNotNullChildByClass(HaxeReferenceExpression.class);
  }

  @Override
  @Nullable
  public HaxeTypeParam getTypeParam() {
    return findChildByClass(HaxeTypeParam.class);
  }

}
