package com.giovannibozzano.betonquestgui.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.IRenderable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WidgetCenteredText extends AbstractGui implements IRenderable
{
    private final ITextComponent text;
    private final int x;
    private final int y;
    private final double scale;

    public WidgetCenteredText(ITextComponent text, int x, int y, double scale)
    {
        this.text = text;
        this.x = x;
        this.y = y;
        this.scale = scale;
    }

    public WidgetCenteredText(ITextComponent text, int x, int y)
    {
        this(text, x, y, 1.0D);
    }

    @Override
    public void render(int mouseX, int mouseY, float unused)
    {
        RenderSystem.scaled(this.scale, this.scale, this.scale);
        this.drawCenteredString(Minecraft.getInstance().fontRenderer, this.text.getFormattedText(), (int) (this.x / this.scale), (int) (this.y / this.scale), TextFormatting.WHITE.getColor());
        RenderSystem.scaled(1.0 / this.scale, 1.0 / this.scale, 1.0 / this.scale);
    }
}
