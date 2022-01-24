package com.cuboiddroid.cuboidqs.modules.collapser.registry;

import com.cuboiddroid.cuboidqs.CuboidQuantumSingularitiesMod;
import com.cuboiddroid.cuboidqs.setup.ModItems;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

public class QuantumSingularityUtils {
    public static QuantumSingularity loadFromJson(JsonObject json) {
        boolean enabled = JSONUtils.getAsBoolean(json, "enabled");
        String name = JSONUtils.getAsString(json, "name");
        ResourceLocation id = new ResourceLocation(CuboidQuantumSingularitiesMod.MOD_ID, name);
        JsonArray colors = JSONUtils.getAsJsonArray(json, "colors");

        int overlayColor = Integer.parseInt(colors.get(0).getAsString(), 16);
        int underlayColor = Integer.parseInt(colors.get(1).getAsString(), 16);

        String input = JSONUtils.getAsString(json, "input");
        int inputCount = JSONUtils.getAsInt(json, "inputCount");

        int workTicks = JSONUtils.getAsInt(json, "workTicks");

        int tier = JSONUtils.getAsInt(json, "tier");

        return new QuantumSingularity(id, enabled, name, new int[] { overlayColor, underlayColor }, input, inputCount, workTicks, tier);
    }

    public static JsonObject writeToJson(QuantumSingularity singularity) {
        JsonObject json = new JsonObject();
        json.addProperty("enabled", singularity.getEnabled());
        json.addProperty("name", singularity.getName());
        JsonArray colors = new JsonArray();
        colors.add(Integer.toString(singularity.getOverlayColor(), 16));
        colors.add(Integer.toString(singularity.getUnderlayColor(), 16));
        json.add("colors", colors);

        json.addProperty("input", singularity.getInput());
        json.addProperty("inputCount", singularity.getInputCount());

        json.addProperty("workTicks", singularity.getWorkTicks());
        json.addProperty("tier", singularity.getTier());

        return json;
    }

    public static CompoundNBT makeTag(QuantumSingularity singularity) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("Id", singularity.getId().toString());
        return nbt;
    }

    public static ItemStack getItemForSingularity(QuantumSingularity singularity) {
        CompoundNBT nbt = makeTag(singularity);
        ItemStack stack = new ItemStack(ModItems.QUANTUM_SINGULARITY.get());
        stack.setTag(nbt);
        return stack;
    }

    public static QuantumSingularity getSingularity(ItemStack stack) {
        String id = stack.hasTag() ? stack.getTag().getString("Id") : "";
        if (!id.isEmpty()) {
            return QuantumSingularityRegistry.getInstance().getSingularityById(new ResourceLocation(id));
        }

        return null;
    }
}