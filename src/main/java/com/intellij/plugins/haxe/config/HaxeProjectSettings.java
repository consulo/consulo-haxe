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
package com.intellij.plugins.haxe.config;

import consulo.annotation.component.ComponentScope;
import consulo.annotation.component.ServiceAPI;
import consulo.annotation.component.ServiceImpl;
import consulo.component.persist.PersistentStateComponent;
import consulo.component.persist.State;
import consulo.component.persist.Storage;
import consulo.ide.ServiceManager;
import consulo.project.Project;
import consulo.util.collection.ContainerUtil;
import consulo.util.lang.StringUtil;
import consulo.util.lang.function.Condition;
import jakarta.inject.Singleton;
import org.jdom.Element;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Fedor.Korotkov
 */
@Singleton
@State(name = "HaxeProjectSettings", storages = @Storage("haxe.xml"))
@ServiceAPI(ComponentScope.PROJECT)
@ServiceImpl
public class HaxeProjectSettings implements PersistentStateComponent<Element> {
  public static final String HAXE_SETTINGS = "HaxeProjectSettings";
  public static final String DEFINES = "defines";
  private String myCompilerDefinitions = "";

  public Set<String> getUserCompilerDefinitionsAsSet() {
    return new HashSet<String>(Arrays.asList(getCompilerDefinitions()));
  }

  public static HaxeProjectSettings getInstance(Project project) {
    return ServiceManager.getService(project, HaxeProjectSettings.class);
  }

  public String[] getCompilerDefinitions() {
    return myCompilerDefinitions.split(",");
  }

  public void setCompilerDefinitions(String[] compilerDefinitions) {
    this.myCompilerDefinitions = StringUtil.join(ContainerUtil.filter(compilerDefinitions, new Condition<String>() {
      @Override
      public boolean value(String s) {
        return s != null && !s.isEmpty();
      }
    }), ",");
  }

  @Override
  public void loadState(Element state) {
    myCompilerDefinitions = state.getAttributeValue(DEFINES, "");
  }

  @Override
  public Element getState() {
    if (myCompilerDefinitions.isEmpty()) {
      return null;
    }
    final Element element = new Element(HAXE_SETTINGS);
    element.setAttribute(DEFINES, myCompilerDefinitions);
    return element;
  }
}
