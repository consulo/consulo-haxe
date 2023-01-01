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

import com.intellij.plugins.haxe.HaxeComponentType;
import com.intellij.plugins.haxe.HaxeLanguage;
import com.intellij.plugins.haxe.lang.psi.HaxeClass;
import com.intellij.plugins.haxe.lang.psi.HaxeComponentName;
import com.intellij.plugins.haxe.lang.psi.HaxeNamedComponent;
import com.intellij.plugins.haxe.util.HaxePresentableUtil;
import com.intellij.plugins.haxe.util.HaxeResolveUtil;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.editor.documentation.LanguageDocumentationProvider;
import consulo.language.psi.PsiComment;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiManager;
import consulo.language.psi.util.PsiTreeUtil;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author: Fedor.Korotkov
 */
@ExtensionImpl
public class HaxeDocumentationProvider implements LanguageDocumentationProvider {
  @Override
  public String generateDoc(PsiElement element, PsiElement originalElement) {
    if (!(element instanceof HaxeComponentName) && !(element instanceof HaxeNamedComponent)) {
      return null;
    }
    HaxeNamedComponent namedComponent = (HaxeNamedComponent)(element instanceof HaxeNamedComponent ? element : element.getParent());
    final StringBuilder builder = new StringBuilder();
    final HaxeComponentType type = HaxeComponentType.typeOf(namedComponent);
    if (namedComponent instanceof HaxeClass) {
      builder.append(((HaxeClass)namedComponent).getQualifiedName());
    }
    else if (type == HaxeComponentType.FIELD || type == HaxeComponentType.METHOD) {
      final HaxeClass haxeClass = PsiTreeUtil.getParentOfType(namedComponent, HaxeClass.class);
      assert haxeClass != null;
      builder.append(haxeClass.getQualifiedName());
      builder.append(" ");
      builder.append(type.toString().toLowerCase());
      builder.append(" ");
      builder.append(namedComponent.getName());
    }
    final PsiComment comment = HaxeResolveUtil.findDocumentation(namedComponent);
    if (comment != null) {
      builder.append("<br/>");
      builder.append(HaxePresentableUtil.unwrapCommentDelimiters(comment.getText()));
    }
    return builder.toString();
  }

  @Override
  public PsiElement getDocumentationElementForLookupItem(PsiManager psiManager, Object object, PsiElement element) {
    return null;
  }

  @Override
  public List<String> getUrlFor(PsiElement element, PsiElement originalElement) {
    return null;
  }

  @Override
  public PsiElement getDocumentationElementForLink(PsiManager psiManager, String link, PsiElement context) {
    return null;
  }

  @Nonnull
  @Override
  public Language getLanguage() {
    return HaxeLanguage.INSTANCE;
  }
}
