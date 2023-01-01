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
package com.intellij.plugins.haxe.ide;

import com.intellij.plugins.haxe.HaxeBundle;
import com.intellij.plugins.haxe.HaxeComponentType;
import com.intellij.plugins.haxe.HaxeLanguage;
import com.intellij.plugins.haxe.ide.index.HaxeInheritanceDefinitionsSearchExecutor;
import com.intellij.plugins.haxe.lang.lexer.HaxeTokenTypes;
import com.intellij.plugins.haxe.lang.psi.*;
import com.intellij.plugins.haxe.util.HaxeResolveUtil;
import consulo.annotation.component.ExtensionImpl;
import consulo.application.AllIcons;
import consulo.codeEditor.markup.GutterIconRenderer;
import consulo.language.Language;
import consulo.language.editor.DaemonBundle;
import consulo.language.editor.Pass;
import consulo.language.editor.gutter.GutterIconNavigationHandler;
import consulo.language.editor.gutter.LineMarkerInfo;
import consulo.language.editor.gutter.LineMarkerProvider;
import consulo.language.editor.ui.DefaultPsiElementCellRenderer;
import consulo.language.editor.ui.PsiElementListNavigator;
import consulo.language.psi.NavigatablePsiElement;
import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.ui.image.Image;
import consulo.util.collection.ContainerUtil;
import consulo.util.lang.function.Condition;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * @author: Fedor.Korotkov
 */
@ExtensionImpl
public class HaxeLineMarkerProvider implements LineMarkerProvider {

  @Override
  public LineMarkerInfo getLineMarkerInfo(@Nonnull PsiElement element) {
    return null;
  }

  @Override
  public void collectSlowLineMarkers(@Nonnull List<PsiElement> elements, @Nonnull Collection<LineMarkerInfo> result) {
    for (PsiElement element : elements) {
      if (element instanceof HaxeClass) {
        collectClassMarkers(result, (HaxeClass)element);
      }
    }
  }

  private static void collectClassMarkers(Collection<LineMarkerInfo> result, @Nonnull HaxeClass haxeClass) {
    final List<HaxeClass> supers = HaxeResolveUtil.tyrResolveClassesByQName(haxeClass.getExtendsList());
    supers.addAll(HaxeResolveUtil.tyrResolveClassesByQName(haxeClass.getImplementsList()));
    final List<HaxeNamedComponent> superItems = HaxeResolveUtil.findNamedSubComponents(supers.toArray(new HaxeClass[supers.size()]));

    final List<HaxeClass> subClasses = HaxeInheritanceDefinitionsSearchExecutor.getItemsByQName(haxeClass);
    final List<HaxeNamedComponent> subItems = new ArrayList<HaxeNamedComponent>();
    for (HaxeClass subClass : subClasses) {
      subItems.addAll(HaxeResolveUtil.getNamedSubComponents(subClass));
    }

    final boolean isInterface = HaxeComponentType.typeOf(haxeClass) == HaxeComponentType.INTERFACE;
    for (HaxeNamedComponent haxeNamedComponent : HaxeResolveUtil.getNamedSubComponents(haxeClass)) {
      final HaxeComponentType type = HaxeComponentType.typeOf(haxeNamedComponent);
      if (type == HaxeComponentType.METHOD || type == HaxeComponentType.FIELD) {
        LineMarkerInfo item = tryCreateOverrideMarker(haxeNamedComponent, superItems);
        if (item != null) {
          result.add(item);
        }
        item = tryCreateImplementationMarker(haxeNamedComponent, subItems, isInterface);
        if (item != null) {
          result.add(item);
        }
      }
    }

    if (!subClasses.isEmpty()) {
      final LineMarkerInfo marker = createImplementationMarker(haxeClass, subClasses);
      if (marker != null) {
        result.add(marker);
      }
    }
  }

