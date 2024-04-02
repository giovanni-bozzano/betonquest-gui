package com.giovannibozzano.betonquestgui.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

public class BQGConfig
{
    static final ForgeConfigSpec clientSpec;
    public static final Compass COMPASS_CONFIG;

    static {
        final Pair<Compass, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Compass::new);
        clientSpec = specPair.getRight();
        COMPASS_CONFIG = specPair.getLeft();
    }

    public static void register() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, clientSpec);
    }

    public static class Compass
    {
        public final ForgeConfigSpec.BooleanValue showCompass;
        public final ForgeConfigSpec.BooleanValue showDistance;

        Compass(ForgeConfigSpec.Builder builder)
        {
            builder.push("BetonQuestGui");
            this.showCompass = builder
                    .comment("If true show the compass on screen")
                    .define("showCompass", true);
            this.showDistance = builder
                    .comment("If true show the distance from the quest marker")
                    .define("showDistance", true);
            builder.pop();
        }
    }
}
