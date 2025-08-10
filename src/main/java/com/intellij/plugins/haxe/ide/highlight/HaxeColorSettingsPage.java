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
package com.intellij.plugins.haxe.ide.highlight;

import consulo.annotation.component.ExtensionImpl;
import consulo.colorScheme.TextAttributesKey;
import consulo.colorScheme.setting.AttributesDescriptor;
import consulo.colorScheme.setting.ColorDescriptor;
import consulo.haxe.localize.HaxeLocalize;
import consulo.language.editor.colorScheme.setting.ColorSettingsPage;
import consulo.language.editor.highlight.SyntaxHighlighter;
import consulo.localize.LocalizeValue;
import jakarta.annotation.Nonnull;

import java.util.HashMap;
import java.util.Map;

import static com.intellij.plugins.haxe.ide.highlight.HaxeSyntaxHighlighterColors.*;

/**
 * @author fedor.korotkov
 */
@ExtensionImpl
public class HaxeColorSettingsPage implements ColorSettingsPage
{
  private static final AttributesDescriptor[] ATTRS = new AttributesDescriptor[]{
    new AttributesDescriptor(HaxeLocalize.haxeColorSettingsDescriptionLineComment(), LINE_COMMENT),
    new AttributesDescriptor(HaxeLocalize.haxeColorSettingsDescriptionBlockComment(), BLOCK_COMMENT),
    new AttributesDescriptor(HaxeLocalize.haxeColorSettingsDescriptionDocComment(), DOC_COMMENT),
    new AttributesDescriptor(HaxeLocalize.haxeColorSettingsDescriptionConditionalCompilation(), CONDITIONALLY_NOT_COMPILED),
    new AttributesDescriptor(HaxeLocalize.haxeColorSettingsDescriptionConditionalCompilationDefinedFlag(), DEFINED_VAR),
    new AttributesDescriptor(HaxeLocalize.haxeColorSettingsDescriptionConditionalCompilationUndefinedFlag(), UNDEFINED_VAR),
    new AttributesDescriptor(HaxeLocalize.haxeColorSettingsDescriptionKeyword(), KEYWORD),
    new AttributesDescriptor(HaxeLocalize.haxeColorSettingsDescriptionNumber(), NUMBER),
    new AttributesDescriptor(HaxeLocalize.haxeColorSettingsDescriptionString(), STRING),
    new AttributesDescriptor(HaxeLocalize.haxeColorSettingsDescriptionOperator(), OPERATION_SIGN),
    new AttributesDescriptor(HaxeLocalize.haxeColorSettingsDescriptionParenths(), PARENTHS),
    new AttributesDescriptor(HaxeLocalize.haxeColorSettingsDescriptionBrackets(), BRACKETS),
    new AttributesDescriptor(HaxeLocalize.haxeColorSettingsDescriptionBraces(), BRACES),
    new AttributesDescriptor(HaxeLocalize.haxeColorSettingsDescriptionComma(), COMMA),
    new AttributesDescriptor(HaxeLocalize.haxeColorSettingsDescriptionDot(), DOT),
    new AttributesDescriptor(HaxeLocalize.haxeColorSettingsDescriptionSemicolon(), SEMICOLON),
    new AttributesDescriptor(HaxeLocalize.haxeColorSettingsDescriptionBadCharacter(), BAD_CHARACTER),
    new AttributesDescriptor(HaxeLocalize.haxeColorSettingsDescriptionParameter(), PARAMETER),
    new AttributesDescriptor(HaxeLocalize.haxeColorSettingsDescriptionLocalVariable(), LOCAL_VARIABLE),
    new AttributesDescriptor(HaxeLocalize.haxeColorSettingsDescriptionClass(), CLASS),
    new AttributesDescriptor(HaxeLocalize.haxeColorSettingsDescriptionInterface(), INTERFACE),
    new AttributesDescriptor(HaxeLocalize.haxeColorSettingsDescriptionInstanceMemberFunction(), INSTANCE_MEMBER_FUNCTION),
    new AttributesDescriptor(HaxeLocalize.haxeColorSettingsDescriptionStaticMemberFunction(), STATIC_MEMBER_FUNCTION),
    new AttributesDescriptor(HaxeLocalize.haxeColorSettingsDescriptionInstanceMemberVariable(), INSTANCE_MEMBER_VARIABLE),
    new AttributesDescriptor(HaxeLocalize.haxeColorSettingsDescriptionStaticMemberVariable(), STATIC_MEMBER_VARIABLE)
  };

