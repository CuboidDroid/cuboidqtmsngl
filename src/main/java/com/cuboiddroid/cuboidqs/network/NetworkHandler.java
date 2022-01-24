package com.cuboiddroid.cuboidqs.network;

import com.cuboiddroid.cuboidqs.CuboidQuantumSingularitiesMod;
import com.cuboiddroid.cuboidqs.network.message.AcknowledgeMessage;
import com.cuboiddroid.cuboidqs.network.message.SyncSingularitiesMessage;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.FMLHandshakeHandler;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class NetworkHandler {
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(CuboidQuantumSingularitiesMod.MOD_ID, "main"), () -> "1.0", (s) -> true, (s) -> true);
    private static int id = 0;

    public static void onCommonSetup() {
        INSTANCE.messageBuilder(SyncSingularitiesMessage.class, id())
                .loginIndex(SyncSingularitiesMessage::getLoginIndex, SyncSingularitiesMessage::setLoginIndex)
                .encoder(SyncSingularitiesMessage::write)
                .decoder(SyncSingularitiesMessage::read)
                .consumer((message, context) -> {
                    BiConsumer<SyncSingularitiesMessage, Supplier<NetworkEvent.Context>> handler;
                    if (context.get().getDirection().getReceptionSide().isServer()) {
                        handler = FMLHandshakeHandler.indexFirst((handshake, msg, ctx) -> SyncSingularitiesMessage.onMessage(msg, ctx));
                    } else {
                        handler = SyncSingularitiesMessage::onMessage;
                    }

                    handler.accept(message, context);
                })
                .markAsLoginPacket()
                .add();

        INSTANCE.messageBuilder(AcknowledgeMessage.class, id())
                .loginIndex(AcknowledgeMessage::getLoginIndex, AcknowledgeMessage::setLoginIndex)
                .encoder(AcknowledgeMessage::write)
                .decoder(AcknowledgeMessage::read)
                .consumer((message, context) -> {
                    BiConsumer<AcknowledgeMessage, Supplier<NetworkEvent.Context>> handler;
                    if (context.get().getDirection().getReceptionSide().isServer()) {
                        handler = FMLHandshakeHandler.indexFirst((handshake, msg, ctx) -> AcknowledgeMessage.onMessage(msg, ctx));
                    } else {
                        handler = AcknowledgeMessage::onMessage;
                    }

                    handler.accept(message, context);
                })
                .markAsLoginPacket()
                .add();
    }

    private static int id() {
        return id++;
    }
}
