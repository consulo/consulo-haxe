package consulo.haxe;

import consulo.annotation.component.ExtensionImpl;
import consulo.colorScheme.AdditionalTextAttributesProvider;
import consulo.colorScheme.EditorColorsScheme;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 20-Aug-22
 */
@ExtensionImpl
public class HaxeDefaultAdditionalTextAttributesProvider implements AdditionalTextAttributesProvider {
  @Nonnull
  @Override
  public String getColorSchemeName() {
    return EditorColorsScheme.DEFAULT_SCHEME_NAME;
  }

  @Nonnull
  @Override
  public String getColorSchemeFile() {
    return "/colorSchemes/HaxeDefault.xml";
  }
}
