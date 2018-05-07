// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

import javax.annotation.*;

import com.intellij.plugins.haxe.lang.psi.*;

public class HaxeAnonymousTypeBodyImpl extends HaxePsiCompositeElementImpl implements HaxeAnonymousTypeBody {

  public HaxeAnonymousTypeBodyImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof HaxeVisitor) ((HaxeVisitor)visitor).visitAnonymousTypeBody(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public HaxeAnonymousTypeFieldList getAnonymousTypeFieldList() {
    return findChildByClass(HaxeAnonymousTypeFieldList.class);
  }

  @Override
  @Nullable
  public HaxeInterfaceBody getInterfaceBody() {
    return findChildByClass(HaxeInterfaceBody.class);
  }

  @Override
  @Nullable
  public HaxeTypeExtends getTypeExtends() {
    return findChildByClass(HaxeTypeExtends.class);
  }

}
