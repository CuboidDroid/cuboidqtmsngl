package com.cuboiddroid.cuboidqs.modules.collapser.screen;

import com.cuboiddroid.cuboidqs.modules.collapser.inventory.EmeraldQuantumCollapserContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EmeraldQuantumCollapserScreen extends QuantumCollapserScreenBase<EmeraldQuantumCollapserContainer> {

    public EmeraldQuantumCollapserScreen(EmeraldQuantumCollapserContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }
}