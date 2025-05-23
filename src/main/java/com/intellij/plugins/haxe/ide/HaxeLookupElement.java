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

import consulo.language.editor.completion.lookup.LookupElement;
import consulo.language.editor.completion.lookup.LookupElementPresentation;
import com.intellij.plugins.haxe.lang.psi.HaxeComponentName;
import consulo.navigation.ItemPresentation;
import consulo.util.lang.StringUtil;

import jakarta.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author: Fedor.Korotkov
 */
public class HaxeLookupElement extends LookupElement {
  private final HaxeComponentName myComponentName;

  public static Collection<HaxeLookupElement> convert(@Nonnull Collection<HaxeComponentName> componentNames) {
    final List<HaxeLookupElement> result = new ArrayList<HaxeLookupElement>(componentNames.size());
    for (HaxeComponentName componentName : componentNames) {
      result.add(new HaxeLookupElement(componentName));
    }
    return result;
  }

  public HaxeLookupElement(HaxeComponentName name) {
    myComponentName = name;
  }

  @Nonnull
  @Override
  public String getLookupString() {
    final String result = myComponentName.getName();
    if (result != null) {
      return result;
    }
    return myComponentName.getIdentifier().getText();
  }

  @Override
  public void renderElement(LookupElementPresentation presentation) {
    final ItemPresentation componentNamePresentation = myComponentName.getPresentation();
    if (componentNamePresentation == null) {
      presentation.setItemText(getLookupString());
      return;
    }
    presentation.setItemText(componentNamePresentation.getPresentableText());
    presentation.setIcon(componentNamePresentation.getIcon());
    final String pkg = componentNamePresentation.getLocationString();
    if (StringUtil.isNotEmpty(pkg)) {
      presentation.setTailText(" " + pkg, true);
    }
  }

  @Nonnull
  @Override
  public Object getObject() {
    return myComponentName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof HaxeLookupElement)) return false;

    return myComponentName.equals(((HaxeLookupElement)o).myComponentName);
  }

  @Override
  public int hashCode() {
    return myComponentName.hashCode();
  }
}
