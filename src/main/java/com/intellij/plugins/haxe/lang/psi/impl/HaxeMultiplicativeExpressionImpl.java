// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi.impl;

import java.util.List;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;

import javax.annotation.*;

import com.intellij.plugins.haxe.lang.psi.*;

public class HaxeMultiplicativeExpressionImpl extends HaxeExpressionImpl implements HaxeMultiplicativeExpression {

  public HaxeMultiplicativeExpressionImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof HaxeVisitor) ((HaxeVisitor)visitor).visitMultiplicativeExpression(this);
    else super.accept(visitor);
  }

  @Override
  @Nonnull
  public List<HaxeExpression> getExpressionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaxeExpression.class);
  }

  @Override
  @Nullable
  public HaxeIfStatement getIfStatement() {
    return findChildByClass(HaxeIfStatement.class);
  }

  @Override
  @Nullable
  public HaxeSwitchStatement getSwitchStatement() {
    return findChildByClass(HaxeSwitchStatement.class);
  }

  @Override
  @Nullable
  public HaxeTryStatement getTryStatement() {
    return findChildByClass(HaxeTryStatement.class);
  }

}
