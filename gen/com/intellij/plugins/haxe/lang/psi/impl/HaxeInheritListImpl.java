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

public class HaxeInheritListImpl extends HaxePsiCompositeElementImpl implements HaxeInheritList {

  public HaxeInheritListImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof HaxeVisitor) ((HaxeVisitor)visitor).visitInheritList(this);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<HaxeInherit> getInheritList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaxeInherit.class);
  }

}
