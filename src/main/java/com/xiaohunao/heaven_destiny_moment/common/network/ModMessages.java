package com.xiaohunao.heaven_destiny_moment.common.network;

import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.network.s2c.MomentBarSyncPacket;
import com.xiaohunao.heaven_destiny_moment.common.network.s2c.MomentInstanceAddS2CPacket;
import com.xiaohunao.heaven_destiny_moment.common.network.s2c.MomentInstanceSyncS2CPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public final class ModMessages {
    public static final String PROTOCOL_VERSION = "0.0.1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            HeavenDestinyMoment.asResource("main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int packetId = 0;

    public static void register() {
        INSTANCE.registerMessage(packetId++, MomentBarSyncPacket.class, MomentBarSyncPacket::encode, MomentBarSyncPacket::decode, MomentBarSyncPacket::handle);
        INSTANCE.registerMessage(packetId++, MomentInstanceSyncS2CPacket.class, MomentInstanceSyncS2CPacket::encode, MomentInstanceSyncS2CPacket::decode, MomentInstanceSyncS2CPacket::handle);
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player){
        INSTANCE.send(PacketDistributor.PLAYER.with(()-> player), message);
    }

}