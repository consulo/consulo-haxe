// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

import javax.annotation.*;

import com.intellij.plugins.haxe.lang.psi.*;

public class HaxeNativeMetaImpl extends HaxePsiCompositeElementImpl implements HaxeNativeMeta {

  public HaxeNativeMetaImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof HaxeVisitor) ((HaxeVisitor)visitor).visitNativeMeta(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public HaxeStringLiteralExpression getStringLiteralExpression() {
    return findChildByClass(HaxeStringLiteralExpression.class);
  }

}
