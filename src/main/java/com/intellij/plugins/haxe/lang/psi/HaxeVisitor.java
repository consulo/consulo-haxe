// This is a generated file. Not intended for manual editing.
package com.intellij.plugins.haxe.lang.psi;

import javax.annotation.Nonnull;

import com.intellij.psi.PsiElementVisitor;

public class HaxeVisitor extends PsiElementVisitor {

  public void visitAbstractClassDeclaration(@Nonnull HaxeAbstractClassDeclaration o) {
    visitClass(o);
  }

  public void visitAccess(@Nonnull HaxeAccess o) {
    visitPsiCompositeElement(o);
  }

  public void visitAdditiveExpression(@Nonnull HaxeAdditiveExpression o) {
    visitExpression(o);
  }

  public void visitAnonymousFunctionDeclaration(@Nonnull HaxeAnonymousFunctionDeclaration o) {
    visitPsiCompositeElement(o);
  }

  public void visitAnonymousType(@Nonnull HaxeAnonymousType o) {
    visitClass(o);
  }

  public void visitAnonymousTypeBody(@Nonnull HaxeAnonymousTypeBody o) {
    visitPsiCompositeElement(o);
  }

  public void visitAnonymousTypeField(@Nonnull HaxeAnonymousTypeField o) {
    visitComponent(o);
  }

  public void visitAnonymousTypeFieldList(@Nonnull HaxeAnonymousTypeFieldList o) {
    visitPsiCompositeElement(o);
  }

  public void visitArrayAccessExpression(@Nonnull HaxeArrayAccessExpression o) {
    visitExpression(o);
    // visitReference(o);
  }

  public void visitArrayLiteral(@Nonnull HaxeArrayLiteral o) {
    visitExpression(o);
    // visitReference(o);
  }

  public void visitAssignExpression(@Nonnull HaxeAssignExpression o) {
    visitExpression(o);
  }

  public void visitAssignOperation(@Nonnull HaxeAssignOperation o) {
    visitPsiCompositeElement(o);
  }

  public void visitAutoBuildMacro(@Nonnull HaxeAutoBuildMacro o) {
    visitPsiCompositeElement(o);
  }

  public void visitBitOperation(@Nonnull HaxeBitOperation o) {
    visitPsiCompositeElement(o);
  }

  public void visitBitmapMeta(@Nonnull HaxeBitmapMeta o) {
    visitPsiCompositeElement(o);
  }

  public void visitBitwiseExpression(@Nonnull HaxeBitwiseExpression o) {
    visitExpression(o);
  }

  public void visitBlockStatement(@Nonnull HaxeBlockStatement o) {
    visitPsiCompositeElement(o);
  }

  public void visitBreakStatement(@Nonnull HaxeBreakStatement o) {
    visitPsiCompositeElement(o);
  }

  public void visitBuildMacro(@Nonnull HaxeBuildMacro o) {
    visitPsiCompositeElement(o);
  }

  public void visitCallExpression(@Nonnull HaxeCallExpression o) {
    visitExpression(o);
    // visitReference(o);
  }

  public void visitCastExpression(@Nonnull HaxeCastExpression o) {
    visitExpression(o);
    // visitReference(o);
  }

  public void visitCatchStatement(@Nonnull HaxeCatchStatement o) {
    visitPsiCompositeElement(o);
  }

  public void visitClassBody(@Nonnull HaxeClassBody o) {
    visitPsiCompositeElement(o);
  }

  public void visitClassDeclaration(@Nonnull HaxeClassDeclaration o) {
    visitClass(o);
  }

  public void visitCompareExpression(@Nonnull HaxeCompareExpression o) {
    visitExpression(o);
  }

  public void visitCompareOperation(@Nonnull HaxeCompareOperation o) {
    visitPsiCompositeElement(o);
  }

  public void visitComponentName(@Nonnull HaxeComponentName o) {
    visitNamedElement(o);
  }

  public void visitConditional(@Nonnull HaxeConditional o) {
    visitPsiCompositeElement(o);
  }

  public void visitContinueStatement(@Nonnull HaxeContinueStatement o) {
    visitPsiCompositeElement(o);
  }

  public void visitCustomMeta(@Nonnull HaxeCustomMeta o) {
    visitPsiCompositeElement(o);
  }

  public void visitDeclarationAttribute(@Nonnull HaxeDeclarationAttribute o) {
    visitPsiCompositeElement(o);
  }

