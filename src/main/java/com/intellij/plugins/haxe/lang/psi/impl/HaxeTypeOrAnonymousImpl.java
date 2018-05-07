// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

import javax.annotation.*;

import com.intellij.plugins.haxe.lang.psi.*;

public class HaxeTypeOrAnonymousImpl extends HaxePsiCompositeElementImpl implements HaxeTypeOrAnonymous {

  public HaxeTypeOrAnonymousImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof HaxeVisitor) ((HaxeVisitor)visitor).visitTypeOrAnonymous(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public HaxeAnonymousType getAnonymousType() {
    return findChildByClass(HaxeAnonymousType.class);
  }

  @Override
  @Nullable
  public HaxeType getType() {
    return findChildByClass(HaxeType.class);
  }

}
