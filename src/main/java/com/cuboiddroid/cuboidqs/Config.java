package com.cuboiddroid.cuboidqs;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

import java.nio.file.Path;

@Mod.EventBusSubscriber
public class Config {
    public static ForgeConfigSpec COMMON_CONFIG;

    public static final String CATEGORY_QUANTUM_COLLAPSERS = "collapsers";
    public static ForgeConfigSpec.IntValue defaultRecipeTicks;
    public static ForgeConfigSpec.IntValue defaultInputCount;
    public static ForgeConfigSpec.DoubleValue ironQuantumCollapserSpeed;
    public static ForgeConfigSpec.DoubleValue goldQuantumCollapserSpeed;
    public static ForgeConfigSpec.DoubleValue diamondQuantumCollapserSpeed;
    public static ForgeConfigSpec.DoubleValue emeraldQuantumCollapserSpeed;
    public static ForgeConfigSpec.DoubleValue netheriteQuantumCollapserSpeed;

    // --- JEI CATEGORY ---
    public static final String CATEGORY_JEI = "jei";
    public static ForgeConfigSpec.BooleanValue enableJeiPlugin;
    public static ForgeConfigSpec.BooleanValue enableJeiCatalysts;
    public static ForgeConfigSpec.BooleanValue enableJeiClickArea;

    // --- MISC CATEGORY ---
    public static final String CATEGORY_MISC = "misc";
    public static ForgeConfigSpec.BooleanValue verboseLogging;

    static {
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
        ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

        COMMON_BUILDER.comment("Quantum Collapser Settings").push(CATEGORY_QUANTUM_COLLAPSERS);
        setupQuantumCollapsersConfig(COMMON_BUILDER, CLIENT_BUILDER);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("JEI Settings").push(CATEGORY_JEI);
        setupJEIConfig(COMMON_BUILDER, CLIENT_BUILDER);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Misc").push(CATEGORY_MISC);
        verboseLogging = COMMON_BUILDER
                .comment(" Logs additional information when loading Quantum Singularity config files.")
                .define("verbose_logging", false);
        COMMON_BUILDER.pop();

        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    private static void setupQuantumCollapsersConfig(ForgeConfigSpec.Builder COMMON_BUILDER, ForgeConfigSpec.Builder CLIENT_BUILDER) {
        defaultRecipeTicks = COMMON_BUILDER
                .comment(" The base number of ticks to use for collapsing recipes.\n 600 means it takes 30 seconds (20 ticks per second).\n Default: 600")
                .defineInRange("recipe.defaultTicks", 600, 1, 72000);

        defaultInputCount = COMMON_BUILDER
                .comment(" The default number of items required for collapsing recipes.\n 640 means it takes 10 stacks (640 items).\n Default: 640")
                .defineInRange("recipe.defaultCount", 640, 1, 50000);

        ironQuantumCollapserSpeed = COMMON_BUILDER
                .comment(" Speed at which the collapser works.\n 1.0 means the collapser runs at normal speed according to the recipe being used.\n Default: 1.0")
                .defineInRange("speed.iron", 1.0F, 1.0F, 20.0F);

        goldQuantumCollapserSpeed = COMMON_BUILDER
                .comment(" Speed at which the collapser works.\n 1.2 means the collapser runs 20% faster than the recipe being used specifies (e.g. 200 / 1.2 = 167 ticks).\n Default: 1.2")
                .defineInRange("speed.gold", 1.2F, 1.0F, 20.0F);

        diamondQuantumCollapserSpeed = COMMON_BUILDER
                .comment(" Speed at which the collapser works.\n 1.5 means the collapser runs 50% faster than the recipe being used specifies (e.g. 200 / 1.5 = 133 ticks).\n Default: 1.5")
                .defineInRange("speed.diamond", 1.5F, 1.0F, 20.0F);

        emeraldQuantumCollapserSpeed = COMMON_BUILDER
                .comment(" Speed at which the collapser works.\n 2.5 means the collapser runs 150% faster than the recipe being used specifies (e.g. 200 / 2.5 = 80 ticks).\n Default: 2.5")
                .defineInRange("speed.emerald", 2.5F, 1.0F, 20.0F);

        netheriteQuantumCollapserSpeed = COMMON_BUILDER
                .comment(" Speed at which the collapser works.\n 5.0 means the collapser runs 400% faster than the recipe being used specifies (e.g. 200 / 5.0 = 40 ticks).\n Default: 5.0")
                .defineInRange("speed.netherite", 5.0F, 1.0F, 20.0F);
    }

    private static void setupJEIConfig(ForgeConfigSpec.Builder COMMON_BUILDER, ForgeConfigSpec.Builder CLIENT_BUILDER) {
        enableJeiPlugin = COMMON_BUILDER
                .comment(" Enable or disable the JeiPlugin of Cuboid machines.").define("enable_jei", true);

        enableJeiCatalysts = COMMON_BUILDER
                .comment(" Enable or disable the Catalysts in Jei for Cuboid machines.").define("enable_jei_catalysts", true);

        enableJeiClickArea = COMMON_BUILDER
                .comment(" Enable or disable the Click Area inside the GUI in all Cuboid machines.").define("enable_jei_click_area", true);
    }


    public static void loadConfig(ForgeConfigSpec spec, Path path) {
        CuboidQuantumSingularitiesMod.LOGGER.debug("Loading config file {}", path);

        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();

        CuboidQuantumSingularitiesMod.LOGGER.debug("Built TOML config for {}", path.toString());
        configData.load();
        CuboidQuantumSingularitiesMod.LOGGER.debug("Loaded TOML config file {}", path.toString());
        spec.setConfig(configData);
    }
}
