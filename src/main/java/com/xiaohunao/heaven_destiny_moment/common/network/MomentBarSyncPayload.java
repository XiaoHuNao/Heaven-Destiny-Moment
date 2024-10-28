package com.xiaohunao.heaven_destiny_moment.common.network;

import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.client.gui.bar.MomentBar;
import com.xiaohunao.heaven_destiny_moment.client.gui.hud.MomentBarOverlay;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record MomentBarSyncPayload(MomentBar bar, boolean isRemove) implements CustomPacketPayload {
    public static final Type<MomentBarSyncPayload> TYPE = new Type<>(HeavenDestinyMoment.asResource("moment_bar_sync"));
    public static final StreamCodec<ByteBuf, MomentBarSyncPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodec(MomentBar.CODEC), MomentBarSyncPayload::bar,
            ByteBufCodecs.BOOL, MomentBarSyncPayload::isRemove,
            MomentBarSyncPayload::new
    );

    public static MomentBarSyncPayload update(MomentBar bar) {
        return new MomentBarSyncPayload(bar, false);
    }

    public static MomentBarSyncPayload removePlayer(MomentBar bar) {
        return new MomentBarSyncPayload(bar, true);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().isLocalPlayer()) {
                if (isRemove) {
                    MomentBarOverlay.barMap.remove(bar.getID());
                    return;
                }
                MomentBarOverlay.barMap.put(bar.getID(), bar);
            }
        }).exceptionally(e -> {
            context.disconnect(Component.translatable("neoforge.network.invalid_flow", e.getMessage()));
            return null;
        });
    }
}
