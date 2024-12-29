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
package com.intellij.plugins.haxe.lang.psi.impl;

import com.intellij.plugins.haxe.HaxeLanguage;
import com.intellij.plugins.haxe.lang.lexer.HaxeTokenTypes;
import com.intellij.plugins.haxe.lang.parser.HaxeParser;
import com.intellij.plugins.haxe.lang.psi.HaxeExpressionCodeFragment;
import com.intellij.plugins.haxe.lang.psi.HaxeFile;
import consulo.language.Language;
import consulo.language.ast.ASTNode;
import consulo.language.ast.IFileElementType;
import consulo.language.file.FileTypeManager;
import consulo.language.file.FileViewProvider;
import consulo.language.file.light.LightVirtualFile;
import consulo.language.impl.ast.FileElement;
import consulo.language.impl.file.SingleRootFileViewProvider;
import consulo.language.parser.PsiBuilder;
import consulo.language.parser.PsiBuilderFactory;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiManager;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.language.version.LanguageVersionUtil;
import consulo.project.Project;
import org.jetbrains.annotations.NonNls;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import static consulo.language.impl.parser.GeneratedParserUtilBase.*;

/**
 * @author: Fedor.Korotkov
 */
public class HaxeExpressionCodeFragmentImpl extends HaxeFile implements HaxeExpressionCodeFragment {
  private PsiElement myContext;
  private boolean myPhysical;
  private FileViewProvider myViewProvider;
  private GlobalSearchScope myScope = null;

  public HaxeExpressionCodeFragmentImpl(Project project, @NonNls String name, CharSequence text, boolean isPhysical) {
    super(new SingleRootFileViewProvider(PsiManager.getInstance(project), new LightVirtualFile(name,
        FileTypeManager.getInstance().getFileTypeByFileName(name), text), isPhysical) {
      @Override
      public boolean supportsIncrementalReparse(@Nonnull Language rootLanguage) {
        return false;
      }
    });

    myPhysical = isPhysical;
    ((SingleRootFileViewProvider) getViewProvider()).forceCachedPsi(this);
    final MyHaxeFileElementType type = new MyHaxeFileElementType();
    init(type, type);
  }


  @Override
  public PsiElement getContext() {
    return myContext;
  }

  @Override
  @Nonnull
  public FileViewProvider getViewProvider() {
    if (myViewProvider != null) {
      return myViewProvider;
    }
    return super.getViewProvider();
  }

  @Override
  public boolean isValid() {
    if (!super.isValid()) {
      return false;
    }
    if (myContext != null && !myContext.isValid()) {
      return false;
    }
    return true;
  }

  @Override
  protected HaxeExpressionCodeFragmentImpl clone() {
    final HaxeExpressionCodeFragmentImpl clone = (HaxeExpressionCodeFragmentImpl) cloneImpl((FileElement) calcTreeElement().clone());
    clone.myPhysical = myPhysical;

    clone.myOriginalFile = this;
    SingleRootFileViewProvider cloneViewProvider = new SingleRootFileViewProvider(getManager(), new LightVirtualFile(getName(), getLanguage(), getText()), myPhysical);
    clone.myViewProvider = cloneViewProvider;
    cloneViewProvider.forceCachedPsi(clone);
    clone.init(getContentElementType(), getContentElementType());
    return clone;
  }

  @Override
  public boolean isPhysical() {
    return myPhysical;
  }

  public void setContext(PsiElement context) {
    myContext = context;
  }

  @Override
  public void forceResolveScope(GlobalSearchScope scope) {
    myScope = scope;
  }

  @Override
  public GlobalSearchScope getForcedResolveScope() {
    return myScope;
  }

  private class MyHaxeFileElementType extends IFileElementType {
    public MyHaxeFileElementType() {
      super(HaxeLanguage.INSTANCE);
    }

    @Nullable
    @Override
    public ASTNode parseContents(final ASTNode chameleon) {
      final PsiElement psi = new HaxePsiCompositeElementImpl(chameleon);
      return doParseContents(chameleon, psi);
    }

    @Override
    protected ASTNode doParseContents(@Nonnull ASTNode chameleon, @Nonnull PsiElement psi) {
      final PsiBuilderFactory factory = PsiBuilderFactory.getInstance();
      final PsiBuilder psiBuilder = factory.createBuilder(getProject(), chameleon, LanguageVersionUtil.findDefaultVersion(getLanguage()));
      final PsiBuilder builder = adapt_builder_(HaxeTokenTypes.EXPRESSION, psiBuilder, new HaxeParser());

      final PsiBuilder.Marker marker = builder.mark();
      enter_section_(builder, 0, _NONE_, "<code fragment>");
      HaxeParser.expression(builder, 1);
      while (builder.getTokenType() != null) {
        builder.advanceLexer();
      }
      marker.done(HaxeTokenTypes.EXPRESSION);
      return builder.getTreeBuilt();
    }
  }
}
