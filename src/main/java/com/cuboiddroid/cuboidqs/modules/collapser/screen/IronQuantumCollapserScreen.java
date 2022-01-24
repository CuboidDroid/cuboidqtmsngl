package com.cuboiddroid.cuboidqs.modules.collapser.screen;

import com.cuboiddroid.cuboidqs.modules.collapser.inventory.IronQuantumCollapserContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class IronQuantumCollapserScreen extends QuantumCollapserScreenBase<IronQuantumCollapserContainer> {

    public IronQuantumCollapserScreen(IronQuantumCollapserContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }
}