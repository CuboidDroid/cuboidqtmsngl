package com.cuboiddroid.cuboidqs.modules.collapser.registry;

import com.cuboiddroid.cuboidqs.CuboidQuantumSingularitiesMod;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistries;

public class QuantumSingularity {
    private final ResourceLocation id;
    private final Boolean enabled;
    private final String name;
    private final int[] colors;
    private final String input;
    private final int inputCount;
    private Ingredient ingredient;
    private final int workTicks;
    private final int tier;

    public QuantumSingularity(
            ResourceLocation id,
            Boolean enabled,
            String name,
            int[] colors,
            String input,
            int inputCount,
            int workTicks,
            int tier) {
        this.id = id;
        this.enabled = enabled;
        this.name = name;
        this.colors = colors;
        this.input = input;
        this.inputCount = inputCount;
        this.ingredient = Ingredient.EMPTY;
        this.workTicks = workTicks;
        this.tier = tier >= 1 && tier <= 5 ? tier : 1;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public Boolean getEnabled() { return this.enabled; }

    public String getName() {
        return this.name;
    }

    public int getOverlayColor() {
        return this.colors[0];
    }

    public int getUnderlayColor() {
        return this.colors[1];
    }

    public String getInput() { return this.input; }

    public int getInputCount() { return this.inputCount; }

    public int getWorkTicks() { return this.workTicks; }

    public int getTier() { return this.tier; }

    public Ingredient getIngredient() {
        if (this.ingredient != Ingredient.EMPTY)
            return this.ingredient;

        if (this.input.startsWith("#")) {
            ResourceLocation tagLocation = new ResourceLocation(this.input.substring(1));
            ITag<Item> tag = TagCollectionManager.getInstance().getItems().getTag(tagLocation);
            if (tag != null) {
                this.ingredient = Ingredient.of(tag);
            }
        } else {
            ResourceLocation itemId = new ResourceLocation(this.input);
            this.ingredient = Ingredient.of(new ItemStack(ForgeRegistries.ITEMS.getValue(itemId), 1));
        }

        return this.ingredient;
    }

    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("item." + CuboidQuantumSingularitiesMod.MOD_ID + "." + this.name);
    }

    public void write(PacketBuffer buffer) {
        buffer.writeResourceLocation(this.id);
        buffer.writeBoolean(this.enabled);
        buffer.writeUtf(this.name);
        buffer.writeVarIntArray(this.colors);
        buffer.writeUtf(this.input);
        buffer.writeVarInt(this.inputCount);
        buffer.writeVarInt(this.workTicks);
        buffer.writeVarInt(this.tier);
    }

    public static QuantumSingularity read(PacketBuffer buffer) {
        ResourceLocation id = buffer.readResourceLocation();
        Boolean enabled = buffer.readBoolean();
        String name = buffer.readUtf();
        int[] colors = buffer.readVarIntArray();
        String input = buffer.readUtf();
        int inputCount = buffer.readVarInt();
        int workTicks = buffer.readVarInt();
        int tier = buffer.readVarInt();

        return new QuantumSingularity(id, enabled, name, colors, input, inputCount, workTicks, tier);
    }
}
