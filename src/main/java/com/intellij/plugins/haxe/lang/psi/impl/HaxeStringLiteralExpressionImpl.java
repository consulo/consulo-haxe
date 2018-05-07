// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi.impl;

import java.util.List;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;

import javax.annotation.Nonnull;

import com.intellij.plugins.haxe.lang.psi.*;

public class HaxeStringLiteralExpressionImpl extends HaxeReferenceImpl implements HaxeStringLiteralExpression {

  public HaxeStringLiteralExpressionImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof HaxeVisitor) ((HaxeVisitor)visitor).visitStringLiteralExpression(this);
    else super.accept(visitor);
  }

  @Override
  @Nonnull
  public List<HaxeLongTemplateEntry> getLongTemplateEntryList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaxeLongTemplateEntry.class);
  }

  @Override
  @Nonnull
  public List<HaxeShortTemplateEntry> getShortTemplateEntryList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaxeShortTemplateEntry.class);
  }

}
