// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.intellij.plugins.haxe.lang.lexer.HaxeTokenTypes.*;
import com.intellij.plugins.haxe.lang.psi.*;

public class HaxeParameterImpl extends AbstractHaxeNamedComponent implements HaxeParameter {

  public HaxeParameterImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof HaxeVisitor) ((HaxeVisitor)visitor).visitParameter(this);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public HaxeComponentName getComponentName() {
    return findNotNullChildByClass(HaxeComponentName.class);
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