  public void visitDefaultCase(@Nonnull HaxeDefaultCase o) {
    visitPsiCompositeElement(o);
  }

  public void visitDoWhileStatement(@Nonnull HaxeDoWhileStatement o) {
    visitPsiCompositeElement(o);
  }

  public void visitEnumBody(@Nonnull HaxeEnumBody o) {
    visitPsiCompositeElement(o);
  }

  public void visitEnumConstructorParameters(@Nonnull HaxeEnumConstructorParameters o) {
    visitPsiCompositeElement(o);
  }

  public void visitEnumDeclaration(@Nonnull HaxeEnumDeclaration o) {
    visitClass(o);
  }

  public void visitEnumValueDeclaration(@Nonnull HaxeEnumValueDeclaration o) {
    visitComponent(o);
  }

  public void visitExpression(@Nonnull HaxeExpression o) {
    visitPsiCompositeElement(o);
  }

  public void visitExpressionList(@Nonnull HaxeExpressionList o) {
    visitPsiCompositeElement(o);
  }

  public void visitExternClassDeclaration(@Nonnull HaxeExternClassDeclaration o) {
    visitClass(o);
  }

  public void visitExternClassDeclarationBody(@Nonnull HaxeExternClassDeclarationBody o) {
    visitPsiCompositeElement(o);
  }

  public void visitExternFunctionDeclaration(@Nonnull HaxeExternFunctionDeclaration o) {
    visitComponentWithDeclarationList(o);
  }

  public void visitExternOrPrivate(@Nonnull HaxeExternOrPrivate o) {
    visitPsiCompositeElement(o);
  }

  public void visitFakeEnumMeta(@Nonnull HaxeFakeEnumMeta o) {
    visitPsiCompositeElement(o);
  }

  public void visitFatArrowExpression(@Nonnull HaxeFatArrowExpression o) {
    visitExpression(o);
  }

  public void visitForStatement(@Nonnull HaxeForStatement o) {
    visitComponent(o);
  }

  public void visitFunctionDeclarationWithAttributes(@Nonnull HaxeFunctionDeclarationWithAttributes o) {
    visitComponentWithDeclarationList(o);
  }

  public void visitFunctionLiteral(@Nonnull HaxeFunctionLiteral o) {
    visitExpression(o);
  }

  public void visitFunctionPrototypeDeclarationWithAttributes(@Nonnull HaxeFunctionPrototypeDeclarationWithAttributes o) {
    visitComponentWithDeclarationList(o);
  }

  public void visitFunctionType(@Nonnull HaxeFunctionType o) {
    visitPsiCompositeElement(o);
  }

  public void visitGenericListPart(@Nonnull HaxeGenericListPart o) {
    visitComponent(o);
  }

  public void visitGenericParam(@Nonnull HaxeGenericParam o) {
    visitPsiCompositeElement(o);
  }

  public void visitGetterMeta(@Nonnull HaxeGetterMeta o) {
    visitPsiCompositeElement(o);
  }

  public void visitIdentifier(@Nonnull HaxeIdentifier o) {
    visitPsiCompositeElement(o);
  }

  public void visitIfStatement(@Nonnull HaxeIfStatement o) {
    visitPsiCompositeElement(o);
  }

  public void visitImportStatementRegular(@Nonnull HaxeImportStatementRegular o) {
    visitPsiCompositeElement(o);
  }

  public void visitImportStatementWithInSupport(@Nonnull HaxeImportStatementWithInSupport o) {
    visitPsiCompositeElement(o);
  }

  public void visitImportStatementWithWildcard(@Nonnull HaxeImportStatementWithWildcard o) {
    visitPsiCompositeElement(o);
  }

  public void visitInherit(@Nonnull HaxeInherit o) {
    visitPsiCompositeElement(o);
  }

  public void visitInheritList(@Nonnull HaxeInheritList o) {
    visitPsiCompositeElement(o);
  }

  public void visitInterfaceBody(@Nonnull HaxeInterfaceBody o) {
    visitPsiCompositeElement(o);
  }

  public void visitInterfaceDeclaration(@Nonnull HaxeInterfaceDeclaration o) {
    visitClass(o);
  }

  public void visitIterable(@Nonnull HaxeIterable o) {
    visitPsiCompositeElement(o);
  }

  public void visitIteratorExpression(@Nonnull HaxeIteratorExpression o) {
    visitExpression(o);
  }

