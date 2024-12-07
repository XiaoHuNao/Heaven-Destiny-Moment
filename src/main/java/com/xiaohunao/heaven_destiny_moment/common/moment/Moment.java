package com.xiaohunao.heaven_destiny_moment.common.moment;

import com.xiaohunao.heaven_destiny_moment.client.gui.bar.render.IBarRenderType;
import com.xiaohunao.heaven_destiny_moment.common.context.ClientSettingsContext;
import com.xiaohunao.heaven_destiny_moment.common.context.MomentDataContext;
import com.xiaohunao.heaven_destiny_moment.common.context.TipSettingsContext;
import com.xiaohunao.heaven_destiny_moment.common.moment.area.Area;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import java.util.Optional;

public abstract class Moment implements IMoment {
    public Optional<IBarRenderType> barRenderType;
    public Optional<Area> area;
    public Optional<MomentDataContext> momentDataContext;
    public Optional<TipSettingsContext> tipSettingsContext;
    public Optional<ClientSettingsContext> clientSettingsContext;

    public Moment() {
        this.barRenderType = Optional.empty();
        this.area = Optional.empty();
        this.momentDataContext = Optional.empty();
        this.tipSettingsContext = Optional.empty();
        this.clientSettingsContext = Optional.empty();
    }

    public Moment(Optional<IBarRenderType> renderType, Optional<Area> area, Optional<MomentDataContext> momentDataContext, Optional<TipSettingsContext> tipSettingsContext, Optional<ClientSettingsContext> clientSettingsContext) {
        this.barRenderType = renderType;
        this.area = area;
        this.momentDataContext = momentDataContext;
        this.tipSettingsContext = tipSettingsContext;
        this.clientSettingsContext = clientSettingsContext;
    }


    public abstract MomentInstance newMomentInstance(Level level, ResourceKey<Moment> momentResourceKey);

    public boolean isInArea(ServerLevel level, BlockPos blockPos) {
        return area.map(area1 -> area1.matches(level,blockPos)).orElse(true);
    }

    public Optional<IBarRenderType> barRenderType() {
        return barRenderType;
    }

    public Optional<MomentDataContext> momentDataContext() {
        return momentDataContext;
    }

    public Optional<ClientSettingsContext> clientSettingsContext() {
        return clientSettingsContext;
    }

    public Optional<TipSettingsContext> tipSettingsContext() {
        return tipSettingsContext;
    }

    public Optional<Area> area() {
        return area;
    }

    public Moment setBarRenderType(IBarRenderType barRenderType) {
        this.barRenderType = Optional.of(barRenderType);
        return this;
    }

    public Moment setMomentDataContext(MomentDataContext momentDataContext) {
        this.momentDataContext = Optional.of(momentDataContext);
        return this;
    }

    public Moment setArea(Area area) {
        this.area = Optional.of(area);
        return this;
    }

    public Moment setClientSettingsContext(ClientSettingsContext clientSettingsContext) {
        this.clientSettingsContext = Optional.of(clientSettingsContext);
        return this;
    }

    public Moment setTipSettingsContext(TipSettingsContext tipSettingsContext) {
        this.tipSettingsContext = Optional.of(tipSettingsContext);
        return this;
    }
}
