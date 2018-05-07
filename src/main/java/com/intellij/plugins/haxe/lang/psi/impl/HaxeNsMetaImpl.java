// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

import javax.annotation.*;

import com.intellij.plugins.haxe.lang.psi.*;

public class HaxeNsMetaImpl extends HaxePsiCompositeElementImpl implements HaxeNsMeta {

  public HaxeNsMetaImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof HaxeVisitor) ((HaxeVisitor)visitor).visitNsMeta(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public HaxeStringLiteralExpression getStringLiteralExpression() {
    return findChildByClass(HaxeStringLiteralExpression.class);
  }

}
