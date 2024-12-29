/*
 * Copyright 2013-2014 must-be.org
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

package consulo.haxe.psi.impl;

import com.intellij.plugins.haxe.lang.psi.HaxePackage;
import consulo.document.util.TextRange;
import consulo.language.psi.*;
import consulo.language.util.IncorrectOperationException;
import consulo.localize.LocalizeValue;

import jakarta.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class HaxePsiPackageReference extends PsiPolyVariantReferenceBase<PsiElement> implements EmptyResolveMessageProvider {
  private final HaxePsiPackageReferenceSet myReferenceSet;
  private final int myIndex;

  public HaxePsiPackageReference(final HaxePsiPackageReferenceSet set, final TextRange range, final int index) {
    super(set.getElement(), range, set.isSoft());
    myReferenceSet = set;
    myIndex = index;
  }

  @Nonnull
  private Set<HaxePackage> getContext() {
    if (myIndex == 0) {
      return myReferenceSet.getInitialContext();
    }
    Set<HaxePackage> psiPackages = new HashSet<>();
    for (ResolveResult resolveResult : myReferenceSet.getReference(myIndex - 1).multiResolve(false)) {
      PsiElement psiElement = resolveResult.getElement();
      if (psiElement instanceof HaxePackage) {
        psiPackages.add((HaxePackage)psiElement);
      }
    }
    return psiPackages;
  }

  @Override
  @Nonnull
  public Object[] getVariants() {
    Set<HaxePackage> subPackages = new HashSet<>();
    for (HaxePackage psiPackage : getContext()) {
      subPackages.addAll(Arrays.asList(psiPackage.getSubPackages()));
    }

    return subPackages.toArray();
  }

  @Nonnull
  @Override
  public LocalizeValue buildUnresolvedMessage(@Nonnull String s) {
    return LocalizeValue.localizeTODO("Cant not resolve package");
  }

  @Override
  @Nonnull
  public ResolveResult[] multiResolve(final boolean incompleteCode) {
    final Collection<HaxePackage> packages = new HashSet<>();
    for (HaxePackage parentPackage : getContext()) {
      packages.addAll(myReferenceSet.resolvePackageName(parentPackage, getValue()));
    }
    return PsiElementResolveResult.createResults(packages);
  }

  @Override
  public PsiElement bindToElement(@Nonnull final PsiElement element) throws IncorrectOperationException {
    if (!(element instanceof HaxePackage)) {
      throw new IncorrectOperationException("Cannot bind to " + element);
    }
    final String newName = ((HaxePackage)element).getQualifiedName();
    final TextRange range = new TextRange(getReferenceSet().getReference(0).getRangeInElement().getStartOffset(),
                                          getRangeInElement().getEndOffset());
    final ElementManipulator<PsiElement> manipulator = ElementManipulators.getManipulator(getElement());
    return manipulator.handleContentChange(getElement(), range, newName);
  }

  public HaxePsiPackageReferenceSet getReferenceSet() {
    return myReferenceSet;
  }
}
