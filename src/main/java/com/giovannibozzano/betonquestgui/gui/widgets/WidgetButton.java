package com.giovannibozzano.betonquestgui.gui.widgets;

import com.giovannibozzano.betonquestgui.BetonQuestGui;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;
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
    private final Button.OnPress onPress;
    private final int type;

    public WidgetButton(int x, int y, int width, int height, Button.OnPress onPress, int type)
    {
        super(x, y, width, height, CommonComponents.EMPTY, onPress);
        this.onPress = onPress;
        this.type = type;
    }

    @Override
    public void onPress()
    {
        this.onPress.onPress(this);
    }

    @Override
    public void render(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float unused)
    {
        if (!this.visible) {
            return;
        }

        if (this.type == 0) {
            if (this.isMouseOver(mouseX, mouseY)) {
                RenderSystem.setShaderTexture(0, BUTTON_DOWN_OVER_TEXTURE);
            } else {
                RenderSystem.setShaderTexture(0, BUTTON_DOWN_TEXTURE);
            }
        } else {
            if (this.isMouseOver(mouseX, mouseY)) {
                RenderSystem.setShaderTexture(0, BUTTON_UP_OVER_TEXTURE);
            } else {
                RenderSystem.setShaderTexture(0, BUTTON_UP_TEXTURE);
            }
        }

        RenderSystem.setShader(GameRenderer::getPositionTexShader);

        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.vertex(this.x, this.y + this.height, this.getBlitOffset()).uv(0, 1).endVertex();
        bufferBuilder.vertex(this.x + this.width, this.y + this.height, this.getBlitOffset()).uv(1, 1).endVertex();
        bufferBuilder.vertex(this.x + this.width, this.y, this.getBlitOffset()).uv(1, 0).endVertex();
        bufferBuilder.vertex(this.x, this.y, this.getBlitOffset()).uv(0, 0).endVertex();

        BufferUploader.drawWithShader(bufferBuilder.end());
    }

    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }
}
