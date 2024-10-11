package com.xiaohunao.heaven_destiny_moment.common.network.s2c;

import com.xiaohunao.heaven_destiny_moment.client.gui.bar.MomentBar;
import com.xiaohunao.heaven_destiny_moment.client.gui.hud.MomentBarOverlay;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record MomentBarSyncPacket(MomentBar bar,Handler handler){
    public static void encode(MomentBarSyncPacket packet, FriendlyByteBuf friendlyByteBuf) {
        packet.bar().write(friendlyByteBuf);
        friendlyByteBuf.writeUtf(packet.handler.type());
    }
    public static MomentBarSyncPacket decode(FriendlyByteBuf friendlyByteBuf) {
        return new MomentBarSyncPacket(MomentBar.read(friendlyByteBuf),Handler.valueOf(friendlyByteBuf.readUtf()));
    }
    public static void handle(MomentBarSyncPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->Client.handle(packet,ctx)));
    }

    public static MomentBarSyncPacket addPlayer(MomentBar bar) {
        return new MomentBarSyncPacket(bar,Handler.valueOf("add"));
    }

    public static MomentBarSyncPacket removePlayer(MomentBar bar) {
        return new MomentBarSyncPacket(bar,Handler.valueOf("remove"));
    }

    public static MomentBarSyncPacket update(MomentBar bar) {
        return new MomentBarSyncPacket(bar,Handler.valueOf("update"));
    }

    public static class Client{
        public static void handle(MomentBarSyncPacket packet, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                packet.handler.handle(packet);
            });
            ctx.get().setPacketHandled(true);
        }
    }

    public abstract static class Handler {
        public final String type;
        public Handler(String type) {
            this.type = type;
        }

        static Handler valueOf(String type) {
            return switch (type) {
                case "add" -> new Handler("add") {
                    @Override
                    public void handle(MomentBarSyncPacket packet) {
                        MomentBarOverlay.barMap.put(packet.bar().getId(), packet.bar());
                    }
                };
                case "remove" -> new Handler("remove") {
                    @Override
                    public void handle(MomentBarSyncPacket packet) {
                        MomentBarOverlay.barMap.remove(packet.bar().getId());
                    }
                };
                case "update" -> new Handler("update") {
                    @Override
                    public void handle(MomentBarSyncPacket packet) {
                        MomentBarOverlay.barMap.put(packet.bar().getId(), packet.bar());
                    }
                };
                default -> throw new IllegalArgumentException("Invalid type: " + type);
            };
        }
        abstract void handle(MomentBarSyncPacket packet);

        public String type() {
            return type;
        }
    }



}
