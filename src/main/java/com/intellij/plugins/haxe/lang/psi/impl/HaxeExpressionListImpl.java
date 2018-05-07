// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi.impl;

import java.util.List;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;

import javax.annotation.*;

import com.intellij.plugins.haxe.lang.psi.*;

public class HaxeExpressionListImpl extends HaxePsiCompositeElementImpl implements HaxeExpressionList {

  public HaxeExpressionListImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof HaxeVisitor) ((HaxeVisitor)visitor).visitExpressionList(this);
    else super.accept(visitor);
  }

  @Override
  @Nonnull
  public List<HaxeExpression> getExpressionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaxeExpression.class);
  }

  @Override
  @Nullable
  public HaxeForStatement getForStatement() {
    return findChildByClass(HaxeForStatement.class);
  }

  @Override
  @Nullable
  public HaxeWhileStatement getWhileStatement() {
    return findChildByClass(HaxeWhileStatement.class);
  }

}
