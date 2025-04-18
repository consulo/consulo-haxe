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
package com.intellij.plugins.haxe.compilation;

import com.intellij.plugins.haxe.HaxeBundle;
import com.intellij.plugins.haxe.config.sdk.HaxeSdkAdditionalDataBase;
import com.intellij.plugins.haxe.ide.module.HaxeModuleSettings;
import com.intellij.plugins.haxe.module.HaxeModuleSettingsBase;
import com.intellij.plugins.haxe.runner.HaxeApplicationConfiguration;
import com.intellij.plugins.haxe.runner.debugger.HaxeDebugRunner;
import com.intellij.plugins.haxe.util.HaxeCommonCompilerUtil;
import consulo.annotation.component.ExtensionImpl;
import consulo.compiler.*;
import consulo.compiler.scope.CompileScope;
import consulo.content.bundle.Sdk;
import consulo.content.bundle.SdkAdditionalData;
import consulo.execution.configuration.RunConfigurationModule;
import consulo.execution.debug.DefaultDebugExecutor;
import consulo.execution.executor.ExecutorRegistry;
import consulo.haxe.module.extension.HaxeModuleExtension;
import consulo.language.content.ProductionContentFolderTypeProvider;
import consulo.language.util.ModuleUtilCore;
import consulo.logging.Logger;
import consulo.module.Module;
import consulo.module.content.layer.OrderEnumerator;
import consulo.virtualFileSystem.VirtualFile;

import jakarta.annotation.Nonnull;
import java.io.DataInput;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ExtensionImpl
public class HaxeCompiler implements SourceProcessingCompiler {
  private static final Logger LOG = Logger.getInstance(HaxeCompiler.class);

  @Nonnull
  public String getDescription() {
    return HaxeBundle.message("haxe.compiler.description");
  }

  @Override
  public boolean validateConfiguration(CompileScope scope) {
    return true;
  }

  @Nonnull
  @Override
  public ProcessingItem[] getProcessingItems(consulo.compiler.CompileContext context) {
    final List<ProcessingItem> itemList = new ArrayList<ProcessingItem>();
    for (final Module module : getModulesToCompile(context.getCompileScope())) {
      itemList.add(new MyProcessingItem(module));
    }
    return itemList.toArray(new ProcessingItem[itemList.size()]);
  }

  private static List<Module> getModulesToCompile(CompileScope scope) {
    final List<Module> result = new ArrayList<Module>();
    for (final Module module : scope.getAffectedModules()) {
      if (ModuleUtilCore.getExtension(module, HaxeModuleExtension.class) == null) {
        continue;
      }
      result.add(module);
    }
    return result;
  }

  @Override
  public ProcessingItem[] process(CompileContext context, ProcessingItem[] items) {
    return make(context, items);
  }

  private static ProcessingItem[] run(CompileContext context, ProcessingItem[] items, HaxeApplicationConfiguration haxeApplicationConfiguration) {
    final Module module = haxeApplicationConfiguration.getConfigurationModule().getModule();
    if (module == null) {
      context.addMessage(consulo.compiler.CompilerMessageCategory.ERROR, HaxeBundle.message("no.module.for.run.configuration", haxeApplicationConfiguration.getName()), null, -1, -1);
      return ProcessingItem.EMPTY_ARRAY;
    }
    if (compileModule(context, module)) {
      final int index = findProcessingItemIndexByModule(items, haxeApplicationConfiguration.getConfigurationModule());
      if (index != -1) {
        return new ProcessingItem[]{items[index]};
      }
    }
    return ProcessingItem.EMPTY_ARRAY;
  }

  private static ProcessingItem[] make(CompileContext context, ProcessingItem[] items) {
    final List<ProcessingItem> result = new ArrayList<ProcessingItem>();
    for (ProcessingItem processingItem : items) {
      if (!(processingItem instanceof MyProcessingItem)) {
        continue;
      }
      final MyProcessingItem myProcessingItem = (MyProcessingItem) processingItem;

      if (compileModule(context, myProcessingItem.myModule)) {
        result.add(processingItem);
      }
    }
    return result.toArray(new ProcessingItem[result.size()]);
  }

