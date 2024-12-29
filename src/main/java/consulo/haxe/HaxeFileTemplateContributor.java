package consulo.haxe;

import consulo.annotation.component.ExtensionImpl;
import consulo.fileTemplate.FileTemplateContributor;
import consulo.fileTemplate.FileTemplateRegistrator;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 20-Aug-22
 */
@ExtensionImpl
public class HaxeFileTemplateContributor implements FileTemplateContributor {
  @Override
  public void register(@Nonnull FileTemplateRegistrator fileTemplateRegistrator) {
    fileTemplateRegistrator.registerInternalTemplate("Haxe Class");
    fileTemplateRegistrator.registerInternalTemplate("Haxe Interface");
    fileTemplateRegistrator.registerInternalTemplate("NMML Project File");
  }
}
