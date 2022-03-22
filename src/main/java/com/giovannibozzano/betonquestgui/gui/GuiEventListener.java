package com.giovannibozzano.betonquestgui.gui;

import com.giovannibozzano.betonquestgui.BetonQuestGui;
import com.giovannibozzano.betonquestgui.config.BQGConfig;
import com.giovannibozzano.betonquestgui.gui.compass.CompassOverlay;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BetonQuestGui.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class GuiEventListener
{
    @SubscribeEvent
    public static void onRenderGameOverlayPre(RenderGameOverlayEvent.Pre event) {
            if(!BQGConfig.COMPASS.showCompass.get()) return;

            switch (event.getType()) {
            case BOSSINFO, PLAYER_LIST -> event.getMatrixStack().translate(0, (CompassOverlay.POSY + 28), 0);
        }
    }

    @SubscribeEvent
    public static void onRenderGameOverlayPost(RenderGameOverlayEvent.Post event) {
        if(!BQGConfig.COMPASS.showCompass.get()) return;

        switch (event.getType()) {
            case BOSSINFO, PLAYER_LIST -> event.getMatrixStack().translate(0, -(CompassOverlay.POSY + 28), 0);
        }
    }
}
