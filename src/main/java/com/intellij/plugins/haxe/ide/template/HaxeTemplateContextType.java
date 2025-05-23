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
package com.intellij.plugins.haxe.ide.template;

import com.intellij.plugins.haxe.HaxeLanguage;
import consulo.annotation.component.ExtensionImpl;
import consulo.haxe.localize.HaxeLocalize;
import consulo.language.editor.template.context.BaseTemplateContextType;
import consulo.language.editor.template.context.TemplateActionContext;
import consulo.language.psi.PsiFile;

import jakarta.annotation.Nonnull;

/**
 * @author Fedor.Korotkov
 */
@ExtensionImpl
public class HaxeTemplateContextType extends BaseTemplateContextType {
    public HaxeTemplateContextType() {
        super("HAXE", HaxeLocalize.haxeLanguageId());
    }

    @Override
    public boolean isInContext(@Nonnull TemplateActionContext context) {
        PsiFile file = context.getFile();
        return file.getLanguage() instanceof HaxeLanguage;
    }
}
