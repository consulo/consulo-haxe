// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

import javax.annotation.*;

import com.intellij.plugins.haxe.lang.psi.*;

public class HaxeGetterMetaImpl extends HaxePsiCompositeElementImpl implements HaxeGetterMeta {

  public HaxeGetterMetaImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof HaxeVisitor) ((HaxeVisitor)visitor).visitGetterMeta(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public HaxeReferenceExpression getReferenceExpression() {
    return findChildByClass(HaxeReferenceExpression.class);
  }

}
