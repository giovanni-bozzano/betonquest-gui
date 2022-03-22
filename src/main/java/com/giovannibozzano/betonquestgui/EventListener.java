package com.giovannibozzano.betonquestgui;

import com.giovannibozzano.betonquestgui.gui.compass.CompassOverlay;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BetonQuestGui.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class EventListener
{
    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event)
    {
        if (event.getEntity() instanceof LocalPlayer)
        {
            CompassOverlay.marker_location = null;
        }
    }

}