  public void visitJsRequireMeta(@Nonnull HaxeJsRequireMeta o) {
    visitPsiCompositeElement(o);
  }

  public void visitLiteralExpression(@Nonnull HaxeLiteralExpression o) {
    visitExpression(o);
    // visitReference(o);
  }

  public void visitLocalFunctionDeclaration(@Nonnull HaxeLocalFunctionDeclaration o) {
    visitComponent(o);
  }

  public void visitLocalVarDeclaration(@Nonnull HaxeLocalVarDeclaration o) {
    visitPsiCompositeElement(o);
  }

  public void visitLocalVarDeclarationPart(@Nonnull HaxeLocalVarDeclarationPart o) {
    visitComponent(o);
  }

  public void visitLogicAndExpression(@Nonnull HaxeLogicAndExpression o) {
    visitExpression(o);
  }

  public void visitLogicOrExpression(@Nonnull HaxeLogicOrExpression o) {
    visitExpression(o);
  }

  public void visitLongTemplateEntry(@Nonnull HaxeLongTemplateEntry o) {
    visitPsiCompositeElement(o);
  }

  public void visitMetaKeyValue(@Nonnull HaxeMetaKeyValue o) {
    visitPsiCompositeElement(o);
  }

  public void visitMetaMeta(@Nonnull HaxeMetaMeta o) {
    visitPsiCompositeElement(o);
  }

  public void visitMultiplicativeExpression(@Nonnull HaxeMultiplicativeExpression o) {
    visitExpression(o);
  }

  public void visitNativeMeta(@Nonnull HaxeNativeMeta o) {
    visitPsiCompositeElement(o);
  }

  public void visitNewExpression(@Nonnull HaxeNewExpression o) {
    visitExpression(o);
    // visitReference(o);
  }

  public void visitNsMeta(@Nonnull HaxeNsMeta o) {
    visitPsiCompositeElement(o);
  }

  public void visitObjectLiteral(@Nonnull HaxeObjectLiteral o) {
    visitExpression(o);
  }

  public void visitObjectLiteralElement(@Nonnull HaxeObjectLiteralElement o) {
    visitPsiCompositeElement(o);
  }

  public void visitOverloadMeta(@Nonnull HaxeOverloadMeta o) {
    visitPsiCompositeElement(o);
  }

  public void visitPackageStatement(@Nonnull HaxePackageStatement o) {
    visitPsiCompositeElement(o);
  }

  public void visitParameter(@Nonnull HaxeParameter o) {
    visitComponent(o);
  }

  public void visitParameterList(@Nonnull HaxeParameterList o) {
    visitPsiCompositeElement(o);
  }

  public void visitParenthesizedExpression(@Nonnull HaxeParenthesizedExpression o) {
    visitExpression(o);
  }

  public void visitPrefixExpression(@Nonnull HaxePrefixExpression o) {
    visitExpression(o);
  }

  public void visitPropertyAccessor(@Nonnull HaxePropertyAccessor o) {
    visitPsiCompositeElement(o);
  }

  public void visitPropertyDeclaration(@Nonnull HaxePropertyDeclaration o) {
    visitPsiCompositeElement(o);
  }

  public void visitReferenceExpression(@Nonnull HaxeReferenceExpression o) {
    visitExpression(o);
    // visitReference(o);
  }

  public void visitRegularExpressionLiteral(@Nonnull HaxeRegularExpressionLiteral o) {
    visitLiteralExpression(o);
    // visitRegularExpression(o);
  }

  public void visitRequireMeta(@Nonnull HaxeRequireMeta o) {
    visitPsiCompositeElement(o);
  }

  public void visitReturnStatement(@Nonnull HaxeReturnStatement o) {
    visitPsiCompositeElement(o);
  }

  public void visitReturnStatementWithoutSemicolon(@Nonnull HaxeReturnStatementWithoutSemicolon o) {
    visitPsiCompositeElement(o);
  }

  public void visitSetterMeta(@Nonnull HaxeSetterMeta o) {
    visitPsiCompositeElement(o);
  }

  public void visitShiftExpression(@Nonnull HaxeShiftExpression o) {
    visitExpression(o);
  }

  public void visitShiftOperator(@Nonnull HaxeShiftOperator o) {
    visitPsiCompositeElement(o);
  }

