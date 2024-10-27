package consulo.haxe.template;

import com.intellij.plugins.haxe.ide.template.HaxeTemplateContextType;
import consulo.annotation.component.ExtensionImpl;
import consulo.haxe.localize.HaxeLocalize;
import consulo.language.editor.template.LiveTemplateContributor;
import consulo.localize.LocalizeValue;
import jakarta.annotation.Nonnull;

@ExtensionImpl
public class HaxeLiveTemplateContributor implements LiveTemplateContributor {
  @Override
  @Nonnull
  public String groupId() {
    return "haxe";
  }

  @Override
  @Nonnull
  public LocalizeValue groupName() {
    return LocalizeValue.localizeTODO("Haxe");
  }

  @Override
  public void contribute(@Nonnull LiveTemplateContributor.Factory factory) {
    try(Builder builder = factory.newBuilder("haxeIter", "iter", "for ($VAR$ in $ARRAY$) {\n"
        + "  $END$\n"
        + "}", HaxeLocalize.livetemplateDescriptionIter())) {
      builder.withReformat();

      builder.withVariable("ARRAY", "haxeArrayVariable()", "\"array \"", true);
      builder.withVariable("VAR", "haxeSuggestVariableName()", "\"o\"", true);

      builder.withContext(HaxeTemplateContextType.class, true);
    }

    try(Builder builder = factory.newBuilder("haxeItar", "itar", "for ($INDEX$ in 0...$ARRAY$.length) {\n"
        + "  var $VAR$ = $ARRAY$[$INDEX$];\n"
        + "  $END$\n"
        + "}", HaxeLocalize.livetemplateDescriptionItar())) {
      builder.withReformat();

      builder.withVariable("INDEX", "haxeSuggestIndexName()", "\"i\"", true);
      builder.withVariable("ARRAY", "haxeArrayVariable()", "\"array\"", true);
      builder.withVariable("VAR", "haxeSuggestVariableName()", "\"o\"", true);

      builder.withContext(HaxeTemplateContextType.class, true);
    }

    try(Builder builder = factory.newBuilder("haxePv", "pv", "public var $END$", LocalizeValue.localizeTODO("public var"))) {
      builder.withContext(HaxeTemplateContextType.class, true);
    }

    try(Builder builder = factory.newBuilder("haxePrv", "prv", "private var $END$", LocalizeValue.localizeTODO("private var"))) {
      builder.withContext(HaxeTemplateContextType.class, true);
    }

    try(Builder builder = factory.newBuilder("haxePsv", "psv", "public static var $END$", LocalizeValue.localizeTODO("public static var"))) {
      builder.withContext(HaxeTemplateContextType.class, true);
    }

    try(Builder builder = factory.newBuilder("haxePrsv", "prsv", "private static var $END$", LocalizeValue.localizeTODO("private static var"))) {
      builder.withContext(HaxeTemplateContextType.class, true);
    }

    try(Builder builder = factory.newBuilder("haxePf", "pf", "public function $NAME$($ARGS$):$RETURN_TYPE$ {\n"
        + " $END$ \n"
        + "}", LocalizeValue.localizeTODO("public function"))) {
      builder.withReformat();

      builder.withVariable("NAME", "", "\"name\"", true);
      builder.withVariable("ARGS", "", "", true);
      builder.withVariable("RETURN_TYPE", "\"Void\"", "\"Void\"", true);

      builder.withContext(HaxeTemplateContextType.class, true);
    }

    try(Builder builder = factory.newBuilder("haxePrf", "prf", "private function $NAME$($ARGS$):$RETURN_TYPE$ {\n"
        + " $END$ \n"
        + "}", LocalizeValue.localizeTODO("private function"))) {
      builder.withReformat();

      builder.withVariable("NAME", "", "\"name\"", true);
      builder.withVariable("ARGS", "", "", true);
      builder.withVariable("RETURN_TYPE", "\"Void\"", "\"Void\"", true);

      builder.withContext(HaxeTemplateContextType.class, true);
    }

    try(Builder builder = factory.newBuilder("haxePsf", "psf", "public static function $NAME$($ARGS$):$RETURN_TYPE$ {\n"
        + " $END$ \n"
        + "}", LocalizeValue.localizeTODO("public static function"))) {
      builder.withReformat();

      builder.withVariable("NAME", "", "\"name\"", true);
      builder.withVariable("ARGS", "", "", true);
      builder.withVariable("RETURN_TYPE", "\"Void\"", "\"Void\"", true);

      builder.withContext(HaxeTemplateContextType.class, true);
    }

    try(Builder builder = factory.newBuilder("haxePrsf", "prsf", "private static function $NAME$($ARGS$):$RETURN_TYPE$ {\n"
        + " $END$ \n"
        + "}", LocalizeValue.localizeTODO("private static function"))) {
      builder.withReformat();

      builder.withVariable("NAME", "", "\"name\"", true);
      builder.withVariable("ARGS", "", "", true);
      builder.withVariable("RETURN_TYPE", "\"Void\"", "\"Void\"", true);

      builder.withContext(HaxeTemplateContextType.class, true);
    }

  }
}
