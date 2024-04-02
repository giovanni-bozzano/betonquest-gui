package com.giovannibozzano.betonquestgui.gui.widgets;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class WidgetCenteredText implements Renderable
{
    private final MutableComponent text;
    private final int x;
    private final int y;
    private final float scale;

    public WidgetCenteredText(MutableComponent text, int x, int y, float scale)
    {
        this.text = text;
        this.x = x;
        this.y = y;
        this.scale = scale;
    }

    public WidgetCenteredText(MutableComponent text, int x, int y)
    {
        this(text, x, y, 1.0F);
    }

    @Override
    public void render(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float unused)
    {
        guiGraphics.pose().scale(this.scale, this.scale, this.scale);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, this.text, (int) (this.x / this.scale), (int) (this.y / this.scale), ChatFormatting.WHITE.getColor());
        guiGraphics.pose().scale(1.0F / this.scale, 1.0F / this.scale, 1.0F / this.scale);
    }
}
