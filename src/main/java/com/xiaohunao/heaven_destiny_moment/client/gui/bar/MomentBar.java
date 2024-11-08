package com.xiaohunao.heaven_destiny_moment.client.gui.bar;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.common.init.MomentRegistries;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import com.xiaohunao.heaven_destiny_moment.common.network.MomentBarSyncPayload;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

public class MomentBar {
    public static final Codec<MomentBar> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(UUIDUtil.CODEC.fieldOf("uuid").forGetter(MomentBar::getID),
                    ResourceLocation.CODEC.fieldOf("type").forGetter(MomentBar::getType),
                    Codec.FLOAT.fieldOf("progress").forGetter(MomentBar::getProgress),
                    Codec.STRING.xmap(BossEvent.BossBarColor::valueOf, BossEvent.BossBarColor::name).fieldOf("color").forGetter(MomentBar::getColor))
            .apply(inst, MomentBar::new)
    );


    private final UUID uuid;
    private final ResourceLocation type;
    private final Set<ServerPlayer> players = Sets.newHashSet();
    private float progress = 1.0F;
    private BossEvent.BossBarColor color = BossEvent.BossBarColor.YELLOW;


    public MomentBar(UUID uuid, ResourceLocation type) {
        this.uuid = uuid;
        this.type = type;
    }

    public MomentBar(UUID uuid, ResourceLocation type, float progress, BossEvent.BossBarColor color) {
        this.uuid = uuid;
        this.type = type;
        this.progress = progress;
        this.color = color;
    }

    public void updateProgress(float progress) {
        if (progress != this.progress) {
            this.progress = progress;
            broadcast(MomentBarSyncPayload::update);
        }
    }
    public void addPlayer(ServerPlayer serverPlayer) {
        if (this.players.add(serverPlayer)) {
            broadcast(MomentBarSyncPayload::update);
        }
    }
    public void removePlayer(ServerPlayer serverPlayer) {
        if (this.players.remove(serverPlayer)) {
            broadcast(MomentBarSyncPayload::removePlayer);
        }
    }

    public UUID getID() {
        return uuid;
    }

    public ResourceLocation getType() {
        return type;
    }

    public Set<ServerPlayer> getPlayers() {
        return players;
    }

    public float getProgress() {
        return progress;
    }

    public BossEvent.BossBarColor getColor() {
        return color;
    }

    public void broadcast(Function<MomentBar, MomentBarSyncPayload> function) {
        if (!this.players.isEmpty()) {
            MomentBarSyncPayload packet = function.apply(this);
            PacketDistributor.sendToAllPlayers(packet);
        }
    }
}
