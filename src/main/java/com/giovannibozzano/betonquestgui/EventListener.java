package com.giovannibozzano.betonquestgui;

import com.giovannibozzano.betonquestgui.gui.compass.CompassOverlay;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class EventListener
{
    @Mod.EventBusSubscriber(modid = BetonQuestGui.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class ForgeBus {
        @SubscribeEvent
        public static void onEntityJoinWorld(EntityJoinLevelEvent event)
        {
            if (event.getEntity() instanceof LocalPlayer)
            {
                CompassOverlay.marker_location = null;
            }
        }
    }

    @Mod.EventBusSubscriber(modid = BetonQuestGui.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ModBus{
        @SubscribeEvent
        public static void registerOverlays(RegisterGuiOverlaysEvent event) {
            event.registerBelow(VanillaGuiOverlay.DEBUG_TEXT.id(),"beton_gui_compass", CompassOverlay.COMPASS_OVERLAY);
        }
    }
}
