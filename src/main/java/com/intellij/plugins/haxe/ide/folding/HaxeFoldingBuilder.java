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
package com.intellij.plugins.haxe.ide.folding;

import com.intellij.plugins.haxe.HaxeLanguage;
import com.intellij.plugins.haxe.lang.lexer.HaxeTokenTypes;
import consulo.annotation.component.ExtensionImpl;
import consulo.document.Document;
import consulo.document.util.TextRange;
import consulo.language.Language;
import consulo.language.ast.ASTNode;
import consulo.language.editor.folding.FoldingBuilder;
import consulo.language.editor.folding.FoldingDescriptor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@ExtensionImpl
public class HaxeFoldingBuilder implements FoldingBuilder {
  @Nonnull
  @Override
  public FoldingDescriptor[] buildFoldRegions(@Nonnull ASTNode node, @Nonnull Document document) {
    List<FoldingDescriptor> list = new ArrayList<FoldingDescriptor>();
    buildFolding(node, list);
    FoldingDescriptor[] descriptors = new FoldingDescriptor[list.size()];
    return list.toArray(descriptors);
  }

  private static void buildFolding(ASTNode node, List<FoldingDescriptor> list) {
    if (node.getElementType() == HaxeTokenTypes.BLOCK_STATEMENT) {
      final TextRange range = node.getTextRange();
      list.add(new FoldingDescriptor(node, range));
    }
    for (ASTNode child : node.getChildren(null)) {
      buildFolding(child, list);
    }
  }

  @Nullable
  @Override
  public String getPlaceholderText(@Nonnull ASTNode node) {
    return "{...}";
  }

  @Override
  public boolean isCollapsedByDefault(@Nonnull ASTNode node) {
    return false;
  }

  @Nonnull
  @Override
  public Language getLanguage() {
    return HaxeLanguage.INSTANCE;
  }
}
