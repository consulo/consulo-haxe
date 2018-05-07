// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi.impl;

import java.util.List;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;

import javax.annotation.Nonnull;

import com.intellij.plugins.haxe.lang.psi.*;

public class HaxeIfStatementImpl extends HaxePsiCompositeElementImpl implements HaxeIfStatement {

  public HaxeIfStatementImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof HaxeVisitor) ((HaxeVisitor)visitor).visitIfStatement(this);
    else super.accept(visitor);
  }

  @Override
  @Nonnull
  public List<HaxeBlockStatement> getBlockStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaxeBlockStatement.class);
  }

  @Override
  @Nonnull
  public List<HaxeBreakStatement> getBreakStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaxeBreakStatement.class);
  }

  @Override
  @Nonnull
  public List<HaxeConditional> getConditionalList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaxeConditional.class);
  }

  @Override
  @Nonnull
  public List<HaxeContinueStatement> getContinueStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaxeContinueStatement.class);
  }

  @Override
  @Nonnull
  public List<HaxeDoWhileStatement> getDoWhileStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaxeDoWhileStatement.class);
  }

  @Override
  @Nonnull
  public List<HaxeExpression> getExpressionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaxeExpression.class);
  }

  @Override
  @Nonnull
  public List<HaxeForStatement> getForStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaxeForStatement.class);
  }

  @Override
  @Nonnull
  public List<HaxeIfStatement> getIfStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaxeIfStatement.class);
  }

  @Override
  @Nonnull
  public List<HaxeLocalFunctionDeclaration> getLocalFunctionDeclarationList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaxeLocalFunctionDeclaration.class);
  }

  @Override
  @Nonnull
  public List<HaxeLocalVarDeclaration> getLocalVarDeclarationList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaxeLocalVarDeclaration.class);
  }

  @Override
  @Nonnull
  public List<HaxeReturnStatement> getReturnStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaxeReturnStatement.class);
  }

  @Override
  @Nonnull
  public List<HaxeSwitchStatement> getSwitchStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaxeSwitchStatement.class);
  }

  @Override
  @Nonnull
  public List<HaxeThrowStatement> getThrowStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaxeThrowStatement.class);
  }

  @Override
  @Nonnull
  public List<HaxeTryStatement> getTryStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaxeTryStatement.class);
  }

  @Override
  @Nonnull
  public List<HaxeWhileStatement> getWhileStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaxeWhileStatement.class);
  }

}