  private static boolean compileModule(final CompileContext context, @Nonnull final Module module) {
    final HaxeModuleSettings settings = HaxeModuleSettings.getInstance(module);
    final boolean isDebug = ExecutorRegistry.getInstance().isStarting(context.getProject(), DefaultDebugExecutor.EXECUTOR_ID, HaxeDebugRunner.HAXE_DEBUG_RUNNER_ID);
    final Sdk sdk = ModuleUtilCore.getSdk(module, HaxeModuleExtension.class);
    if (sdk == null) {
      context.addMessage(CompilerMessageCategory.ERROR, HaxeBundle.message("no.sdk.for.module", module.getName()), null, -1, -1);
      return false;
    }
    boolean compiled = HaxeCommonCompilerUtil.compile(new HaxeCommonCompilerUtil.CompilationContext() {
      @Nonnull
      @Override
      public HaxeModuleSettingsBase getModuleSettings() {
        return settings;
      }

      @Override
      public String getModuleName() {
        return module.getName();
      }

      @Override
      public void errorHandler(String message) {
        context.addMessage(CompilerMessageCategory.ERROR, message, null, -1, -1);
      }

      @Override
      public void log(String message) {
        LOG.debug(message);
      }

      @Override
      public String getSdkName() {
        return sdk.getName();
      }

      @Override
      public String getSdkHomePath() {
        return sdk.getHomePath();
      }

      @Override
      public String getHaxelibPath() {
        SdkAdditionalData data = sdk.getSdkAdditionalData();
        return data instanceof HaxeSdkAdditionalDataBase ? ((HaxeSdkAdditionalDataBase) data).getHaxelibPath() : null;
      }

      @Override
      public boolean isDebug() {
        return isDebug;
      }

      @Override
      public List<String> getSourceRoots() {
        final List<String> result = new ArrayList<String>();
        for (VirtualFile sourceRoot : OrderEnumerator.orderEntries(module).recursively().withoutSdk().exportedOnly().sources().getRoots()) {
          result.add(sourceRoot.getPath());
        }
        for (VirtualFile sourceRoot : OrderEnumerator.orderEntries(module).librariesOnly().getSourceRoots()) {
          result.add(sourceRoot.getPath());
        }
        return result;
      }

      @Override
      public String getCompileOutputPath() {
        return ModuleCompilerPathsManager.getInstance(module).getCompilerOutputUrl(ProductionContentFolderTypeProvider.getInstance());
      }

      @Override
      public void handleOutput(String[] lines) {
        HaxeCompilerUtil.fillContext(module, context, lines);
      }
    });

    if (!compiled) {
      context.addMessage(consulo.compiler.CompilerMessageCategory.ERROR, "compilation failed", null, 0, 0);
    }

    return compiled;
  }

  private static int findProcessingItemIndexByModule(ProcessingItem[] items, RunConfigurationModule moduleConfiguration) {
    final Module module = moduleConfiguration.getModule();
    if (module == null || module.getModuleDir() == null) {
      return -1;
    }
    for (int i = 0; i < items.length; ++i) {
      if (module.getModuleDir().equals(items[i].getFile())) {
        return i;
      }
    }
    return -1;
  }

  @Override
  public ValidityState createValidityState(DataInput in) throws IOException {
    return new EmptyValidityState();
  }

  private static class MyProcessingItem implements ProcessingItem {
    private final Module myModule;

    private MyProcessingItem(Module module) {
      myModule = module;
    }

    @Nonnull
    public File getFile() {
      return new File(myModule.getModuleDirPath());
    }

    public consulo.compiler.ValidityState getValidityState() {
      return new consulo.compiler.EmptyValidityState();
    }
  }
}
