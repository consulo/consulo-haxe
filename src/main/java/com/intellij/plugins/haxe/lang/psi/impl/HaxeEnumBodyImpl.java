// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi.impl;

import java.util.List;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;

import javax.annotation.Nonnull;

import com.intellij.plugins.haxe.lang.psi.*;

public class HaxeEnumBodyImpl extends HaxePsiCompositeElementImpl implements HaxeEnumBody {

  public HaxeEnumBodyImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof HaxeVisitor) ((HaxeVisitor)visitor).visitEnumBody(this);
    else super.accept(visitor);
  }

  @Override
  @Nonnull
  public List<HaxeEnumValueDeclaration> getEnumValueDeclarationList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaxeEnumValueDeclaration.class);
  }

}
