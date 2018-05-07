// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi.impl;

import java.util.List;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;

import javax.annotation.Nonnull;

import com.intellij.plugins.haxe.lang.psi.*;

public class HaxeExternClassDeclarationBodyImpl extends HaxePsiCompositeElementImpl implements HaxeExternClassDeclarationBody {

  public HaxeExternClassDeclarationBodyImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof HaxeVisitor) ((HaxeVisitor)visitor).visitExternClassDeclarationBody(this);
    else super.accept(visitor);
  }

  @Override
  @Nonnull
  public List<HaxeConditional> getConditionalList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaxeConditional.class);
  }

  @Override
  @Nonnull
  public List<HaxeExternFunctionDeclaration> getExternFunctionDeclarationList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaxeExternFunctionDeclaration.class);
  }

  @Override
  @Nonnull
  public List<HaxeVarDeclaration> getVarDeclarationList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaxeVarDeclaration.class);
  }

}
