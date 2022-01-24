package com.cuboiddroid.cuboidqs.setup;

import com.cuboiddroid.cuboidqs.CuboidQuantumSingularitiesMod;
import com.cuboiddroid.cuboidqs.modules.collapser.handler.ColorHandler;
import com.cuboiddroid.cuboidqs.modules.collapser.recipe.DynamicRecipeInjector;
import com.cuboiddroid.cuboidqs.modules.collapser.registry.QuantumSingularityRegistry;
import com.cuboiddroid.cuboidqs.modules.collapser.screen.*;
import com.cuboiddroid.cuboidqs.network.NetworkHandler;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class Registration {
    public static final DeferredRegister<Block> BLOCKS = create(ForgeRegistries.BLOCKS);
    public static final DeferredRegister<Item> ITEMS = create(ForgeRegistries.ITEMS);
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = create(ForgeRegistries.CONTAINERS);
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = create(ForgeRegistries.TILE_ENTITIES);
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = create(ForgeRegistries.RECIPE_SERIALIZERS);
    public static final DeferredRegister<Fluid> FLUIDS = create(ForgeRegistries.FLUIDS);

    public static void register() {
        //QuantumSingularityRegistry.getInstance().loadSingularities();

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(modEventBus);
        CONTAINERS.register(modEventBus);
        ITEMS.register(modEventBus);
        TILE_ENTITIES.register(modEventBus);
        RECIPE_SERIALIZERS.register(modEventBus);
        FLUIDS.register(modEventBus);

        // class-load the registry object holder classes.
        ModBlocks.register();
        ModContainers.register();
        ModItems.register();
        ModRecipeTypes.register();
        ModRecipeSerializers.register();
        ModTileEntities.register();

        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            // Client setup
            modEventBus.register(new ColorHandler());
        });
    }

    private static <T extends IForgeRegistryEntry<T>> DeferredRegister<T> create(IForgeRegistry<T> registry) {
        return DeferredRegister.create(registry, CuboidQuantumSingularitiesMod.MOD_ID);
    }

    @Mod.EventBusSubscriber(modid = CuboidQuantumSingularitiesMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static final class Common {
        private Common() {}

        @SubscribeEvent
        public static void onCommonSetup(FMLCommonSetupEvent event)
        {
            MinecraftForge.EVENT_BUS.register(new DynamicRecipeInjector());

            event.enqueueWork(() -> {
                NetworkHandler.onCommonSetup();
                QuantumSingularityRegistry.getInstance().loadSingularities();
            });
        }
    }

    @Mod.EventBusSubscriber(value=Dist.CLIENT, modid = CuboidQuantumSingularitiesMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static final class Client {
        private Client() {}

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            ScreenManager.register(ModContainers.IRON_QUANTUM_COLLAPSER.get(), IronQuantumCollapserScreen::new);
            ScreenManager.register(ModContainers.GOLD_QUANTUM_COLLAPSER.get(), GoldQuantumCollapserScreen::new);
            ScreenManager.register(ModContainers.DIAMOND_QUANTUM_COLLAPSER.get(), DiamondQuantumCollapserScreen::new);
            ScreenManager.register(ModContainers.EMERALD_QUANTUM_COLLAPSER.get(), EmeraldQuantumCollapserScreen::new);
            ScreenManager.register(ModContainers.NETHERITE_QUANTUM_COLLAPSER.get(), NetheriteQuantumCollapserScreen::new);
        }
    }
}
