// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

import javax.annotation.*;

import com.intellij.plugins.haxe.lang.psi.*;

public class HaxeAnonymousFunctionDeclarationImpl extends HaxePsiCompositeElementImpl implements HaxeAnonymousFunctionDeclaration {

  public HaxeAnonymousFunctionDeclarationImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof HaxeVisitor) ((HaxeVisitor)visitor).visitAnonymousFunctionDeclaration(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public HaxeParameterList getParameterList() {
    return findChildByClass(HaxeParameterList.class);
  }

  @Override
  @Nullable
  public HaxeTypeTag getTypeTag() {
    return findChildByClass(HaxeTypeTag.class);
  }

}
