package com.giovannibozzano.betonquestgui.gui.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.IRenderable;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class WidgetText extends AbstractGui implements IRenderable
{
    private final IFormattableTextComponent text;
    private final int x;
    private final int y;
    private final double scale;

    public WidgetText(IFormattableTextComponent text, int x, int y, double scale)
    {
        this.text = text;
        this.x = x;
        this.y = y;
        this.scale = scale;
    }

    public WidgetText(IFormattableTextComponent text, int x, int y)
    {
        this(text, x, y, 1.0F);
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float unused)
    {
        RenderSystem.scaled(this.scale, this.scale, this.scale);
        drawString(matrixStack, Minecraft.getInstance().font, this.text, (int) (this.x / this.scale), (int) (this.y / this.scale), TextFormatting.WHITE.getColor());
        RenderSystem.scaled(1.0F / this.scale, 1.0F / this.scale, 1.0F / this.scale);
    }
}
