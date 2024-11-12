package com.xiaohunao.heaven_destiny_moment.common.moment;

import com.mojang.logging.LogUtils;
import com.xiaohunao.heaven_destiny_moment.common.codec.CodecMap;
import com.xiaohunao.heaven_destiny_moment.common.context.ClientSettingsContext;
import com.xiaohunao.heaven_destiny_moment.common.context.MomentDataContext;
import com.xiaohunao.heaven_destiny_moment.common.context.TipSettingsContext;
import com.xiaohunao.heaven_destiny_moment.common.moment.area.Area;
import com.xiaohunao.heaven_destiny_moment.common.moment.moment.DefaultMoment;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;


public abstract class Moment implements IMoment {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final ResourceLocation barRenderType;
    private final Area area;
    private final MomentDataContext momentDataContext;
    private final TipSettingsContext tipSettingsContext;
    private final ClientSettingsContext clientSettingsContext;


    public Moment(ResourceLocation barRenderType, Area area, MomentDataContext momentDataContext, TipSettingsContext tipSettingsContext, ClientSettingsContext clientSettingsContext) {
        this.barRenderType = barRenderType;
        this.area = area;
        this.momentDataContext = momentDataContext;
        this.tipSettingsContext = tipSettingsContext;
        this.clientSettingsContext = clientSettingsContext;
    }

    public abstract MomentInstance newMomentInstance(Level level,ResourceKey<Moment> momentResourceKey);
    public boolean isInArea(ServerLevel level, BlockPos blockPos) {
        return area.matches(level, blockPos);
    }

    public ResourceLocation getBarRenderType() {
        return barRenderType;
    }

    public MomentDataContext getMomentDataContext() {
        return momentDataContext;
    }

    public ClientSettingsContext getClientSettingsContext() {
        return clientSettingsContext;
    }

    public TipSettingsContext getTipSettingsContext() {
        return tipSettingsContext;
    }
    public Area getArea() {
        return area;
    }

}
