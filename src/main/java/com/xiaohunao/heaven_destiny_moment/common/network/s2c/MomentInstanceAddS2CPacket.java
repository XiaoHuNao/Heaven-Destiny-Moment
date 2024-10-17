package com.xiaohunao.heaven_destiny_moment.common.network.s2c;


import com.xiaohunao.heaven_destiny_moment.common.capability.MomentCap;
import com.xiaohunao.heaven_destiny_moment.common.init.ModMoments;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import com.xiaohunao.heaven_destiny_moment.common.moment.coverage.LevelCoverage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public record MomentInstanceAddS2CPacket(ResourceKey<Moment> momentKey, UUID uuid) {
    public static void encode(MomentInstanceAddS2CPacket packet, FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeResourceLocation(packet.momentKey.location());
        friendlyByteBuf.writeUUID(packet.uuid());
    }
    public static MomentInstanceAddS2CPacket decode(FriendlyByteBuf friendlyByteBuf) {
        return new MomentInstanceAddS2CPacket(ResourceKey.create(ModMoments.MOMENT_KEY, friendlyByteBuf.readResourceLocation()), friendlyByteBuf.readUUID());
    }
    public static void handle(MomentInstanceAddS2CPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> Client.handle(packet,ctx)));
    }
    public static class Client{
        public static void handle(MomentInstanceAddS2CPacket packet, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ClientLevel clientLevel = Minecraft.getInstance().level;
                MomentInstance momentInstance = MomentInstance.create(packet.momentKey, packet.uuid(), clientLevel);
                if (momentInstance != null) {
                    MomentCap cap = MomentCap.getCap(clientLevel);
                    cap.getRunMoment().put(momentInstance.getID(), momentInstance);
                    if (momentInstance.getMoment().coverageType() == LevelCoverage.DEFAULT) {
                        cap.setLevelCoverageMomentMoment(momentInstance);
                    }
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
