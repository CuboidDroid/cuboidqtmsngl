package com.cuboiddroid.cuboidqs.modules.collapser.handler;

import com.blakebr0.cucumber.iface.IColored;
import com.cuboiddroid.cuboidqs.setup.ModItems;
//import com.cuboiddroid.cuboidqs.util.IColored;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class ColorHandler {
    @SubscribeEvent
    public void onItemColors(ColorHandlerEvent.Item event) {
        ItemColors itemColors = event.getItemColors();

        itemColors.register(new IColored.ItemColors(), ModItems.QUANTUM_SINGULARITY.get());
    }
}
