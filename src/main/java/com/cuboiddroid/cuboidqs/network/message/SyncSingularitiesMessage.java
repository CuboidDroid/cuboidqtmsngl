package com.cuboiddroid.cuboidqs.network.message;

import com.cuboiddroid.cuboidqs.modules.collapser.registry.QuantumSingularity;
import com.cuboiddroid.cuboidqs.modules.collapser.registry.QuantumSingularityRegistry;
import com.cuboiddroid.cuboidqs.network.NetworkHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class SyncSingularitiesMessage implements IntSupplier {
    private List<QuantumSingularity> singularities = new ArrayList<>();
    private int loginIndex;

    public SyncSingularitiesMessage() { }

    @Override
    public int getAsInt() {
        return this.loginIndex;
    }

    public int getLoginIndex() {
        return this.loginIndex;
    }

    public void setLoginIndex(int loginIndex) {
        this.loginIndex = loginIndex;
    }

    public List<QuantumSingularity> getSingularities() {
        return this.singularities;
    }

    public static SyncSingularitiesMessage read(PacketBuffer buffer) {
        SyncSingularitiesMessage message = new SyncSingularitiesMessage();

        message.singularities = QuantumSingularityRegistry.getInstance().readFromBuffer(buffer);

        return message;
    }

    public static void write(SyncSingularitiesMessage message, PacketBuffer buffer) {
        QuantumSingularityRegistry.getInstance().writeToBuffer(buffer);
    }

    public static void onMessage(SyncSingularitiesMessage message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            QuantumSingularityRegistry.getInstance().loadSingularities(message);

            NetworkHandler.INSTANCE.reply(new AcknowledgeMessage(), context.get());
        });

        context.get().setPacketHandled(true);
    }
}