// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

import javax.annotation.*;

import com.intellij.plugins.haxe.lang.psi.*;

public class HaxeInheritImpl extends HaxePsiCompositeElementImpl implements HaxeInherit {

  public HaxeInheritImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof HaxeVisitor) ((HaxeVisitor)visitor).visitInherit(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public HaxeType getType() {
    return findChildByClass(HaxeType.class);
  }

}
