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

import java.util.List;

import javax.annotation.Nonnull;

import com.intellij.icons.AllIcons;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.openapi.util.Condition;
import com.intellij.plugins.haxe.HaxeFileType;
import com.intellij.plugins.haxe.nmml.NMMLFileType;
import com.intellij.util.SmartList;
import com.intellij.util.containers.ContainerUtil;
import consulo.ui.image.Image;
import consulo.ui.image.ImageEffects;
import icons.HaxeIcons;

/**
 * @author: Fedor.Korotkov
 */
public class HaxeFileTemplateUtil {
  private final static String HAXE_TEMPLATE_PREFIX = "Haxe ";

  public static List<FileTemplate> getApplicableTemplates() {
    return getApplicableTemplates(new Condition<FileTemplate>() {
      @Override
      public boolean value(FileTemplate fileTemplate) {
        return HaxeFileType.DEFAULT_EXTENSION.equals(fileTemplate.getExtension());
      }
    });
  }

  public static List<FileTemplate> getNMMLTemplates() {
    return getApplicableTemplates(new Condition<FileTemplate>() {
      @Override
      public boolean value(FileTemplate fileTemplate) {
        return NMMLFileType.DEFAULT_EXTENSION.equals(fileTemplate.getExtension());
      }
    });
  }

  public static List<FileTemplate> getApplicableTemplates(Condition<FileTemplate> filter) {
    List<FileTemplate> applicableTemplates = new SmartList<FileTemplate>();
    applicableTemplates.addAll(ContainerUtil.findAll(FileTemplateManager.getInstance().getInternalTemplates(), filter));
    applicableTemplates.addAll(ContainerUtil.findAll(FileTemplateManager.getInstance().getAllTemplates(), filter));
    return applicableTemplates;
  }

  public static String getTemplateShortName(String templateName) {
    if (templateName.startsWith(HAXE_TEMPLATE_PREFIX)) {
      return templateName.substring(HAXE_TEMPLATE_PREFIX.length());
    }
    return templateName;
  }

  @Nonnull
  public static Image getTemplateIcon(String name) {
    name = getTemplateShortName(name);
    if ("Class".equals(name)) {
      return ImageEffects.folded(AllIcons.Nodes.Class, HaxeIcons.HaxeLang);
    }
    else if ("Interface".equals(name)) {
      return ImageEffects.folded(AllIcons.Nodes.Interface, HaxeIcons.HaxeLang);
    }
    else if ("Enum".equals(name)) {
      return ImageEffects.folded(AllIcons.Nodes.Enum, HaxeIcons.HaxeLang);
    }
    return icons.HaxeIcons.Haxe;
  }
}
