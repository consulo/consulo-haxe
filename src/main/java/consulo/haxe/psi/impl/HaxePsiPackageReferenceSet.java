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
import consulo.haxe.module.extension.packageSupport.HaxePackageUtil;
import consulo.language.psi.PsiElement;
import consulo.language.psi.ReferenceSetBase;
import consulo.util.collection.ContainerUtil;
import consulo.util.lang.Comparing;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * @author Dmitry Avdeev
 */
public class HaxePsiPackageReferenceSet extends ReferenceSetBase<HaxePsiPackageReference> {

  public HaxePsiPackageReferenceSet(@Nonnull final String str, @Nonnull final PsiElement element, final int startInElement) {
    super(str, element, startInElement, DOT_SEPARATOR);
  }

  @Override
  @Nonnull
  protected HaxePsiPackageReference createReference(final TextRange range, final int index) {
    return new HaxePsiPackageReference(this, range, index);
  }

  public Collection<HaxePackage> resolvePackageName(@Nullable HaxePackage context, final String packageName) {
    if (context != null) {
      return ContainerUtil.filter(context.getSubPackages(), aPackage -> Comparing.equal(aPackage.getName(), packageName));
    }
    return Collections.emptyList();
  }

  public Collection<HaxePackage> resolvePackage() {
    final HaxePsiPackageReference packageReference = getLastReference();
    if (packageReference == null) {
      return Collections.emptyList();
    }
    return ContainerUtil.map2List(packageReference.multiResolve(false), resolveResult -> (HaxePackage) resolveResult.getElement());
  }

  public Set<HaxePackage> getInitialContext() {
    return Collections.singleton(HaxePackageUtil.findPackage(getElement().getProject(), ""));
  }
}