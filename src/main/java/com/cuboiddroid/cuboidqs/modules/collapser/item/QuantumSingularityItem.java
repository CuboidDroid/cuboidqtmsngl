package com.cuboiddroid.cuboidqs.modules.collapser.item;

import com.blakebr0.cucumber.iface.IColored;
import com.cuboiddroid.cuboidqs.CuboidQuantumSingularitiesMod;
import com.cuboiddroid.cuboidqs.modules.collapser.registry.QuantumSingularity;
import com.cuboiddroid.cuboidqs.modules.collapser.registry.QuantumSingularityRegistry;
import com.cuboiddroid.cuboidqs.modules.collapser.registry.QuantumSingularityUtils;
//import com.cuboiddroid.cuboidqs.util.IColored;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class QuantumSingularityItem extends Item implements IColored {
    public QuantumSingularityItem() {
        super(new Properties().tab(CuboidQuantumSingularitiesMod.CUBOIDQS_ITEM_GROUP));
    }

    @Override
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
        if (this.allowdedIn(group)) {
            QuantumSingularityRegistry.getInstance().getSingularities().forEach(singularity -> {
                items.add(QuantumSingularityUtils.getItemForSingularity(singularity));
            });
        }
    }

    @Override
    public ITextComponent getName(ItemStack stack) {
        QuantumSingularity quantumSingularity = QuantumSingularityUtils.getSingularity(stack);
        return quantumSingularity.getDisplayName();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        QuantumSingularity quantumSingularity = QuantumSingularityUtils.getSingularity(stack);
        if (flag.isAdvanced())
            tooltip.add(new StringTextComponent("Quantum Singularity ID: " + quantumSingularity.getId()));
    }

    @Override
    public int getColor(int tintIndex, ItemStack stack) {
        QuantumSingularity quantumSingularity = QuantumSingularityUtils.getSingularity(stack);
        return tintIndex == 0
                ? quantumSingularity.getUnderlayColor()
                : tintIndex == 1
                    ? quantumSingularity.getOverlayColor()
                    : -1;
    }
}