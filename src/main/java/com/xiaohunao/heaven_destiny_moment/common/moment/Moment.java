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
    public Optional<MomentData> momentDataContext = Optional.empty();
    public Optional<TipSettings> tipSettingsContext = Optional.empty();
    public Optional<ClientSettings> clientSettingsContext = Optional.empty();

    public Moment() {}

    public Moment(Optional<IBarRenderType> renderType, Optional<Area> area, Optional<MomentData> momentDataContext,
                  Optional<TipSettings> tipSettingsContext, Optional<ClientSettings> clientSettingsContext) {
        this.barRenderType = renderType;
        this.area = area;
        this.momentDataContext = momentDataContext;
        this.tipSettingsContext = tipSettingsContext;
        this.clientSettingsContext = clientSettingsContext;
    }


    public abstract MomentInstance<? extends Moment> newMomentInstance(Level level, ResourceKey<Moment> momentResourceKey);

    public boolean isInArea(ServerLevel level, BlockPos blockPos) {
        return area.map(area1 -> area1.matches(level,blockPos)).orElse(true);
    }

    public Optional<IBarRenderType> barRenderType() {
        return barRenderType;
    }

    public Optional<MomentData> momentDataContext() {
        return momentDataContext;
    }

    public Optional<ClientSettings> clientSettingsContext() {
        return clientSettingsContext;
    }

    public Optional<TipSettings> tipSettingsContext() {
        return tipSettingsContext;
    }

    public Optional<Area> area() {
        return area;
    }

    public Moment setBarRenderType(IBarRenderType barRenderType) {
        this.barRenderType = Optional.of(barRenderType);
        return this;
    }

    public Moment setMomentDataContext(MomentData momentData) {
        this.momentDataContext = Optional.of(momentData);
        return this;
    }

    public Moment setArea(Area area) {
        this.area = Optional.of(area);
        return this;
    }

    public Moment setClientSettingsContext(ClientSettings clientSettings) {
        this.clientSettingsContext = Optional.of(clientSettings);
        return this;
    }

    public Moment setTipSettingsContext(TipSettings tipSettings) {
        this.tipSettingsContext = Optional.of(tipSettings);
        return this;
    }
}
