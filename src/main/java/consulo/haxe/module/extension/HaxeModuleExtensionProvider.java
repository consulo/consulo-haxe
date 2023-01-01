package consulo.haxe.module.extension;

import consulo.annotation.component.ExtensionImpl;
import consulo.haxe.icon.HaxeIconGroup;
import consulo.localize.LocalizeValue;
import consulo.module.content.layer.ModuleExtensionProvider;
import consulo.module.content.layer.ModuleRootLayer;
import consulo.module.extension.ModuleExtension;
import consulo.module.extension.MutableModuleExtension;
import consulo.ui.image.Image;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 20-Aug-22
 */
@ExtensionImpl
public class HaxeModuleExtensionProvider implements ModuleExtensionProvider<HaxeModuleExtension> {
  @Nonnull
  @Override
  public String getId() {
    return "haxe";
  }

  @Nonnull
  @Override
  public LocalizeValue getName() {
    return LocalizeValue.localizeTODO("Haxe");
  }

  @Nonnull
  @Override
  public Image getIcon() {
    return HaxeIconGroup.haxe();
  }

  @Nonnull
  @Override
  public ModuleExtension<HaxeModuleExtension> createImmutableExtension(@Nonnull ModuleRootLayer moduleRootLayer) {
    return new HaxeModuleExtension(getId(), moduleRootLayer);
  }

  @Nonnull
  @Override
  public MutableModuleExtension<HaxeModuleExtension> createMutableExtension(@Nonnull ModuleRootLayer moduleRootLayer) {
    return new HaxeMutableModuleExtension(getId(), moduleRootLayer);
  }
}
