// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi.impl;

import java.util.List;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;

import javax.annotation.Nonnull;

import com.intellij.plugins.haxe.lang.psi.*;

public class HaxeVarDeclarationImpl extends HaxePsiCompositeElementImpl implements HaxeVarDeclaration {

  public HaxeVarDeclarationImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof HaxeVisitor) ((HaxeVisitor)visitor).visitVarDeclaration(this);
    else super.accept(visitor);
  }

  @Override
  @Nonnull
  public List<HaxeAutoBuildMacro> getAutoBuildMacroList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaxeAutoBuildMacro.class);
  }

  @Override
  @Nonnull
  public List<HaxeBuildMacro> getBuildMacroList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaxeBuildMacro.class);
  }

  @Override
  @Nonnull
  public List<HaxeCustomMeta> getCustomMetaList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaxeCustomMeta.class);
  }

  @Override
  @Nonnull
  public List<HaxeDeclarationAttribute> getDeclarationAttributeList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaxeDeclarationAttribute.class);
  }

  @Override
  @Nonnull
  public List<HaxeGetterMeta> getGetterMetaList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaxeGetterMeta.class);
  }

  @Override
  @Nonnull
  public List<HaxeMetaMeta> getMetaMetaList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaxeMetaMeta.class);
  }

  @Override
  @Nonnull
  public List<HaxeNsMeta> getNsMetaList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaxeNsMeta.class);
  }

  @Override
  @Nonnull
  public List<HaxeRequireMeta> getRequireMetaList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaxeRequireMeta.class);
  }

  @Override
  @Nonnull
  public List<HaxeSetterMeta> getSetterMetaList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaxeSetterMeta.class);
  }

  @Override
  @Nonnull
  public List<HaxeVarDeclarationPart> getVarDeclarationPartList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaxeVarDeclarationPart.class);
  }

}
