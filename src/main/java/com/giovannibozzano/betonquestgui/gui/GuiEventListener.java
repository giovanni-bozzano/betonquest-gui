package com.giovannibozzano.betonquestgui.gui;

import com.giovannibozzano.betonquestgui.BetonQuestGui;
import com.giovannibozzano.betonquestgui.config.BQGConfig;
import com.giovannibozzano.betonquestgui.gui.compass.CompassOverlay;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = BetonQuestGui.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class GuiEventListener
{
    @SubscribeEvent
    public static void onRenderGameOverlayPre(RenderGuiOverlayEvent.Pre event) {
        if(!BQGConfig.COMPASS.showCompass.get()) return;

        if (VanillaGuiOverlay.BOSS_EVENT_PROGRESS.type() == event.getOverlay() || VanillaGuiOverlay.PLAYER_LIST.type() == event.getOverlay()) {
            event.getPoseStack().translate(0, (CompassOverlay.POSY + 28), 0);
        }
    }

    @SubscribeEvent
    public static void onRenderGameOverlayPost(RenderGuiOverlayEvent.Post event) {
        if(!BQGConfig.COMPASS.showCompass.get()) return;

        if (VanillaGuiOverlay.BOSS_EVENT_PROGRESS.type() == event.getOverlay() || VanillaGuiOverlay.PLAYER_LIST.type() == event.getOverlay()) {
            event.getPoseStack().translate(0, -(CompassOverlay.POSY + 28), 0);
        }
    }
}