  @Nullable
  private static LineMarkerInfo tryCreateOverrideMarker(final HaxeNamedComponent namedComponent,
                                                        List<HaxeNamedComponent> superItems) {
    final String methodName = namedComponent.getName();
    if (methodName == null) {
      return null;
    }
    final List<HaxeNamedComponent> filteredSuperItems = ContainerUtil.filter(superItems, new consulo.util.lang.function.Condition<HaxeNamedComponent>() {
      @Override
      public boolean value(HaxeNamedComponent component) {
        return methodName.equals(component.getName());
      }
    });
    if (filteredSuperItems.isEmpty()) {
      return null;
    }
    final PsiElement element = namedComponent.getComponentName();
    HaxeComponentWithDeclarationList componentWithDeclarationList = namedComponent instanceof HaxeComponentWithDeclarationList ?
                                                                    (HaxeComponentWithDeclarationList)namedComponent : null;
    final boolean overrides = componentWithDeclarationList != null &&
                              HaxeResolveUtil.getDeclarationTypes(componentWithDeclarationList.getDeclarationAttributeList()).
                                contains(HaxeTokenTypes.KOVERRIDE);
    final Image icon = overrides ? AllIcons.Gutter.OverridingMethod : AllIcons.Gutter.ImplementingMethod;
    assert element != null;
    return new LineMarkerInfo<PsiElement>(
      element,
      element.getTextRange(),
      icon,
      Pass.UPDATE_ALL,
      new Function<PsiElement, String>() {
        @Override
        public String apply(PsiElement element) {
          final HaxeClass superHaxeClass = PsiTreeUtil.getParentOfType(namedComponent, HaxeClass.class);
          if (superHaxeClass == null) return "null";
          if (overrides) {
            return HaxeBundle.message("overrides.method.in", namedComponent.getName(), superHaxeClass.getQualifiedName());
          }
          return HaxeBundle.message("implements.method.in", namedComponent.getName(), superHaxeClass.getQualifiedName());
        }
      },
      new GutterIconNavigationHandler<PsiElement>() {
        @Override
        public void navigate(MouseEvent e, PsiElement elt) {
          PsiElementListNavigator.openTargets(
            e,
            HaxeResolveUtil.getComponentNames(filteredSuperItems).toArray(new NavigatablePsiElement[filteredSuperItems.size()]),
            DaemonBundle.message("navigation.title.super.method", namedComponent.getName()),
            DaemonBundle.message("navigation.findUsages.title.super.method", namedComponent.getName()),
            new DefaultPsiElementCellRenderer());
        }
      },
      GutterIconRenderer.Alignment.LEFT
    );
  }

  @Nullable
  private static LineMarkerInfo tryCreateImplementationMarker(final HaxeNamedComponent namedComponent,
																							 List<HaxeNamedComponent> subItems,
																							 final boolean isInterface) {
    final PsiElement componentName = namedComponent.getComponentName();
    final String methodName = namedComponent.getName();
    if (methodName == null) {
      return null;
    }
    final List<HaxeNamedComponent> filteredSubItems = ContainerUtil.filter(subItems, new Condition<HaxeNamedComponent>() {
      @Override
      public boolean value(HaxeNamedComponent component) {
        return methodName.equals(component.getName());
      }
    });
    if (filteredSubItems.isEmpty() || componentName == null) {
      return null;
    }
    return new LineMarkerInfo<PsiElement>(
      componentName,
      componentName.getTextRange(),
      isInterface ? AllIcons.Gutter.ImplementedMethod : AllIcons.Gutter.OverridenMethod,
      Pass.UPDATE_ALL,
      new Function<PsiElement, String>() {
        @Override
        public String apply(PsiElement element) {
          return isInterface
                 ? DaemonBundle.message("method.is.implemented.too.many")
                 : DaemonBundle.message("method.is.overridden.too.many");
        }
      },
      new GutterIconNavigationHandler<PsiElement>() {
        @Override
        public void navigate(MouseEvent e, PsiElement elt) {
          PsiElementListNavigator.openTargets(
            e, HaxeResolveUtil.getComponentNames(filteredSubItems).toArray(new NavigatablePsiElement[filteredSubItems.size()]),
            isInterface ?
            DaemonBundle.message("navigation.title.implementation.method", namedComponent.getName(), filteredSubItems.size())
                        :
            DaemonBundle.message("navigation.title.overrider.method", namedComponent.getName(), filteredSubItems.size()),
            "Implementations of " + namedComponent.getName(),
            new DefaultPsiElementCellRenderer()
          );
        }
      },
      GutterIconRenderer.Alignment.RIGHT
    );
  }

  @Nullable
  private static LineMarkerInfo createImplementationMarker(final HaxeClass componentWithDeclarationList,
																						  final List<HaxeClass> items) {
    final HaxeComponentName componentName = componentWithDeclarationList.getComponentName();
    if (componentName == null) {
      return null;
    }
    return new LineMarkerInfo<PsiElement>(
      componentName,
      componentName.getTextRange(),
      componentWithDeclarationList instanceof HaxeInterfaceDeclaration
      ? AllIcons.Gutter.ImplementedMethod
      : AllIcons.Gutter.OverridenMethod,
      Pass.UPDATE_ALL,
      new Function<PsiElement, String>() {
        @Override
        public String apply(PsiElement element) {
          return DaemonBundle.message("method.is.implemented.too.many");
        }
      },
      new GutterIconNavigationHandler<PsiElement>() {
        @Override
        public void navigate(MouseEvent e, PsiElement elt) {
          PsiElementListNavigator.openTargets(
            e, HaxeResolveUtil.getComponentNames(items).toArray(new NavigatablePsiElement[items.size()]),
            DaemonBundle.message("navigation.title.subclass", componentWithDeclarationList.getName(), items.size()),
            "Subclasses of " + componentWithDeclarationList.getName(),
            new DefaultPsiElementCellRenderer()
          );
        }
      },
      GutterIconRenderer.Alignment.RIGHT
    );
  }

  @Nonnull
  @Override
  public Language getLanguage() {
    return HaxeLanguage.INSTANCE;
  }
}
