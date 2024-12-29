package consulo.haxe;

import consulo.annotation.component.ExtensionImpl;
import consulo.colorScheme.AdditionalTextAttributesProvider;
import consulo.colorScheme.EditorColorsScheme;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 20-Aug-22
 */
@ExtensionImpl
public class HaxeDarkAdditionalTextAttributesProvider implements AdditionalTextAttributesProvider {
  @Nonnull
  @Override
  public String getColorSchemeName() {
    return EditorColorsScheme.DARCULA_SCHEME_NAME;
  }

  @Nonnull
  @Override
  public String getColorSchemeFile() {
    return "/colorSchemes/HaxeDefault.xml";
  }
}
