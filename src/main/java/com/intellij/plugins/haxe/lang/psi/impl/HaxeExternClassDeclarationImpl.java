// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi.impl;

import java.util.List;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;

import javax.annotation.*;

import com.intellij.plugins.haxe.lang.psi.*;

public class HaxeExternClassDeclarationImpl extends AbstractHaxePsiClass implements HaxeExternClassDeclaration {

  public HaxeExternClassDeclarationImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof HaxeVisitor) ((HaxeVisitor)visitor).visitExternClassDeclaration(this);
    else super.accept(visitor);
  }

  @Override
  @Nonnull
  public List<HaxeAutoBuildMacro> getAutoBuildMacroList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaxeAutoBuildMacro.class);
  }

  @Override
  @Nonnull
  public List<HaxeBitmapMeta> getBitmapMetaList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaxeBitmapMeta.class);
  }

  @Override
  @Nonnull
  public List<HaxeBuildMacro> getBuildMacroList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaxeBuildMacro.class);
  }

  @Override
  @Nonnull
  public HaxeComponentName getComponentName() {
    return findNotNullChildByClass(HaxeComponentName.class);
  }

  @Override
  @Nonnull
  public List<HaxeCustomMeta> getCustomMetaList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaxeCustomMeta.class);
  }

  @Override
  @Nullable
  public HaxeExternClassDeclarationBody getExternClassDeclarationBody() {
    return findChildByClass(HaxeExternClassDeclarationBody.class);
  }

  @Override
  @Nonnull
  public List<HaxeExternOrPrivate> getExternOrPrivateList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaxeExternOrPrivate.class);
  }

  @Override
  @Nonnull
  public List<HaxeFakeEnumMeta> getFakeEnumMetaList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaxeFakeEnumMeta.class);
  }

  @Override
  @Nullable
  public HaxeGenericParam getGenericParam() {
    return findChildByClass(HaxeGenericParam.class);
  }

  @Override
  @Nullable
  public HaxeInheritList getInheritList() {
    return findChildByClass(HaxeInheritList.class);
  }

  @Override
  @Nonnull
  public List<HaxeJsRequireMeta> getJsRequireMetaList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaxeJsRequireMeta.class);
  }

  @Override
  @Nonnull
  public List<HaxeMetaMeta> getMetaMetaList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaxeMetaMeta.class);
  }

  @Override
  @Nonnull
  public List<HaxeNativeMeta> getNativeMetaList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaxeNativeMeta.class);
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

}
