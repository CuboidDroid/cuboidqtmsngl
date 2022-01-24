package com.cuboiddroid.cuboidqs;

import com.cuboiddroid.cuboidqs.setup.ModBlocks;
import com.cuboiddroid.cuboidqs.setup.Registration;
import com.cuboiddroid.cuboidqs.util.QuantumSingularityPaths;
import com.google.common.eventbus.Subscribe;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(CuboidQuantumSingularitiesMod.MOD_ID)
public class CuboidQuantumSingularitiesMod
{
    public static final String MOD_ID = "cuboidqs";

    public static final ItemGroup CUBOIDQS_ITEM_GROUP = (new ItemGroup("cuboidqs") {
        @Override
        @OnlyIn(Dist.CLIENT)
        public ItemStack makeIcon() {
            return new ItemStack(ModBlocks.IRON_QUANTUM_COLLAPSER.get());
        }
    });

    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();

    public CuboidQuantumSingularitiesMod() {
        QuantumSingularityPaths.createDirectories();

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.COMMON_CONFIG);

        Registration.register();

        Config.loadConfig(Config.COMMON_CONFIG, QuantumSingularityPaths.MOD_CONFIG.resolve("cuboidqs.toml"));

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static ResourceLocation getModId(String path) {
        return new ResourceLocation(MOD_ID + ":" + path);
    }
}
