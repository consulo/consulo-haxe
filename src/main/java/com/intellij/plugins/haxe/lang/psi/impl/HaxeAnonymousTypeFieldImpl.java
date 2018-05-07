// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

import javax.annotation.Nonnull;

import com.intellij.plugins.haxe.lang.psi.*;

public class HaxeAnonymousTypeFieldImpl extends AbstractHaxeNamedComponent implements HaxeAnonymousTypeField {

  public HaxeAnonymousTypeFieldImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof HaxeVisitor) ((HaxeVisitor)visitor).visitAnonymousTypeField(this);
    else super.accept(visitor);
  }

  @Override
  @Nonnull
  public HaxeComponentName getComponentName() {
    return findNotNullChildByClass(HaxeComponentName.class);
  }

  @Override
  @Nonnull
  public HaxeTypeTag getTypeTag() {
    return findNotNullChildByClass(HaxeTypeTag.class);
  }

}
