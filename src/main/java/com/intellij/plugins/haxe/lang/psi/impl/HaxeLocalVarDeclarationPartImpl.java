// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

import javax.annotation.*;

import com.intellij.plugins.haxe.lang.psi.*;

public class HaxeLocalVarDeclarationPartImpl extends AbstractHaxeNamedComponent implements HaxeLocalVarDeclarationPart {

  public HaxeLocalVarDeclarationPartImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof HaxeVisitor) ((HaxeVisitor)visitor).visitLocalVarDeclarationPart(this);
    else super.accept(visitor);
  }

  @Override
  @Nonnull
  public HaxeComponentName getComponentName() {
    return findNotNullChildByClass(HaxeComponentName.class);
  }

  @Override
  @Nullable
  public HaxePropertyDeclaration getPropertyDeclaration() {
    return findChildByClass(HaxePropertyDeclaration.class);
  }

  @Override
  @Nullable
  public HaxeTypeTag getTypeTag() {
    return findChildByClass(HaxeTypeTag.class);
  }

  @Override
  @Nullable
  public HaxeVarInit getVarInit() {
    return findChildByClass(HaxeVarInit.class);
  }

}
