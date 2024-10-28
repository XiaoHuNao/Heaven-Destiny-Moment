package com.xiaohunao.heaven_destiny_moment.common.network;

import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.client.gui.bar.MomentBar;
import com.xiaohunao.heaven_destiny_moment.client.gui.hud.MomentBarOverlay;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record MomentBarSyncPayload(MomentBar bar,boolean isRemove) implements CustomPacketPayload {
    public static final Type<MomentBarSyncPayload> TYPE = new Type<>(HeavenDestinyMoment.asResource("moment_bar_sync"));
    public static final StreamCodec<ByteBuf, MomentBarSyncPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodec(MomentBar.CODEC),MomentBarSyncPayload::bar,
            ByteBufCodecs.BOOL,MomentBarSyncPayload::isRemove,
            MomentBarSyncPayload::new
    );

    public static void clientHandle(final MomentBarSyncPayload payload,final IPayloadContext context) {
        if (payload.isRemove()) {
            MomentBarOverlay.barMap.remove(payload.bar().getID());
            return;
        }
        MomentBarOverlay.barMap.put(payload.bar().getID(), payload.bar());
    }

    public static MomentBarSyncPayload update(MomentBar bar) {
        return new MomentBarSyncPayload(bar,false);
    }

    public static MomentBarSyncPayload removePlayer(MomentBar bar) {
        return new MomentBarSyncPayload(bar,true);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
