// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

import javax.annotation.*;

import com.intellij.plugins.haxe.lang.psi.*;

public class HaxeOverloadMetaImpl extends HaxePsiCompositeElementImpl implements HaxeOverloadMeta {

  public HaxeOverloadMetaImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof HaxeVisitor) ((HaxeVisitor)visitor).visitOverloadMeta(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public HaxeAnonymousFunctionDeclaration getAnonymousFunctionDeclaration() {
    return findChildByClass(HaxeAnonymousFunctionDeclaration.class);
  }

}
