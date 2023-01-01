package consulo.haxe;

import com.intellij.plugins.haxe.HaxeComponentType;
import com.intellij.plugins.haxe.lang.psi.HaxeComponent;
import com.intellij.plugins.haxe.lang.psi.HaxeFile;
import com.intellij.plugins.haxe.util.HaxeResolveUtil;
import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.component.ExtensionImpl;
import consulo.component.util.Iconable;
import consulo.language.icon.IconDescriptor;
import consulo.language.icon.IconDescriptorUpdater;
import consulo.language.icon.IconDescriptorUpdaters;
import consulo.language.psi.PsiElement;
import consulo.ui.image.Image;
import consulo.util.io.FileUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author VISTALL
 * @since 12.09.13.
 */
@ExtensionImpl
public class HaxeIconDescriptorUpdater implements IconDescriptorUpdater {
  @RequiredReadAction
  @Override
  public void updateIcon(@Nonnull IconDescriptor iconDescriptor, @Nonnull PsiElement element, int flags) {
    if (element instanceof HaxeFile) {
      iconDescriptor.setMainIcon(getHaxeFileIcon((HaxeFile) element, flags));
    } else {
      HaxeComponentType haxeComponentType = HaxeComponentType.typeOf(element);
      if (haxeComponentType != null) {
        iconDescriptor.setMainIcon(haxeComponentType.getIcon());
      }
    }
  }

  @Nullable
  private static Image getHaxeFileIcon(HaxeFile file, @Iconable.IconFlags int flags) {
    final String fileName = FileUtil.getNameWithoutExtension(file.getName());
    for (HaxeComponent component : HaxeResolveUtil.findComponentDeclarations(file)) {
      if (fileName.equals(component.getName())) {
        return IconDescriptorUpdaters.getIcon(component, flags);
      }
    }
    return null;
  }
}
