// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

import javax.annotation.*;

import com.intellij.plugins.haxe.lang.psi.*;

public class HaxeDefaultCaseImpl extends HaxePsiCompositeElementImpl implements HaxeDefaultCase {

  public HaxeDefaultCaseImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof HaxeVisitor) ((HaxeVisitor)visitor).visitDefaultCase(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public HaxeSwitchCaseBlock getSwitchCaseBlock() {
    return findChildByClass(HaxeSwitchCaseBlock.class);
  }

}
