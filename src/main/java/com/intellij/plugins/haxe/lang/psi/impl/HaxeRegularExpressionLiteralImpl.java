// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

import javax.annotation.Nonnull;

import com.intellij.plugins.haxe.lang.psi.*;

public class HaxeRegularExpressionLiteralImpl extends HaxeRegularExpressionImpl implements HaxeRegularExpressionLiteral {

  public HaxeRegularExpressionLiteralImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof HaxeVisitor) ((HaxeVisitor)visitor).visitRegularExpressionLiteral(this);
    else super.accept(visitor);
  }

}
