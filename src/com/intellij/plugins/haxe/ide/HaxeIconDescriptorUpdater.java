package com.intellij.plugins.haxe.ide;

import javax.swing.Icon;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.ide.IconDescriptor;
import com.intellij.ide.IconDescriptorUpdater;
import com.intellij.ide.IconDescriptorUpdaters;
import com.intellij.openapi.util.Iconable;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.plugins.haxe.HaxeComponentType;
import com.intellij.plugins.haxe.lang.psi.HaxeComponent;
import com.intellij.plugins.haxe.lang.psi.HaxeFile;
import com.intellij.plugins.haxe.util.HaxeResolveUtil;
import com.intellij.psi.PsiElement;

/**
 * @author VISTALL
 * @since 12.09.13.
 */
public class HaxeIconDescriptorUpdater implements IconDescriptorUpdater
{
	@Override
	public void updateIcon(@NotNull IconDescriptor iconDescriptor, @NotNull PsiElement element, int flags)
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
	private static Icon getHaxeFileIcon(HaxeFile file, @Iconable.IconFlags int flags)
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
