package com.cuboiddroid.cuboidqs.setup;

import com.cuboiddroid.cuboidqs.modules.collapser.item.QuantumSingularityItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

public class ModItems {
    // Quantum Singularities
    public static final RegistryObject<Item> QUANTUM_SINGULARITY = Registration.ITEMS.register(
            "quantum_singularity", QuantumSingularityItem::new);

    // this register() is only used to load the class so that the deferred register stuff works
    static void register() {}
}
