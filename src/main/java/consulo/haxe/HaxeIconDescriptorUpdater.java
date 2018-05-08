package consulo.haxe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.intellij.openapi.util.Iconable;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.plugins.haxe.HaxeComponentType;
import com.intellij.plugins.haxe.lang.psi.HaxeComponent;
import com.intellij.plugins.haxe.lang.psi.HaxeFile;
import com.intellij.plugins.haxe.util.HaxeResolveUtil;
import com.intellij.psi.PsiElement;
import consulo.ide.IconDescriptor;
import consulo.ide.IconDescriptorUpdater;
import consulo.ide.IconDescriptorUpdaters;
import consulo.ui.image.Image;

/**
 * @author VISTALL
 * @since 12.09.13.
 */
public class HaxeIconDescriptorUpdater implements IconDescriptorUpdater
{
	@Override
	public void updateIcon(@Nonnull IconDescriptor iconDescriptor, @Nonnull PsiElement element, int flags)
	{
		if(element instanceof HaxeFile)
		{
			iconDescriptor.setMainIcon(getHaxeFileIcon((HaxeFile) element, flags));
		}
		else
		{
			HaxeComponentType haxeComponentType = HaxeComponentType.typeOf(element);
			if(haxeComponentType != null)
			{
				iconDescriptor.setMainIcon(haxeComponentType.getIcon());
			}
		}
	}

	@Nullable
	private static Image getHaxeFileIcon(HaxeFile file, @Iconable.IconFlags int flags)
	{
		final String fileName = FileUtil.getNameWithoutExtension(file.getName());
		for(HaxeComponent component : HaxeResolveUtil.findComponentDeclarations(file))
		{
			if(fileName.equals(component.getName()))
			{
				return IconDescriptorUpdaters.getIcon(component, flags);
			}
		}
		return null;
	}
}
