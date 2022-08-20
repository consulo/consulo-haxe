/*
 * Copyright 2000-2013 JetBrains s.r.o.
 * Copyright 2014-2014 AS3Boyan
 * Copyright 2014-2014 Elias Ku
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
package com.intellij.plugins.haxe.lang.psi.impl;

import com.intellij.plugins.haxe.HaxeComponentType;
import com.intellij.plugins.haxe.lang.psi.*;
import com.intellij.plugins.haxe.util.HaxeResolveUtil;
import consulo.language.ast.ASTNode;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.util.collection.ContainerUtil;
import consulo.util.io.FileUtil;
import consulo.util.lang.function.Condition;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author: Fedor.Korotkov
 */
public abstract class AbstractHaxePsiClass extends AbstractHaxeNamedComponent implements HaxeClass
{
	public AbstractHaxePsiClass(@Nonnull ASTNode node)
	{
		super(node);
	}

	@Override
	public HaxeNamedComponent getTypeComponent()
	{
		return this;
	}

	@Nonnull
	@Override
	public String getQualifiedName()
	{
		final String name = getName();
		if(getParent() == null)
		{
			return name == null ? "" : name;
		}
		final String fileName = FileUtil.getNameWithoutExtension(getContainingFile().getName());
		String packageName = HaxeResolveUtil.getPackageName(getContainingFile());
		if(notPublicClass(name, fileName))
		{
			packageName = HaxeResolveUtil.joinQName(packageName, fileName);
		}
		return HaxeResolveUtil.joinQName(packageName, name);
	}

	private boolean notPublicClass(String name, String fileName)
	{
		if(this instanceof HaxeExternClassDeclaration)
		{
			return false;
		}
		return !fileName.equals(name) && HaxeResolveUtil.findComponentDeclaration(getContainingFile(), fileName) != null;
	}

	@Override
	public boolean isInterface()
	{
		return HaxeComponentType.typeOf(this) == HaxeComponentType.INTERFACE;
	}

	@Nonnull
	@Override
	public List<HaxeType> getExtendsList()
	{
		return HaxeResolveUtil.findExtendsList(PsiTreeUtil.getChildOfType(this, HaxeInheritList.class));
	}

	@Nonnull
	@Override
	public List<HaxeType> getImplementsList()
	{
		return HaxeResolveUtil.getImplementsList(PsiTreeUtil.getChildOfType(this, HaxeInheritList.class));
	}

	@Nonnull
	@Override
	public List<HaxeNamedComponent> getMethods()
	{
		final List<HaxeNamedComponent> result = HaxeResolveUtil.findNamedSubComponents(this);
		return HaxeResolveUtil.filterNamedComponentsByType(result, HaxeComponentType.METHOD);
	}

	@Nonnull
	@Override
	public List<HaxeNamedComponent> getFields()
	{
		final List<HaxeNamedComponent> result = HaxeResolveUtil.findNamedSubComponents(this);
		return HaxeResolveUtil.filterNamedComponentsByType(result, HaxeComponentType.FIELD);
	}

	@Nonnull
	@Override
	public List<HaxeVarDeclaration> getVarDeclarations()
	{
		final List<HaxeVarDeclaration> result = HaxeResolveUtil.getClassVarDeclarations(this);
		return result;
	}

	@Nullable
	@Override
	public HaxeNamedComponent findFieldByName(@Nonnull final String name)
	{
		return ContainerUtil.find(getFields(), new Condition<HaxeNamedComponent>()
		{
			@Override
			public boolean value(HaxeNamedComponent component)
			{
				return name.equals(component.getName());
			}
		});
	}

	@Override
	public HaxeNamedComponent findMethodByName(@Nonnull final String name)
	{
		return ContainerUtil.find(getMethods(), new consulo.util.lang.function.Condition<HaxeNamedComponent>()
		{
			@Override
			public boolean value(HaxeNamedComponent component)
			{
				return name.equals(component.getName());
			}
		});
	}

	@Override
	public boolean isGeneric()
	{
		return getGenericParam() != null;
	}
}
