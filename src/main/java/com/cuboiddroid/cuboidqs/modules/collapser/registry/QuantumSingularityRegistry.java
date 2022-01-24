package com.cuboiddroid.cuboidqs.modules.collapser.registry;

import com.cuboiddroid.cuboidqs.Config;
import com.cuboiddroid.cuboidqs.CuboidQuantumSingularitiesMod;
import com.cuboiddroid.cuboidqs.network.message.SyncSingularitiesMessage;
import com.cuboiddroid.cuboidqs.util.QuantumSingularityPaths;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public final class QuantumSingularityRegistry {
    private static final QuantumSingularityRegistry INSTANCE = new QuantumSingularityRegistry();
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

    private final Map<ResourceLocation, QuantumSingularity> singularities = new LinkedHashMap<>();

    public void registerSingularity(QuantumSingularity singularity)
    {
        singularities.put(singularity.getId(), singularity);
    }

    public List<QuantumSingularity> getSingularities() {
        // ensure we've loaded our list of singularities. This is done here
        // because a dependant mod (e.g. SRG or SPG) might load up and need
        // this list before this mod initialises completely.
        if (this.singularities.isEmpty())
            loadSingularities();

        return Lists.newArrayList(this.singularities.values());
    }

    public QuantumSingularity getSingularityById(ResourceLocation id) {
        QuantumSingularity qs = this.singularities.get(id);
        if (qs != null)
            return qs;

        // not found - check defaults
        for (QuantumSingularity q: defaults()) {
            if (q.getId().toString().equalsIgnoreCase(id.toString())) {
                // found in defaults, use it, and add to in-memory & warn
                CuboidQuantumSingularitiesMod.LOGGER.warn("JSON config file missing for Quantum Singularity: " + id.getPath());
                this.singularities.put(q.getId(), q);
                return q;
            }
        }

        return null;
    }

    public static QuantumSingularityRegistry getInstance() {
        return INSTANCE;
    }

    public void loadSingularities() {
        if (!this.singularities.isEmpty()) {
            CuboidQuantumSingularitiesMod.LOGGER.info("{} Quantum Singularity type(s) already loaded.", this.singularities.size());
            return;
        }

        Stopwatch stopwatch = Stopwatch.createStarted();
        File dir = QuantumSingularityPaths.SINGULARITY_CONFIG.toFile();

        if (Config.verboseLogging.get()) CuboidQuantumSingularitiesMod.LOGGER.info("Loading quantum singularities");

        if (!dir.exists() && dir.mkdirs()) {
            if (Config.verboseLogging.get()) CuboidQuantumSingularitiesMod.LOGGER.info("Creating default singularity configs");
            for (QuantumSingularity singularity : defaults()) {
                JsonObject json = QuantumSingularityUtils.writeToJson(singularity);
                FileWriter writer = null;

                try {
                    File file = new File(dir, singularity.getId().getPath() + ".json");
                    writer = new FileWriter(file);
                    GSON.toJson(json, writer);
                    writer.close();
                } catch (Exception e) {
                    CuboidQuantumSingularitiesMod.LOGGER.error("An error occurred while generating default singularities", e);
                } finally {
                    IOUtils.closeQuietly(writer);
                }
            }
        }

        if (!dir.mkdirs() && dir.isDirectory()) {
            this.loadFiles(dir);
        }

        stopwatch.stop();
        CuboidQuantumSingularitiesMod.LOGGER.info("Loaded {} Quantum Singularity type(s) in {} ms", this.singularities.size(), stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    private void loadFiles(File dir) {
        File[] files = dir.listFiles((FileFilter) FileFilterUtils.suffixFileFilter(".json"));
        if (files == null)
            return;

        for (File file : files) {
            JsonObject json;
            FileReader reader = null;
            QuantumSingularity singularity = null;

            try {
                JsonParser parser = new JsonParser();
                reader = new FileReader(file);
                json = parser.parse(reader).getAsJsonObject();
                String name = file.getName().replace(".json", "");
                if (Config.verboseLogging.get()) CuboidQuantumSingularitiesMod.LOGGER.info("Loading Quantum Singularity file: " + name);
                singularity = QuantumSingularityUtils.loadFromJson(json);

                reader.close();
            } catch (Exception e) {
                CuboidQuantumSingularitiesMod.LOGGER.error("An error occurred while loading Quantum Singularities", e);
            } finally {
                IOUtils.closeQuietly(reader);
            }

            if (singularity != null && singularity.getEnabled()) {
                registerSingularity(singularity);
            }
        }
    }

    public void writeToBuffer(PacketBuffer buffer) {
        buffer.writeVarInt(this.singularities.size());

        this.singularities.forEach((id, singularity) -> {
            singularity.write(buffer);
        });
    }

    public List<QuantumSingularity> readFromBuffer(PacketBuffer buffer) {
        List<QuantumSingularity> singularities = new ArrayList<>();

        int size = buffer.readVarInt();

        for (int i = 0; i < size; i++) {
            QuantumSingularity singularity = QuantumSingularity.read(buffer);

            singularities.add(singularity);
        }

        return singularities;
    }

    public void loadSingularities(SyncSingularitiesMessage message) {
        Map<ResourceLocation, QuantumSingularity> singularities = message.getSingularities()
                .stream()
                .collect(Collectors.toMap(QuantumSingularity::getId, s -> s));

        this.singularities.clear();
        this.singularities.putAll(singularities);

        CuboidQuantumSingularitiesMod.LOGGER.info("Loaded {} singularities from the server", singularities.size());
    }

    private static List<QuantumSingularity> defaults() {
        return Lists.newArrayList(
                new QuantumSingularity(
                        new ResourceLocation(CuboidQuantumSingularitiesMod.MOD_ID, "coal_qs"),
                        false,
                        "coal_qs",
                        new int[] { 0x292828, 0x050505 },
                        "minecraft:coal",
                        Config.defaultInputCount.get(),
                        Config.defaultRecipeTicks.get(),
                        2),

                new QuantumSingularity(
                        new ResourceLocation(CuboidQuantumSingularitiesMod.MOD_ID, "cobblestone_qs"),
                        false,
                        "cobblestone_qs",
                        new int[] { 0xB5B5B5, 0x525252 },
                        "minecraft:cobblestone",
                        Config.defaultInputCount.get(),
                        Config.defaultRecipeTicks.get(),
                        1),

                new QuantumSingularity(
                        new ResourceLocation(CuboidQuantumSingularitiesMod.MOD_ID, "dirt_qs"),
                        false,
                        "dirt_qs",
                        new int[] { 0xB9855C, 0x593D29 },
                        "minecraft:dirt",
                        Config.defaultInputCount.get(),
                        Config.defaultRecipeTicks.get(),
                        1),

                new QuantumSingularity(
                        new ResourceLocation(CuboidQuantumSingularitiesMod.MOD_ID, "glowstone_qs"),
                        false,
                        "glowstone_qs",
                        new int[] { 0xfff236, 0xffe836 },
                        "minecraft:glowstone_dust",
                        Config.defaultInputCount.get(),
                        Config.defaultRecipeTicks.get(),
                        3),

                new QuantumSingularity(
                        new ResourceLocation(CuboidQuantumSingularitiesMod.MOD_ID, "gravel_qs"),
                        false,
                        "gravel_qs",
                        new int[] { 0xa5a5a5, 0x797979 },
                        "minecraft:gravel",
                        Config.defaultInputCount.get(),
                        Config.defaultRecipeTicks.get(),
                        1),

                new QuantumSingularity(
                        new ResourceLocation(CuboidQuantumSingularitiesMod.MOD_ID, "redstone_qs"),
                        false,
                        "redstone_qs",
                        new int[] { 0xE62008, 0x730C00 },
                        "minecraft:redstone",
                        Config.defaultInputCount.get(),
                        Config.defaultRecipeTicks.get(),
                        3),

                new QuantumSingularity(
                        new ResourceLocation(CuboidQuantumSingularitiesMod.MOD_ID, "sand_qs"),
                        false,
                        "sand_qs",
                        new int[] { 0xE3DBB0, 0xD1BA8A },
                        "minecraft:sand",
                        Config.defaultInputCount.get(),
                        Config.defaultRecipeTicks.get(),
                        1),

                new QuantumSingularity(
                        new ResourceLocation(CuboidQuantumSingularitiesMod.MOD_ID, "iron_qs"),
                        false,
                        "iron_qs",
                        new int[] { 0xECECEC, 0xB1B0B0 },
                        "#forge:ingots/iron",
                        Config.defaultInputCount.get(),
                        Config.defaultRecipeTicks.get(),
                        2),

                new QuantumSingularity(
                        new ResourceLocation(CuboidQuantumSingularitiesMod.MOD_ID, "gold_qs"),
                        false,
                        "gold_qs",
                        new int[] { 0xFEFFBD, 0xF9BD23 },
                        "#forge:ingots/gold",
                        Config.defaultInputCount.get(),
                        Config.defaultRecipeTicks.get(),
                        3),

                new QuantumSingularity(
                        new ResourceLocation(CuboidQuantumSingularitiesMod.MOD_ID, "diamond_qs"),
                        false,
                        "diamond_qs",
                        new int[] { 0xb9f4fa, 0x39a7bd },
                        "#forge:gems/diamond",
                        Config.defaultInputCount.get(),
                        Config.defaultRecipeTicks.get(),
                        4),

                new QuantumSingularity(
                        new ResourceLocation(CuboidQuantumSingularitiesMod.MOD_ID, "emerald_qs"),
                        false,
                        "emerald_qs",
                        new int[] { 0x4bf253, 0x04c70d },
                        "#forge:gems/emerald",
                        Config.defaultInputCount.get(),
                        Config.defaultRecipeTicks.get(),
                        5),

                new QuantumSingularity(
                        new ResourceLocation(CuboidQuantumSingularitiesMod.MOD_ID, "lapis_qs"),
                        false,
                        "lapis_qs",
                        new int[] { 0x4b53f2, 0x040dc7 },
                        "#forge:gems/lapis",
                        Config.defaultInputCount.get(),
                        Config.defaultRecipeTicks.get(),
                        2)
        );
    }
}