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
package com.intellij.plugins.haxe.codeInspection;

import com.intellij.plugins.haxe.nmml.NMMLFileType;
import consulo.annotation.component.ExtensionImpl;
import consulo.haxe.localize.HaxeLocalize;
import consulo.language.Language;
import consulo.language.editor.inspection.LocalInspectionTool;
import consulo.language.editor.inspection.LocalQuickFix;
import consulo.language.editor.inspection.ProblemDescriptor;
import consulo.language.editor.inspection.ProblemHighlightType;
import consulo.language.editor.inspection.scheme.InspectionManager;
import consulo.language.editor.rawHighlight.HighlightDisplayLevel;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.localize.LocalizeValue;
import consulo.project.Project;
import consulo.util.collection.ContainerUtil;
import consulo.util.io.FileUtil;
import consulo.util.lang.function.Condition;
import consulo.xml.lang.xml.XMLLanguage;
import consulo.xml.psi.XmlRecursiveElementVisitor;
import consulo.xml.psi.xml.XmlAttribute;
import consulo.xml.psi.xml.XmlFile;
import consulo.xml.psi.xml.XmlTag;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fedor.Korotkov
 */
@ExtensionImpl
public class NMEBuildDirectoryInspection extends LocalInspectionTool {
    @Nonnull
    @Override
    public LocalizeValue getGroupDisplayName() {
        return HaxeLocalize.haxeInspectionsGroupName();
    }

    @Nullable
    @Override
    public Language getLanguage() {
        return XMLLanguage.INSTANCE;
    }

    @Nonnull
    @Override
    public LocalizeValue getDisplayName() {
        return HaxeLocalize.haxeInspectionsNmeBuildDirectory();
    }

    @Nonnull
    @Override
    public HighlightDisplayLevel getDefaultLevel() {
        return HighlightDisplayLevel.WARNING;
    }

    @Override
    public ProblemDescriptor[] checkFile(@Nonnull PsiFile file, @Nonnull InspectionManager manager, boolean isOnTheFly) {
        final boolean isNmml = FileUtil.extensionEquals(file.getName(), NMMLFileType.DEFAULT_EXTENSION);
        if (!isNmml || !(file instanceof XmlFile)) {
            return ProblemDescriptor.EMPTY_ARRAY;
        }
        MyVisitor visitor = new MyVisitor();
        file.accept(visitor);

        if (ContainerUtil.exists(visitor.getResult(), new Condition<XmlTag>() {
            @Override
            public boolean value(XmlTag tag) {
                final XmlAttribute ifAttribute = tag.getAttribute("if");
                return "debug".equals(ifAttribute != null ? ifAttribute.getValue() : null);
            }
        })) {
            // all good
            return ProblemDescriptor.EMPTY_ARRAY;
        }

        final XmlTag lastTag = ContainerUtil.iterateAndGetLastItem(visitor.getResult());

        if (lastTag == null) {
            return ProblemDescriptor.EMPTY_ARRAY;
        }

        final ProblemDescriptor descriptor = manager.createProblemDescriptor(
            lastTag,
            HaxeLocalize.haxeInspectionsNmeBuildDirectoryDescriptor().get(),
            new AddTagFix(),
            ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
            isOnTheFly
        );

        return new ProblemDescriptor[]{descriptor};
    }

    private static class MyVisitor extends XmlRecursiveElementVisitor {
        final List<XmlTag> myResult = new ArrayList<XmlTag>();

        public List<XmlTag> getResult() {
            return myResult;
        }

        @Override
        public void visitXmlTag(XmlTag tag) {
            super.visitXmlTag(tag);
            if ("set".equals(tag.getName())) {
                final XmlAttribute name = tag.getAttribute("name");
                if ("BUILD_DIR".equals(name != null ? name.getValue() : null)) {
                    myResult.add(tag);
                }
            }
        }
    }

    private static class AddTagFix implements LocalQuickFix {
        @Nonnull
        @Override
        public LocalizeValue getName() {
            return HaxeLocalize.haxeInspectionsNmeBuildDirectoryFixName();
        }

        @Override
        public void applyFix(@Nonnull Project project, @Nonnull ProblemDescriptor descriptor) {
            final PsiElement element = descriptor.getPsiElement();
            assert element instanceof XmlTag;

            final XmlTag parentTag = ((XmlTag) element).getParentTag();
            final PsiElement debugTag = parentTag.addAfter(element.copy(), element);
            assert debugTag instanceof XmlTag;

            appendValue((XmlTag) element, "release");
            appendValue((XmlTag) debugTag, "debug");

            ((XmlTag) debugTag).setAttribute("if", "debug");
        }

        private void appendValue(XmlTag element, String release) {
            XmlAttribute outAttribute = ((XmlTag) element).getAttribute("value");
            if (outAttribute != null) {
                outAttribute.setValue(outAttribute.getValue() + "/" + release);
            }
        }
    }
}
