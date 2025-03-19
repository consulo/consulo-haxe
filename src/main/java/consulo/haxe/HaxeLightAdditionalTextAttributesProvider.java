package consulo.haxe;

import com.intellij.plugins.haxe.ide.highlight.HaxeSyntaxHighlighterColors;
import consulo.annotation.component.ExtensionImpl;
import consulo.colorScheme.AttributesFlyweightBuilder;
import consulo.colorScheme.EditorColorSchemeExtender;
import consulo.colorScheme.EditorColorsScheme;
import consulo.ui.color.RGBColor;
import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 20-Aug-22
 */
@ExtensionImpl
public class HaxeLightAdditionalTextAttributesProvider implements EditorColorSchemeExtender {
    @Override
    public void extend(Builder builder) {
        builder.add(HaxeSyntaxHighlighterColors.CONDITIONALLY_NOT_COMPILED, AttributesFlyweightBuilder.create()
            .withForeground(new RGBColor(0x38, 0x49, 0x2E))
            .build());

        builder.add(HaxeSyntaxHighlighterColors.DEFINED_VAR, AttributesFlyweightBuilder.create()
            .withForeground(new RGBColor(0x38, 0x49, 0x2E))
            .withBoldFont()
            .build());

        builder.add(HaxeSyntaxHighlighterColors.UNDEFINED_VAR, AttributesFlyweightBuilder.create()
            .withForeground(new RGBColor(0x38, 0x49, 0x2E))
            .build());
    }

    @Nonnull
    @Override
    public String getColorSchemeId() {
        return EditorColorsScheme.DEFAULT_SCHEME_NAME;
    }
}
