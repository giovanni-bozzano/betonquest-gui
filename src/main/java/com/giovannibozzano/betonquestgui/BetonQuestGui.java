package com.giovannibozzano.betonquestgui;

import com.giovannibozzano.betonquestgui.network.PacketHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fmllegacy.network.FMLNetworkConstants;

@Mod(BetonQuestGui.MOD_ID)
@Mod.EventBusSubscriber(modid = BetonQuestGui.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BetonQuestGui
{
    public static final String MOD_ID = "betonquestgui";
    public static final String PROTOCOL_VERSION = "1.0.0";

    @SubscribeEvent
    public static void setup(FMLCommonSetupEvent event)
    {
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> FMLNetworkConstants.IGNORESERVERONLY, (incoming, isNetwork) -> true));
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> PacketHandler::registerPackets);
    }
}
