package com.xiaohunao.heaven_destiny_moment.common.network;

import com.mojang.serialization.Codec;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;


public record MomentManagerSyncPayload(CompoundTag runMoment) implements CustomPacketPayload {
    public static final Type<MomentManagerSyncPayload> TYPE = new Type<>(HeavenDestinyMoment.asResource("moment_manager_sync"));
    public static final StreamCodec<ByteBuf, MomentManagerSyncPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodec(CompoundTag.CODEC),MomentManagerSyncPayload::runMoment,
            MomentManagerSyncPayload::new
    );


    public static void clientHandle(final MomentManagerSyncPayload payload,final IPayloadContext context) {
        context.enqueueWork(() -> {
            LocalPlayer localPlayer = (LocalPlayer) context.player();
            MomentManager momentManager = MomentManager.of(localPlayer.clientLevel);
            MomentInstance momentInstance = MomentInstance.loadStatic(localPlayer.clientLevel, payload.runMoment);
            momentManager.getRunMoment().put(momentInstance.getID(),momentInstance);
        });
    }


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
