package com.example.entitytalker.network;

import com.example.entitytalker.component.IEntityData;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class ModMessages {
    public static final Identifier TALK_PACKET_ID = new Identifier("entity-talker", "talk");
    public static final Identifier GIFT_REQUEST_PACKET_ID = new Identifier("entity-talker", "gift_request");
    public static final Identifier FRIENDSHIP_UPDATE_PACKET_ID = new Identifier("entity-talker", "friendship_update");

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(TALK_PACKET_ID, (server, player, handler, buf, responseSender) -> {
            int entityId = buf.readInt();
            String dialogOption = buf.readString();
            
            server.execute(() -> {
                var entity = player.getWorld().getEntityById(entityId);
                if (entity != null && entity instanceof net.minecraft.entity.LivingEntity livingEntity) {
                    IEntityData data = (IEntityData) livingEntity;
                    
                    if ("greet".equals(dialogOption)) {
                        data.setFriendshipLevel(data.getFriendshipLevel() + 5);
                    } else if ("compliment".equals(dialogOption)) {
                        data.setFriendshipLevel(data.getFriendshipLevel() + 10);
                    } else if ("trade".equals(dialogOption)) {
                        // Handle trade request
                    }
                    
                    // Send update back to client
                    PacketByteBuf responseBuf = new PacketByteBuf(io.netty.buffer.Unpooled.buffer());
                    responseBuf.writeInt(entityId);
                    responseBuf.writeInt(data.getFriendshipLevel());
                    responseBuf.writeBoolean(data.isFriendly());
                    ServerPlayNetworking.send(player, FRIENDSHIP_UPDATE_PACKET_ID, responseBuf);
                }
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(GIFT_REQUEST_PACKET_ID, (server, player, handler, buf, responseSender) -> {
            int entityId = buf.readInt();
            
            server.execute(() -> {
                var entity = player.getWorld().getEntityById(entityId);
                if (entity != null && entity instanceof net.minecraft.entity.LivingEntity livingEntity) {
                    IEntityData data = (IEntityData) livingEntity;
                    
                    if (data.isFriendly() && data.getFriendshipLevel() >= 75) {
                        // Give gift logic here
                        server.sendMessage(net.minecraft.text.Text.literal("Entity gave a gift!"));
                    }
                }
            });
        });
    }
}
