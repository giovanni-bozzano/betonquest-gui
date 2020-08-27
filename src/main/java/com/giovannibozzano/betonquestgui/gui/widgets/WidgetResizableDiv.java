package com.giovannibozzano.betonquestgui.gui.widgets;

import com.giovannibozzano.betonquestgui.BetonQuestGui;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.IRenderable;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WidgetResizableDiv extends AbstractGui implements IRenderable
{
    private static final ResourceLocation BORDER_TEXTURE = new ResourceLocation(BetonQuestGui.MOD_ID, "textures/gui/border.png");
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(BetonQuestGui.MOD_ID, "textures/gui/background.png");
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final float red;
    private final float green;
    private final float blue;
    private final float alpha;

    public WidgetResizableDiv(int x, int y, int width, int height, float red, float green, float blue, float alpha)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public WidgetResizableDiv(int x, int y, int width, int height)
    {
        this(x, y, width, height, 0.5F, 0.5F, 0.5F, 0.5F);
    }

    @Override
    public void render(int mouseX, int mouseY, float f)
    {
        Minecraft minecraft = Minecraft.getInstance();
        RenderSystem.color4f(this.red, this.green, this.blue, this.alpha);
        minecraft.getTextureManager().bindTexture(BACKGROUND_TEXTURE);
        this.renderBackground(this.x + 6, this.y + 6, 0, 0, this.width - 12, this.height - 12);
        minecraft.getTextureManager().bindTexture(BORDER_TEXTURE);
        this.renderBorder(this.x, this.y, 0, 0, 6, 6);
        boolean flag = true;
        int counter = 0;
        while (flag) {
            int length = this.width - 108 * counter;
            if (length > 108) {
                length = 108;
            } else {
                flag = false;
                length -= 12;
            }
            this.renderBorder(this.x + 6 + 108 * counter, this.y, 6, 0, length, 6);
            counter++;
        }
        flag = true;
        counter = 0;
        while (flag) {
            int length = this.height - 71 * counter;
            if (length > 71) {
                length = 71;
            } else {
                flag = false;
                length -= 12;
            }
            this.renderBorder(this.x, this.y + 6 + 71 * counter, 0, 6, 6, length);
            counter++;
        }
        flag = true;
        counter = 0;
        while (flag) {
            int length = this.height - 71 * counter;
            if (length > 71) {
                length = 71;
            } else {
                flag = false;
                length -= 12;
            }
            this.renderBorder(this.x + this.width - 6, this.y + 6 + 71 * counter, 118, 6, 6, length);
            counter++;
        }
        this.renderBorder(this.x + this.width - 6, this.y, 118, 0, 6, 6);
        this.renderBorder(this.x, this.y + this.height - 6, 0, 77, 6, 6);
        flag = true;
        counter = 0;
        while (flag) {
            int length = this.width - 108 * counter;
            if (length > 108) {
                length = 108;
            } else {
                flag = false;
                length -= 12;
            }
            this.renderBorder(this.x + 6 + 108 * counter, this.y + this.height - 6, 6, 77, length, 6);
            counter++;
        }
        this.renderBorder(this.x + this.width - 6, this.y + this.height - 6, 118, 77, 6, 6);
    }

    public void renderBorder(int x, int y, int offsetX, int offsetY, int width, int height)
    {
        this.renderTexture(x, y, offsetX, offsetY, width, height, 0.008F, 0.012F);
    }

    public void renderBackground(int x, int y, int offsetX, int offsetY, int width, int height)
    {
        this.renderTexture(x, y, offsetX, offsetY, width, height, 0.009F, 0.014F);
    }

    public void renderTexture(int x, int y, int offsetX, int offsetY, int width, int height, float f1, float f2)
    {
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferBuilder.pos(x, y + height, this.getBlitOffset()).tex(offsetX * f1, (offsetY + height) * f2).endVertex();
        bufferBuilder.pos(x + width, y + height, this.getBlitOffset()).tex((offsetX + width) * f1, (offsetY + height) * f2).endVertex();
        bufferBuilder.pos(x + width, y, this.getBlitOffset()).tex((offsetX + width) * f1, offsetY * f2).endVertex();
        bufferBuilder.pos(x, y, this.getBlitOffset()).tex(offsetX * f1, offsetY * f2).endVertex();
        bufferBuilder.finishDrawing();
        RenderSystem.enableAlphaTest();
        WorldVertexBufferUploader.draw(bufferBuilder);
    }
}
