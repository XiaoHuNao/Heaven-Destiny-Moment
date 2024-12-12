package com.xiaohunao.heaven_destiny_moment.common.network;

import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;


public record MomentManagerSyncPayload(CompoundTag runMoment,boolean isRemove) implements CustomPacketPayload {
    public static final Type<MomentManagerSyncPayload> TYPE = new Type<>(HeavenDestinyMoment.asResource("moment_manager_sync"));
    public static final StreamCodec<ByteBuf, MomentManagerSyncPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodec(CompoundTag.CODEC), MomentManagerSyncPayload::runMoment,
            ByteBufCodecs.BOOL,MomentManagerSyncPayload::isRemove,
            MomentManagerSyncPayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().isLocalPlayer()) {
                Level level = context.player().level();
                MomentManager momentManager = MomentManager.of(level);
                Optional<MomentInstance<?>> momentInstance = Optional.ofNullable(MomentInstance.loadStatic(level, runMoment));
                momentInstance.ifPresent(instance -> {
                    if (!isRemove) {
                        momentManager.getRunMoments().put(instance.getID(), instance);
                    } else {
                        momentManager.getRunMoments().remove(instance.getID());
                    }
                });
            }
        }).exceptionally(e -> {
            context.disconnect(Component.translatable("neoforge.network.invalid_flow", e.getMessage()));
            return null;
        });
    }
}
