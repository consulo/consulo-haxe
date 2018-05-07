// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

import javax.annotation.*;

import com.intellij.plugins.haxe.lang.psi.*;

public class HaxeLongTemplateEntryImpl extends HaxePsiCompositeElementImpl implements HaxeLongTemplateEntry {

  public HaxeLongTemplateEntryImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof HaxeVisitor) ((HaxeVisitor)visitor).visitLongTemplateEntry(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public HaxeExpression getExpression() {
    return findChildByClass(HaxeExpression.class);
  }

}
