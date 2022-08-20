/**
 * @author VISTALL
 * @since 20-Aug-22
 */
open module com.intellij.plugins.haxe {
  // TODO remove this dependency in future
  requires java.desktop;
  requires forms.rt;

  requires consulo.ide.api;

  requires com.intellij.regexp;
  requires com.intellij.xml;

  exports com.intellij.plugins.haxe;
  exports com.intellij.plugins.haxe.codeInspection;
  exports com.intellij.plugins.haxe.compilation;
  exports com.intellij.plugins.haxe.config;
  exports com.intellij.plugins.haxe.config.sdk;
  exports com.intellij.plugins.haxe.config.sdk.impl;
  exports com.intellij.plugins.haxe.config.sdk.ui;
  exports com.intellij.plugins.haxe.config.ui;
  exports com.intellij.plugins.haxe.ide;
  exports com.intellij.plugins.haxe.ide.actions;
  exports com.intellij.plugins.haxe.ide.annotator;
  exports com.intellij.plugins.haxe.ide.editor;
  exports com.intellij.plugins.haxe.ide.folding;
  exports com.intellij.plugins.haxe.ide.formatter;
  exports com.intellij.plugins.haxe.ide.formatter.settings;
  exports com.intellij.plugins.haxe.ide.generation;
  exports com.intellij.plugins.haxe.ide.highlight;
  exports com.intellij.plugins.haxe.ide.index;
  exports com.intellij.plugins.haxe.ide.info;
  exports com.intellij.plugins.haxe.ide.inspections;
  exports com.intellij.plugins.haxe.ide.intention;
  exports com.intellij.plugins.haxe.ide.library;
  exports com.intellij.plugins.haxe.ide.module;
  exports com.intellij.plugins.haxe.ide.projectStructure.ui;
  exports com.intellij.plugins.haxe.ide.refactoring;
  exports com.intellij.plugins.haxe.ide.refactoring.introduce;
  exports com.intellij.plugins.haxe.ide.refactoring.move;
  exports com.intellij.plugins.haxe.ide.structure;
  exports com.intellij.plugins.haxe.ide.surroundWith;
  exports com.intellij.plugins.haxe.ide.template;
  exports com.intellij.plugins.haxe.ide.template.macro;
  exports com.intellij.plugins.haxe.lang;
  exports com.intellij.plugins.haxe.lang.lexer;
  exports com.intellij.plugins.haxe.lang.parser;
  exports com.intellij.plugins.haxe.lang.psi;
  exports com.intellij.plugins.haxe.lang.psi.impl;
  exports com.intellij.plugins.haxe.lang.psi.manipulators;
  exports com.intellij.plugins.haxe.module;
  exports com.intellij.plugins.haxe.module.impl;
  exports com.intellij.plugins.haxe.nmml;
  exports com.intellij.plugins.haxe.runner;
  exports com.intellij.plugins.haxe.runner.debugger;
  exports com.intellij.plugins.haxe.runner.ui;
  exports com.intellij.plugins.haxe.util;
  exports consulo.haxe;
  exports consulo.haxe.icon;
  exports consulo.haxe.localize;
  exports consulo.haxe.module.extension;
  exports consulo.haxe.module.extension.packageSupport;
  exports consulo.haxe.psi.impl;
}