package com.intellij.plugins.haxe.ide.annotator;

import com.intellij.plugins.haxe.HaxeLanguage;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.editor.annotation.Annotator;
import consulo.language.editor.annotation.AnnotatorFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author VISTALL
 * @since 20-Aug-22
 */
@ExtensionImpl
public class HaxeTypeAnnotatorFactory implements AnnotatorFactory {
  @Nullable
  @Override
  public Annotator createAnnotator() {
    return new HaxeTypeAnnotator();
  }

  @Nonnull
  @Override
  public Language getLanguage() {
    return HaxeLanguage.INSTANCE;
  }
}
