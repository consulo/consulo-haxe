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

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import consulo.haxe.module.extension.packageSupport.HaxePackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.TextRange;
import com.intellij.plugins.haxe.lang.psi.HaxePackage;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.ReferenceSetBase;
import com.intellij.util.NullableFunction;
import com.intellij.util.containers.ContainerUtil;

/**
 * @author Dmitry Avdeev
 */
public class HaxePsiPackageReferenceSet extends ReferenceSetBase<HaxePsiPackageReference>
{

	public HaxePsiPackageReferenceSet(@NotNull final String str, @NotNull final PsiElement element, final int startInElement)
	{
		super(str, element, startInElement, DOT_SEPARATOR);
	}

	@Override
	@NotNull
	protected HaxePsiPackageReference createReference(final TextRange range, final int index)
	{
		return new HaxePsiPackageReference(this, range, index);
	}

	public Collection<HaxePackage> resolvePackageName(@Nullable HaxePackage context, final String packageName)
	{
		if(context != null)
		{
			return ContainerUtil.filter(context.getSubPackages(), new Condition<HaxePackage>()
			{
				@Override
				public boolean value(HaxePackage aPackage)
				{
					return Comparing.equal(aPackage.getName(), packageName);
				}
			});
		}
		return Collections.emptyList();
	}

	public Collection<HaxePackage> resolvePackage()
	{
		final HaxePsiPackageReference packageReference = getLastReference();
		if(packageReference == null)
		{
			return Collections.emptyList();
		}
		return ContainerUtil.map2List(packageReference.multiResolve(false), new NullableFunction<ResolveResult, HaxePackage>()
		{
			@Override
			public HaxePackage fun(final ResolveResult resolveResult)
			{
				return (HaxePackage) resolveResult.getElement();
			}
		});
	}

	public Set<HaxePackage> getInitialContext()
	{
		return Collections.singleton(HaxePackageUtil.findPackage(getElement().getProject(), ""));
	}
}