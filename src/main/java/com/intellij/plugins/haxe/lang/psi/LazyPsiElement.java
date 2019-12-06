/*
 * Copyright 2000-2013 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intellij.plugins.haxe.lang.psi;

import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.util.Function;
import com.intellij.util.IncorrectOperationException;
import consulo.lang.LanguageVersion;
import consulo.util.dataholder.Key;
import org.jetbrains.annotations.NonNls;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author: Fedor.Korotkov
 */
public class LazyPsiElement implements PsiElement {
  private final Function<Void, PsiElement> myCreator;
  private PsiElement element = null;

  public LazyPsiElement(Function<Void, PsiElement> creator) {
    myCreator = creator;
  }

  @Nonnull
  public PsiElement getElement() {
    if (element == null) {
      element = myCreator.fun(null);
    }
    return element;
  }

  @Nonnull
  @Override
  public Project getProject() throws PsiInvalidElementAccessException {
    return getElement().getProject();
  }

  @Nonnull
  @Override
  public Language getLanguage() {
    return getElement().getLanguage();
  }

  @Nonnull
  @Override
  public LanguageVersion getLanguageVersion() {
    return getElement().getLanguageVersion();
  }

  @Override
  public PsiManager getManager() {
    return getElement().getManager();
  }

  @Nonnull
  @Override
  public PsiElement[] getChildren() {
    return getElement().getChildren();
  }

  @Override
  public PsiElement getParent() {
    return getElement().getParent();
  }

  @Override
  public PsiElement getFirstChild() {
    return getElement().getFirstChild();
  }

  @Override
  public PsiElement getLastChild() {
    return getElement().getLastChild();
  }

  @Override
  public PsiElement getNextSibling() {
    return getElement().getNextSibling();
  }

  @Override
  public PsiElement getPrevSibling() {
    return getElement().getPrevSibling();
  }

  @Override
  public PsiFile getContainingFile() throws PsiInvalidElementAccessException {
    return getElement().getContainingFile();
  }

  @Override
  public TextRange getTextRange() {
    return getElement().getTextRange();
  }

  @Override
  public int getStartOffsetInParent() {
    return getElement().getStartOffsetInParent();
  }

  @Override
  public int getTextLength() {
    return getElement().getTextLength();
  }

  @Override
  public PsiElement findElementAt(int offset) {
    return getElement().findElementAt(offset);
  }

  @Override
  public PsiReference findReferenceAt(int offset) {
    return getElement().findReferenceAt(offset);
  }

  @Override
  public int getTextOffset() {
    return getElement().getTextOffset();
  }

  @Override
  public String getText() {
    return getElement().getText();
  }

  @Nonnull
  @Override
  public char[] textToCharArray() {
    return getElement().textToCharArray();
  }

  @Override
  public PsiElement getNavigationElement() {
    return getElement().getNavigationElement();
  }

  @Override
  public PsiElement getOriginalElement() {
    return getElement().getOriginalElement();
  }

  @Override
  public boolean textMatches(@Nonnull @NonNls CharSequence text) {
    return getElement().textMatches(text);
  }

  @Override
  public boolean textMatches(@Nonnull PsiElement element) {
    return getElement().textMatches(element);
  }

  @Override
  public boolean textContains(char c) {
    return getElement().textContains(c);
  }

  @Override
  public void accept(@Nonnull PsiElementVisitor visitor) {
    getElement().accept(visitor);
  }

  @Override
  public void acceptChildren(@Nonnull PsiElementVisitor visitor) {
    getElement().acceptChildren(visitor);
  }

  @Override
  public PsiElement copy() {
    return getElement().copy();
  }

  @Override
  public PsiElement add(@Nonnull PsiElement element) throws IncorrectOperationException {
    return getElement().add(element);
  }

  @Override
  public PsiElement addBefore(@Nonnull PsiElement element, PsiElement anchor) throws IncorrectOperationException {
    return getElement().addBefore(element, anchor);
  }

  @Override
  public PsiElement addAfter(@Nonnull PsiElement element, PsiElement anchor) throws IncorrectOperationException {
    return getElement().addAfter(element, anchor);
  }

  @Override
  public void checkAdd(@Nonnull PsiElement element) throws IncorrectOperationException {
    getElement().checkAdd(element);
  }

  @Override
  public PsiElement addRange(PsiElement first, PsiElement last) throws IncorrectOperationException {
    return getElement().addRange(first, last);
  }

  @Override
  public PsiElement addRangeBefore(@Nonnull PsiElement first, @Nonnull PsiElement last, PsiElement anchor)
    throws IncorrectOperationException {
    return getElement().addRangeBefore(first, last, anchor);
  }

  @Override
  public PsiElement addRangeAfter(PsiElement first, PsiElement last, PsiElement anchor) throws IncorrectOperationException {
    return getElement().addRangeAfter(first, last, anchor);
  }

  @Override
  public void delete() throws IncorrectOperationException {
    getElement().delete();
  }

  @Override
  public void checkDelete() throws IncorrectOperationException {
    getElement().checkDelete();
  }

  @Override
  public void deleteChildRange(PsiElement first, PsiElement last) throws IncorrectOperationException {
    getElement().deleteChildRange(first, last);
  }

  @Override
  public PsiElement replace(@Nonnull PsiElement newElement) throws IncorrectOperationException {
    return getElement().replace(newElement);
  }

  @Override
  public boolean isValid() {
    return getElement().isValid();
  }

  @Override
  public boolean isWritable() {
    return getElement().isWritable();
  }

  @Override
  public PsiReference getReference() {
    return getElement().getReference();
  }

  @Nonnull
  @Override
  public PsiReference[] getReferences() {
    return getElement().getReferences();
  }

  @Override
  public <T> T getCopyableUserData(Key<T> key) {
    return getElement().getCopyableUserData(key);
  }

  @Override
  public <T> void putCopyableUserData(Key<T> key, @Nullable T value) {
    getElement().putCopyableUserData(key, value);
  }

  @Override
  public boolean processDeclarations(@Nonnull PsiScopeProcessor processor,
                                     @Nonnull ResolveState state,
                                     @Nullable PsiElement lastParent,
                                     @Nonnull PsiElement place) {
    return getElement().processDeclarations(processor, state, lastParent, place);
  }

  @Override
  public PsiElement getContext() {
    return getElement().getContext();
  }

  @Override
  public boolean isPhysical() {
    return getElement().isPhysical();
  }

  @Nonnull
  @Override
  public GlobalSearchScope getResolveScope() {
    return getElement().getResolveScope();
  }

  @Nonnull
  @Override
  public SearchScope getUseScope() {
    return getElement().getUseScope();
  }

  @Override
  public ASTNode getNode() {
    return getElement().getNode();
  }

  @Override
  public boolean isEquivalentTo(PsiElement another) {
    return getElement().isEquivalentTo(another);
  }

  @Override
  public <T> T getUserData(@Nonnull Key<T> key) {
    return getElement().getUserData(key);
  }

  @Override
  public <T> void putUserData(@Nonnull Key<T> key, @Nullable T value) {
    getElement().putUserData(key, value);
  }
}
