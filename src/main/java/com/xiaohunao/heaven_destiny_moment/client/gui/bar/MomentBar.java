package com.xiaohunao.heaven_destiny_moment.client.gui.bar;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.client.gui.bar.render.IBarRenderType;
import com.xiaohunao.heaven_destiny_moment.common.init.ModMoments;
import com.xiaohunao.heaven_destiny_moment.common.init.ModRenderBarTypes;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import com.xiaohunao.heaven_destiny_moment.common.network.ModMessages;
import com.xiaohunao.heaven_destiny_moment.common.network.s2c.MomentBarSyncPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;


import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

public class MomentBar {
    private ResourceLocation barRenderType;
    private ResourceKey<Moment> momentKey;
    private UUID id;
    private Component name;
    private float progress = 1.0F;
    private boolean visible = true;
    private BossEvent.BossBarColor color = BossEvent.BossBarColor.YELLOW;
    private Set<ServerPlayer> players = Sets.newHashSet();



    public MomentBar(UUID id, ResourceKey<Moment> momentKey,ResourceLocation barRenderType) {
        this.id = id;
        this.momentKey = momentKey;
        this.barRenderType = barRenderType;
        this.name = Component.translatable(HeavenDestinyMoment.asDescriptionId("gui.bar." + momentKey.location().toLanguageKey()));
    }

    public MomentBar(UUID id,ResourceKey<Moment> momentKey,ResourceLocation barRenderType, Component name, float progress, boolean visible, BossEvent.BossBarColor color, Set<ServerPlayer> players) {
        this.id = id;
        this.momentKey = momentKey;
        this.name = name;
        this.progress = progress;
        this.visible = visible;
        this.color = color;
        this.players = players;
    }

    public UUID getId() {
        return id;
    }

    public Component getName() {
        return name;
    }

    public ResourceKey<Moment> getMomentKey() {
        return momentKey;
    }

    public float getProgress() {
        return progress;
    }

    public ResourceLocation getBarRenderType() {
        return barRenderType;
    }
    public IBarRenderType getRenderType() {
        return ModRenderBarTypes.REGISTRY_SUPPLIER.get().getValue(barRenderType);
    }

    public Set<ServerPlayer> getPlayers() {
        return players;
    }


    public boolean isVisible() {
        return visible;
    }

    public BossEvent.BossBarColor getColor() {
        return color;
    }

    public void updateProgress(float progress) {
        if (progress != this.progress) {
            this.progress = progress;
            broadcast(MomentBarSyncPacket::update);
        }
    }
    public void setColor(BossEvent.BossBarColor color) {
        if (color != this.color) {
            this.color = color;
            broadcast(MomentBarSyncPacket::update);
        }
    }
    public void setName(Component component) {
        if (!Objects.equal(component, this.name)) {
            this.name = component;
            broadcast(MomentBarSyncPacket::update);

        }
    }
    public void setVisible(boolean visible) {
        if (visible != this.visible) {
            this.visible = visible;
            broadcast(MomentBarSyncPacket::update);
        }
    }
    public void addPlayer(ServerPlayer serverPlayer) {
        if (this.players.add(serverPlayer)) {
            broadcast(MomentBarSyncPacket::addPlayer);
        }
    }

    public void removePlayer(ServerPlayer serverPlayer) {
        if (this.players.remove(serverPlayer)) {
            broadcast(MomentBarSyncPacket::removePlayer);
        }
    }

    public void removeAllPlayers() {
        if (!this.players.isEmpty()) {
            for(ServerPlayer serverplayer : Lists.newArrayList(this.players)) {
                this.removePlayer(serverplayer);
            }
        }
    }
    public void setID(UUID uuid) {
        this.id = uuid;
    }

    public void broadcast(Function<MomentBar, MomentBarSyncPacket> function) {
        if (!this.players.isEmpty()) {
            MomentBarSyncPacket packet = function.apply(this);
            players.forEach(player -> ModMessages.sendToPlayer(packet, player));
        }

    }

    public void write(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeUUID(this.id);
        friendlyByteBuf.writeResourceLocation(this.momentKey.location());
        friendlyByteBuf.writeResourceLocation(this.barRenderType);
        friendlyByteBuf.writeComponent(this.name);
        friendlyByteBuf.writeFloat(this.progress);
        friendlyByteBuf.writeBoolean(this.visible);
        friendlyByteBuf.writeEnum(this.color);
    }
    public static MomentBar read(FriendlyByteBuf friendlyByteBuf) {
        UUID id = friendlyByteBuf.readUUID();
        ResourceKey<Moment> momentKey = ResourceKey.create(ModMoments.MOMENT_KEY, friendlyByteBuf.readResourceLocation());
        ResourceLocation barRenderType = friendlyByteBuf.readResourceLocation();
        Component name = friendlyByteBuf.readComponent();
        float progress = friendlyByteBuf.readFloat();
        boolean visible = friendlyByteBuf.readBoolean();
        BossEvent.BossBarColor color = friendlyByteBuf.readEnum(BossEvent.BossBarColor.class);
        return new MomentBar(id, momentKey,barRenderType,name, progress, visible, color, Sets.newHashSet());
    }
}