  public void visitShiftRightOperator(@Nonnull HaxeShiftRightOperator o) {
    visitPsiCompositeElement(o);
  }

  public void visitShortTemplateEntry(@Nonnull HaxeShortTemplateEntry o) {
    visitPsiCompositeElement(o);
  }

  public void visitStringLiteralExpression(@Nonnull HaxeStringLiteralExpression o) {
    visitExpression(o);
    // visitReference(o);
  }

  public void visitSuffixExpression(@Nonnull HaxeSuffixExpression o) {
    visitExpression(o);
  }

  public void visitSuperExpression(@Nonnull HaxeSuperExpression o) {
    visitExpression(o);
    // visitReference(o);
  }

  public void visitSwitchBlock(@Nonnull HaxeSwitchBlock o) {
    visitPsiCompositeElement(o);
  }

  public void visitSwitchCase(@Nonnull HaxeSwitchCase o) {
    visitPsiCompositeElement(o);
  }

  public void visitSwitchCaseBlock(@Nonnull HaxeSwitchCaseBlock o) {
    visitPsiCompositeElement(o);
  }

  public void visitSwitchCaseExpression(@Nonnull HaxeSwitchCaseExpression o) {
    visitExpression(o);
  }

  public void visitSwitchStatement(@Nonnull HaxeSwitchStatement o) {
    visitPsiCompositeElement(o);
  }

  public void visitTernaryExpression(@Nonnull HaxeTernaryExpression o) {
    visitExpression(o);
  }

  public void visitThisExpression(@Nonnull HaxeThisExpression o) {
    visitExpression(o);
    // visitReference(o);
  }

  public void visitThrowStatement(@Nonnull HaxeThrowStatement o) {
    visitPsiCompositeElement(o);
  }

  public void visitTryStatement(@Nonnull HaxeTryStatement o) {
    visitPsiCompositeElement(o);
  }

  public void visitType(@Nonnull HaxeType o) {
    visitPsiCompositeElement(o);
  }

  public void visitTypeExtends(@Nonnull HaxeTypeExtends o) {
    visitPsiCompositeElement(o);
  }

  public void visitTypeList(@Nonnull HaxeTypeList o) {
    visitPsiCompositeElement(o);
  }

  public void visitTypeListPart(@Nonnull HaxeTypeListPart o) {
    visitPsiCompositeElement(o);
  }

  public void visitTypeOrAnonymous(@Nonnull HaxeTypeOrAnonymous o) {
    visitPsiCompositeElement(o);
  }

  public void visitTypeParam(@Nonnull HaxeTypeParam o) {
    visitPsiCompositeElement(o);
  }

  public void visitTypeTag(@Nonnull HaxeTypeTag o) {
    visitPsiCompositeElement(o);
  }

  public void visitTypedefDeclaration(@Nonnull HaxeTypedefDeclaration o) {
    visitClass(o);
  }

  public void visitUnsignedShiftRightOperator(@Nonnull HaxeUnsignedShiftRightOperator o) {
    visitPsiCompositeElement(o);
  }

  public void visitUsingStatement(@Nonnull HaxeUsingStatement o) {
    visitPsiCompositeElement(o);
  }

  public void visitVarDeclaration(@Nonnull HaxeVarDeclaration o) {
    visitPsiCompositeElement(o);
  }

  public void visitVarDeclarationPart(@Nonnull HaxeVarDeclarationPart o) {
    visitComponent(o);
  }

  public void visitVarInit(@Nonnull HaxeVarInit o) {
    visitPsiCompositeElement(o);
  }

  public void visitWhileStatement(@Nonnull HaxeWhileStatement o) {
    visitPsiCompositeElement(o);
  }

  public void visitWildcard(@Nonnull HaxeWildcard o) {
    visitPsiCompositeElement(o);
  }

  public void visitClass(@Nonnull HaxeClass o) {
    visitPsiCompositeElement(o);
  }

  public void visitComponent(@Nonnull HaxeComponent o) {
    visitPsiCompositeElement(o);
  }

  public void visitComponentWithDeclarationList(@Nonnull HaxeComponentWithDeclarationList o) {
    visitPsiCompositeElement(o);
  }

  public void visitNamedElement(@Nonnull HaxeNamedElement o) {
    visitPsiCompositeElement(o);
  }

  public void visitPsiCompositeElement(@Nonnull HaxePsiCompositeElement o) {
    visitElement(o);
  }

}
