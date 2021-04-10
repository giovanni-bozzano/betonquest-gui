package com.giovannibozzano.betonquestgui.gui.widgets;

import com.giovannibozzano.betonquestgui.BetonQuestGui;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class WidgetButton extends Button
{
    private static final ResourceLocation BUTTON_DOWN_TEXTURE = new ResourceLocation(BetonQuestGui.MOD_ID, "textures/gui/button_down.png");
    private static final ResourceLocation BUTTON_DOWN_OVER_TEXTURE = new ResourceLocation(BetonQuestGui.MOD_ID, "textures/gui/button_down_over.png");
    private static final ResourceLocation BUTTON_UP_TEXTURE = new ResourceLocation(BetonQuestGui.MOD_ID, "textures/gui/button_up.png");
    private static final ResourceLocation BUTTON_UP_OVER_TEXTURE = new ResourceLocation(BetonQuestGui.MOD_ID, "textures/gui/button_up_over.png");
    private final Button.IPressable onPress;
    private final int type;

    public WidgetButton(int x, int y, int width, int height, Button.IPressable onPress, int type)
    {
        super(x, y, width, height, new StringTextComponent(""), onPress);
        this.onPress = onPress;
        this.type = type;
    }

    @Override
    public void onPress()
    {
        this.onPress.onPress(this);
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float unused)
    {
        if (!this.visible) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        if (this.type == 0) {
            if (this.isMouseOver(mouseX, mouseY)) {
                minecraft.getTextureManager().bind(BUTTON_DOWN_OVER_TEXTURE);
            } else {
                minecraft.getTextureManager().bind(BUTTON_DOWN_TEXTURE);
            }
        } else {
            if (this.isMouseOver(mouseX, mouseY)) {
                minecraft.getTextureManager().bind(BUTTON_UP_OVER_TEXTURE);
            } else {
                minecraft.getTextureManager().bind(BUTTON_UP_TEXTURE);
            }
        }
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuilder();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferBuilder.vertex(this.x, this.y + this.height, this.getBlitOffset()).uv(0, 1).endVertex();
        bufferBuilder.vertex(this.x + this.width, this.y + this.height, this.getBlitOffset()).uv(1, 1).endVertex();
        bufferBuilder.vertex(this.x + this.width, this.y, this.getBlitOffset()).uv(1, 0).endVertex();
        bufferBuilder.vertex(this.x, this.y, this.getBlitOffset()).uv(0, 0).endVertex();
        bufferBuilder.end();
        RenderSystem.enableAlphaTest();
        WorldVertexBufferUploader.end(bufferBuilder);
    }

    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }
}
