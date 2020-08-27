package com.giovannibozzano.betonquestgui;

import com.giovannibozzano.betonquestgui.network.PacketHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;

@Mod(BetonQuestGui.MOD_ID)
@Mod.EventBusSubscriber(modid = BetonQuestGui.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BetonQuestGui
{
    public static final String MOD_ID = "betonquestgui";
    public static final String PROTOCOL_VERSION = "1.0.0";

    @SubscribeEvent
    public static void setup(FMLCommonSetupEvent event)
    {
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
        DistExecutor.runWhenOn(Dist.CLIENT, () -> PacketHandler::registerPackets);
    }
}
