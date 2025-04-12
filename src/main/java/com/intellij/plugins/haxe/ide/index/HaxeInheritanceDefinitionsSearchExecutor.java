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
package com.intellij.plugins.haxe.ide.index;

import com.intellij.plugins.haxe.HaxeComponentType;
import com.intellij.plugins.haxe.lang.psi.HaxeClass;
import com.intellij.plugins.haxe.lang.psi.HaxeComponentName;
import com.intellij.plugins.haxe.lang.psi.HaxeNamedComponent;
import com.intellij.plugins.haxe.util.HaxeResolveUtil;
import consulo.annotation.component.ExtensionImpl;
import consulo.application.ApplicationManager;
import consulo.language.psi.PsiElement;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.language.psi.search.DefinitionsScopedSearch;
import consulo.language.psi.search.DefinitionsScopedSearchExecutor;
import consulo.language.psi.stub.FileBasedIndex;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.project.Project;
import jakarta.annotation.Nonnull;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author Fedor.Korotkov
 */
@ExtensionImpl
public class HaxeInheritanceDefinitionsSearchExecutor implements DefinitionsScopedSearchExecutor {
    public static List<HaxeClass> getItemsByQName(final HaxeClass haxeClass) {
        final List<HaxeClass> result = new ArrayList<>();
        DefinitionsScopedSearch.search(haxeClass).forEach(element -> {
            if (element instanceof HaxeClass) {
                result.add((HaxeClass) element);
            }
            return true;
        });
        return result;
    }

    @Override
    public boolean execute(@Nonnull final DefinitionsScopedSearch.SearchParameters parameters, @Nonnull final Predicate<? super PsiElement> consumer) {
        final PsiElement queryParameters = parameters.getElement();
        return ApplicationManager.getApplication().runReadAction((Supplier<Boolean>) () -> {
            final PsiElement queryParametersParent = queryParameters.getParent();
            HaxeNamedComponent haxeNamedComponent;
            if (queryParameters instanceof HaxeClass) {
                haxeNamedComponent = (HaxeClass) queryParameters;
            }
            else if (queryParametersParent instanceof HaxeNamedComponent && queryParameters instanceof HaxeComponentName) {
                haxeNamedComponent = (HaxeNamedComponent) queryParametersParent;
            }
            else {
                return true;
            }
            if (haxeNamedComponent instanceof HaxeClass) {
                processInheritors(((HaxeClass) haxeNamedComponent).getQualifiedName(), queryParameters, consumer);
            }
            else if (HaxeComponentType.typeOf(haxeNamedComponent) == HaxeComponentType.METHOD || HaxeComponentType.typeOf(haxeNamedComponent) == HaxeComponentType.FIELD) {
                final String nameToFind = haxeNamedComponent.getName();
                if (nameToFind == null) {
                    return true;
                }

                HaxeClass haxeClass = PsiTreeUtil.getParentOfType(haxeNamedComponent, HaxeClass.class);
                assert haxeClass != null;

                processInheritors(haxeClass.getQualifiedName(), queryParameters, element -> {
                    for (HaxeNamedComponent subHaxeNamedComponent : HaxeResolveUtil.getNamedSubComponents((HaxeClass) element)) {
                        if (nameToFind.equals(subHaxeNamedComponent.getName())) {
                            consumer.test(subHaxeNamedComponent);
                        }
                    }
                    return true;
                });
            }
            return true;
        });
    }

    private static boolean processInheritors(final String qName, final PsiElement context, final Predicate<? super PsiElement> consumer) {
        final Set<String> namesSet = new HashSet<>();
        final LinkedList<String> namesQueue = new LinkedList<>();
        namesQueue.add(qName);
        final Project project = context.getProject();
        final GlobalSearchScope scope = GlobalSearchScope.allScope(project);
        while (!namesQueue.isEmpty()) {
            final String name = namesQueue.pollFirst();
            if (!namesSet.add(name)) {
                continue;
            }
            List<List<HaxeClassInfo>> files = FileBasedIndex.getInstance().getValues(HaxeInheritanceIndex.HAXE_INHERITANCE_INDEX, name, scope);
            files.addAll(FileBasedIndex.getInstance().getValues(HaxeTypeDefInheritanceIndex.HAXE_TYPEDEF_INHERITANCE_INDEX, name, scope));
            for (List<HaxeClassInfo> subClassInfoList : files) {
                for (HaxeClassInfo subClassInfo : subClassInfoList) {
                    final HaxeClass subClass = HaxeResolveUtil.findClassByQName(subClassInfo.getValue(), context.getManager(), scope);
                    if (subClass != null) {
                        if (!consumer.test(subClass)) {
                            return true;
                        }
                        namesQueue.add(subClass.getQualifiedName());
                    }
                }
            }
        }
        return true;
    }
}
