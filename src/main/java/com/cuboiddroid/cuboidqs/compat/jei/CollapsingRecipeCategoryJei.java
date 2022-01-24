package com.cuboiddroid.cuboidqs.compat.jei;

import com.cuboiddroid.cuboidqs.modules.collapser.recipe.QuantumCollapsingRecipe;
import com.cuboiddroid.cuboidqs.modules.collapser.screen.QuantumCollapserScreenBase;
import com.cuboiddroid.cuboidqs.setup.ModBlocks;
import com.cuboiddroid.cuboidqs.util.Constants;
import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Arrays;
import java.util.Collections;

public class CollapsingRecipeCategoryJei implements IRecipeCategory<QuantumCollapsingRecipe> {
    private static final int GUI_START_X = 24;
    private static final int GUI_START_Y = 28;
    private static final int GUI_WIDTH = 170 - GUI_START_X;
    private static final int GUI_HEIGHT = 78 - GUI_START_Y;

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;
    private final IDrawableAnimated itemBar;
    private final String localizedName;

    public CollapsingRecipeCategoryJei(IGuiHelper guiHelper) {
        background = guiHelper.createDrawable(QuantumCollapserScreenBase.GUI, GUI_START_X, GUI_START_Y, GUI_WIDTH, GUI_HEIGHT);
        icon = guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.NETHERITE_QUANTUM_COLLAPSER.get()));
        arrow = guiHelper.drawableBuilder(QuantumCollapserScreenBase.GUI, 184, 0, 24, 17)
                .buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
        itemBar = guiHelper.drawableBuilder(QuantumCollapserScreenBase.GUI, 176, 0, 8, 36)
                .buildAnimated(200, IDrawableAnimated.StartDirection.BOTTOM, false);
        localizedName = new TranslationTextComponent("jei.category.cuboidqs.collapsing").getString();
    }

    private static void renderScaledTextWithShadow(MatrixStack matrix, FontRenderer fontRenderer, IReorderingProcessor text, int x, int y, int width, float scale, int color) {
        matrix.pushPose();
        matrix.scale(scale, scale, scale);
        float xOffset = (width / scale - fontRenderer.width(text)) / 2;
        fontRenderer.drawShadow(matrix, text, xOffset + x / scale, y / scale, color);
        matrix.popPose();
    }

    @Override
    public ResourceLocation getUid() {
        return Constants.COLLAPSING;
    }

    @Override
    public Class<? extends QuantumCollapsingRecipe> getRecipeClass() {
        return QuantumCollapsingRecipe.class;
    }

    @Override
    public String getTitle() {
        return localizedName;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setIngredients(QuantumCollapsingRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(Collections.singletonList(recipe.getIngredient()));
        ingredients.setOutputs(VanillaTypes.ITEM, Collections.singletonList(recipe.getResultItem()));
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, QuantumCollapsingRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
        itemStacks.init(0, true, 28, 14);
        itemStacks.init(1, false, 87, 14);

        // Should only be one ingredient...
        itemStacks.set(0, Arrays.asList(recipe.getIngredient().getItems()));
        // Output
        itemStacks.set(1, recipe.getResultItem().copy());
    }

    @Override
    public void draw(QuantumCollapsingRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        FontRenderer font = Minecraft.getInstance().font;

        // arrow
        arrow.draw(matrixStack, 78 - GUI_START_X, 43 - GUI_START_Y);

        int workSeconds = recipe.getWorkTicks() / 20;
        int workDecimal = (recipe.getWorkTicks() % 20) / 2;
        String arrowText = "" + workSeconds + "." + workDecimal + " s";
        renderScaledTextWithShadow(matrixStack, font, new StringTextComponent(arrowText).getVisualOrderText(), 78 - GUI_START_X, 61 - GUI_START_Y, 24, 0.6f, 0xFFFFFF);

        // required amounts
        itemBar.draw(matrixStack, 32 - GUI_START_X, 34 - GUI_START_Y);

        String itemBarText = "" + recipe.getRequiredInputAmount();
        renderScaledTextWithShadow(matrixStack, font, new StringTextComponent(itemBarText).getVisualOrderText(), 32 - GUI_START_X, 71 - GUI_START_Y, 8, 0.6f, 0xFFFFFF);

        String tier;
        switch (recipe.getTier()) {
            case 2:
                tier = ">= Gold";
                break;
            case 3:
                tier = ">= Diamond";
                break;
            case 4:
                tier = ">= Emerald";
                break;
            case 5:
                tier = ">= Netherite";
                break;
            default:
                tier = ">= Iron";
        }

        renderScaledTextWithShadow(matrixStack, font, new StringTextComponent(tier).getVisualOrderText(), 88 - GUI_START_X, 33 - GUI_START_Y, 8, 0.6f, 0xFFFFFF);
    }
}
