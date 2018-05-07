// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

import javax.annotation.*;

import com.intellij.plugins.haxe.lang.psi.*;

public class HaxeEnumValueDeclarationImpl extends AbstractHaxeNamedComponent implements HaxeEnumValueDeclaration {

  public HaxeEnumValueDeclarationImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof HaxeVisitor) ((HaxeVisitor)visitor).visitEnumValueDeclaration(this);
    else super.accept(visitor);
  }

  @Override
  @Nonnull
  public HaxeComponentName getComponentName() {
    return findNotNullChildByClass(HaxeComponentName.class);
  }

  @Override
  @Nullable
  public HaxeEnumConstructorParameters getEnumConstructorParameters() {
    return findChildByClass(HaxeEnumConstructorParameters.class);
  }

}
