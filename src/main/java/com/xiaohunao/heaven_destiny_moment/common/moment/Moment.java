package com.xiaohunao.heaven_destiny_moment.common.moment;

import com.mojang.logging.LogUtils;
import com.xiaohunao.heaven_destiny_moment.client.gui.bar.render.IBarRenderType;
import com.xiaohunao.heaven_destiny_moment.common.context.ClientSettingsContext;
import com.xiaohunao.heaven_destiny_moment.common.context.MomentDataContext;
import com.xiaohunao.heaven_destiny_moment.common.context.TipSettingsContext;
import com.xiaohunao.heaven_destiny_moment.common.moment.area.Area;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;

import java.util.Optional;

public abstract class Moment implements IMoment {
    private IBarRenderType barRenderType;
    private Area area;
    private MomentDataContext momentDataContext;
    private TipSettingsContext tipSettingsContext;
    private ClientSettingsContext clientSettingsContext;

    public Moment() {
    }

    public Moment(IBarRenderType barRenderType, Area area, MomentDataContext momentDataContext, TipSettingsContext tipSettingsContext, ClientSettingsContext clientSettingsContext) {
        this.barRenderType = barRenderType;
        this.area = area;
        this.momentDataContext = momentDataContext;
        this.tipSettingsContext = tipSettingsContext;
        this.clientSettingsContext = clientSettingsContext;
    }

    public abstract MomentInstance newMomentInstance(Level level, ResourceKey<Moment> momentResourceKey);

    public boolean isInArea(ServerLevel level, BlockPos blockPos) {
        if (area != null){
            return area.matches(level,blockPos);
        }
        return true;
    }

    public Optional<IBarRenderType> barRenderType() {
        return Optional.ofNullable(barRenderType);
    }

    public Optional<MomentDataContext> momentDataContext() {
        return Optional.ofNullable(momentDataContext);
    }

    public Optional<ClientSettingsContext> clientSettingsContext() {
        return Optional.ofNullable(clientSettingsContext);
    }

    public Optional<TipSettingsContext> tipSettingsContext() {
        return Optional.ofNullable(tipSettingsContext);
    }

    public Optional<Area> area() {
        return Optional.ofNullable(area);
    }

    public Moment setBarRenderType(IBarRenderType barRenderType) {
        this.barRenderType = barRenderType;
        return this;
    }

    public Moment setMomentDataContext(MomentDataContext momentDataContext) {
        this.momentDataContext = momentDataContext;
        return this;
    }

    public Moment setArea(Area area) {
        this.area = area;
        return this;
    }

    public Moment setClientSettingsContext(ClientSettingsContext clientSettingsContext) {
        this.clientSettingsContext = clientSettingsContext;
        return this;
    }

    public Moment setTipSettingsContext(TipSettingsContext tipSettingsContext) {
        this.tipSettingsContext = tipSettingsContext;
        return this;
    }
}
