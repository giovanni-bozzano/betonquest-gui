package com.giovannibozzano.betonquestgui.gui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class WidgetCenteredText extends GuiComponent implements Widget
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
    public void render(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float unused)
    {
        matrixStack.scale(this.scale, this.scale, this.scale);
        drawCenteredString(matrixStack, Minecraft.getInstance().font, this.text, (int) (this.x / this.scale), (int) (this.y / this.scale), ChatFormatting.WHITE.getColor());
        matrixStack.scale(1.0F / this.scale, 1.0F / this.scale, 1.0F / this.scale);
    }
}