  private static final Map<String, TextAttributesKey> ourTags = new HashMap<>();

  static {
    ourTags.put("parameter", PARAMETER);
    ourTags.put("local.variable", LOCAL_VARIABLE);
    ourTags.put("class", CLASS);
    ourTags.put("compilation", CONDITIONALLY_NOT_COMPILED);
    ourTags.put("defined.flag", DEFINED_VAR);
    ourTags.put("undefined.flag", UNDEFINED_VAR);
    ourTags.put("interface", INTERFACE);
    ourTags.put("instance.member.function", INSTANCE_MEMBER_FUNCTION);
    ourTags.put("static.member.function", STATIC_MEMBER_FUNCTION);
    ourTags.put("instance.member.variable", INSTANCE_MEMBER_VARIABLE);
    ourTags.put("static.member.variable", STATIC_MEMBER_VARIABLE);
  }

  @Nonnull
  @Override
  public LocalizeValue getDisplayName() {
    return HaxeLocalize.haxeTitle();
  }

  @Nonnull
  @Override
  public ColorDescriptor[] getColorDescriptors() {
    return ColorDescriptor.EMPTY_ARRAY;
  }

  @Nonnull
  @Override
  public SyntaxHighlighter getHighlighter() {
    return new HaxeSyntaxHighlighter();
  }

  @Override
  public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
    return ourTags;
  }

  @Nonnull
  @Override
  public AttributesDescriptor[] getAttributeDescriptors() {
    return ATTRS;
  }

  @Nonnull
  @Override
  public String getDemoText() {
    return "<compilation>#if <defined.flag>definedFlag</defined.flag> && <undefined.flag>undefinedFlag</undefined.flag>\n" +
           "#error \"Error!!\"\n" +
           "#else</compilation>\n" +
           "import <class>util.Date</class>;\n" +
           "<compilation>#end</compilation>\n" +
           "\n" +
           "/* Block comment */\n" +
           "/**\n" +
           " Document comment\n" +
           "**/\n" +
           "class <class>SomeClass</class> implements <interface>IOther</interface> { // some comment\n" +
           "  private var <instance.member.variable>field</instance.member.variable> = null;\n" +
           "  private var <instance.member.variable>unusedField</instance.member.variable>:<class>Number</class> = 12345.67890;\n" +
           "  private var <instance.member.variable>anotherString</instance.member.variable>:<class>String</class> = \"Another\\nStrin\\g\";\n" +
           "  public static var <static.member.variable>staticField</static.member.variable>:<class>Int</class> = 0;\n" +
           "\n" +
           "  public static function <static.member.function>inc</static.member.function>() {\n" +
           "    <static.member.variable>staticField</static.member.variable>++;\n" +
           "  }\n" +
           "  public function <instance.member.function>foo</instance.member.function>(<parameter>param</parameter>:<interface>AnInterface</interface>) {\n" +
           "    trace(<instance.member.variable>anotherString</instance.member.variable> + <parameter>param</parameter>);\n" +
           "    var <local.variable>reassignedValue</local.variable>:<class>Int</class> = <class>SomeClass</class>.<static.member.variable>staticField</static.member.variable>; \n" +
           "    <local.variable>reassignedValue</local.variable> ++; \n" +
           "    function localFunction() {\n" +
           "      var <local.variable>a</local.variable>:<class>Int</class> = $$$;// bad character\n" +
           "    };\n" +
           "  }\n" +
           "}";
  }
}
