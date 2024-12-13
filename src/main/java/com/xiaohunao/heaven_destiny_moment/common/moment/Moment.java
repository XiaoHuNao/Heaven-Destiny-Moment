package com.xiaohunao.heaven_destiny_moment.common.moment;

import com.xiaohunao.heaven_destiny_moment.client.gui.bar.render.IBarRenderType;
import com.xiaohunao.heaven_destiny_moment.common.context.ClientSettings;
import com.xiaohunao.heaven_destiny_moment.common.context.MomentData;
import com.xiaohunao.heaven_destiny_moment.common.context.TipSettings;
import com.xiaohunao.heaven_destiny_moment.common.moment.area.Area;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import java.util.Optional;

public abstract class Moment implements IMoment {
    public Optional<IBarRenderType> barRenderType = Optional.empty();
    public Optional<Area> area = Optional.empty();
    public Optional<MomentData> momentData = Optional.empty();
    public Optional<TipSettings> tipSettings = Optional.empty();
    public Optional<ClientSettings> clientSettings = Optional.empty();

    public Moment() {}

    public Moment(Optional<IBarRenderType> renderType, Optional<Area> area, Optional<MomentData> momentData,
                  Optional<TipSettings> tipSettings, Optional<ClientSettings> clientSettings) {
        this.barRenderType = renderType;
        this.area = area;
        this.momentData = momentData;
        this.tipSettings = tipSettings;
        this.clientSettings = clientSettings;
    }


    public abstract MomentInstance<? extends Moment> newMomentInstance(Level level, ResourceKey<Moment> momentResourceKey);

    public boolean isInArea(ServerLevel level, BlockPos blockPos) {
        return area.map(area1 -> area1.matches(level,blockPos)).orElse(true);
    }

    public Optional<IBarRenderType> barRenderType() {
        return barRenderType;
    }

    public Optional<MomentData> momentData() {
        return momentData;
    }

    public Optional<ClientSettings> clientSettings() {
        return clientSettings;
    }

    public Optional<TipSettings> tipSettings() {
        return tipSettings;
    }

    public Optional<Area> area() {
        return area;
    }

    public Moment setBarRenderType(IBarRenderType barRenderType) {
        this.barRenderType = Optional.of(barRenderType);
        return this;
    }

    public Moment setMomentData(MomentData momentData) {
        this.momentData = Optional.of(momentData);
        return this;
    }

    public Moment setArea(Area area) {
        this.area = Optional.of(area);
        return this;
    }

    public Moment setClientSettings(ClientSettings clientSettings) {
        this.clientSettings = Optional.of(clientSettings);
        return this;
    }

    public Moment setTipSettings(TipSettings tipSettings) {
        this.tipSettings = Optional.of(tipSettings);
        return this;
    }

    public boolean isClientOnlyMoment(){
        return clientSettings.map(ClientSettings::isPresent).orElse(false);
    }
}
