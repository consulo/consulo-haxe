// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi.impl;

import java.util.List;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;

import javax.annotation.*;

import com.intellij.plugins.haxe.lang.psi.*;

public class HaxeSwitchCaseImpl extends HaxePsiCompositeElementImpl implements HaxeSwitchCase {

  public HaxeSwitchCaseImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof HaxeVisitor) ((HaxeVisitor)visitor).visitSwitchCase(this);
    else super.accept(visitor);
  }

  @Override
  @Nonnull
  public List<HaxeExpression> getExpressionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaxeExpression.class);
  }

  @Override
  @Nullable
  public HaxeSwitchCaseBlock getSwitchCaseBlock() {
    return findChildByClass(HaxeSwitchCaseBlock.class);
  }

}
