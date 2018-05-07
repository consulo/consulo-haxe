// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi.impl;

import java.util.List;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;

import javax.annotation.*;

import com.intellij.plugins.haxe.lang.psi.*;

public class HaxeSwitchBlockImpl extends HaxePsiCompositeElementImpl implements HaxeSwitchBlock {

  public HaxeSwitchBlockImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof HaxeVisitor) ((HaxeVisitor)visitor).visitSwitchBlock(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public HaxeDefaultCase getDefaultCase() {
    return findChildByClass(HaxeDefaultCase.class);
  }

  @Override
  @Nonnull
  public List<HaxeSwitchCase> getSwitchCaseList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaxeSwitchCase.class);
  }

}
