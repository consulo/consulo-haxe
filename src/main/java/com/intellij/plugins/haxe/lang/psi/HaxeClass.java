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
package com.intellij.plugins.haxe.lang.psi;

import java.util.List;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.NonNls;

import javax.annotation.Nullable;

/**
 * @author: Fedor.Korotkov
 */
public interface HaxeClass extends HaxeComponent
{
	HaxeClass[] EMPTY_ARRAY = new HaxeClass[0];

	@Nonnull
	@NonNls
	String getQualifiedName();

	@Nonnull
	List<HaxeType> getExtendsList();

	@Nonnull
	List<HaxeType> getImplementsList();

	boolean isInterface();

	@Nonnull
	List<HaxeNamedComponent> getMethods();

	@Nonnull
	List<HaxeNamedComponent> getFields();

	@Nonnull
	List<HaxeVarDeclaration> getVarDeclarations();

	@Nullable
	HaxeNamedComponent findFieldByName(@Nonnull final String name);

	@Nullable
	HaxeNamedComponent findMethodByName(@Nonnull final String name);

	boolean isGeneric();

	@Nullable
	HaxeGenericParam getGenericParam();
}
