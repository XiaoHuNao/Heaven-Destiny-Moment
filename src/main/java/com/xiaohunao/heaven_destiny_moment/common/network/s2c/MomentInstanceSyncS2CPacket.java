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

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public record MomentInstanceSyncS2CPacket(Map<ResourceKey<Moment>, UUID> map,boolean load) {
    public static void encode(MomentInstanceSyncS2CPacket packet, FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeMap(packet.map,
                (friendlyByteBuf1, momentResourceKey) -> friendlyByteBuf1.writeResourceLocation(momentResourceKey.location()),
                FriendlyByteBuf::writeUUID);
        friendlyByteBuf.writeBoolean(packet.load);
    }
    public static MomentInstanceSyncS2CPacket decode(FriendlyByteBuf friendlyByteBuf) {
        return new MomentInstanceSyncS2CPacket(friendlyByteBuf.readMap(friendlyByteBuf1 -> ResourceKey.create(ModMoments.MOMENT_KEY, friendlyByteBuf.readResourceLocation()),
                FriendlyByteBuf::readUUID),friendlyByteBuf.readBoolean());
    }

    public static void handle(MomentInstanceSyncS2CPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> MomentInstanceSyncS2CPacket.Client.handle(packet,ctx)));
    }
    public static class Client{
        public static void handle(MomentInstanceSyncS2CPacket packet, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ClientLevel clientLevel = Minecraft.getInstance().level;
                MomentCap cap = MomentCap.getCap(clientLevel);
                if (packet.load){
                    cap.clear();
                }

                packet.map.forEach(((momentResourceKey, uuid) -> {
                    MomentInstance momentInstance = MomentInstance.create(momentResourceKey, uuid, clientLevel);
                    if (momentInstance != null) {
                        cap.getRunMoment().put(momentInstance.getID(), momentInstance);
                        if (momentInstance.getMoment().coverageType() == LevelCoverage.DEFAULT) {
                            cap.setLevelCoverageMomentMoment(momentInstance);
                        }
                    }
                }));
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
