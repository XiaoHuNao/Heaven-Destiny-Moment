package com.xiaohunao.heaven_destiny_moment.common.moment;

import com.mojang.logging.LogUtils;
import com.xiaohunao.heaven_destiny_moment.common.codec.CodecMap;
import com.xiaohunao.heaven_destiny_moment.common.context.ClientSettingsContext;
import com.xiaohunao.heaven_destiny_moment.common.context.MomentDataContext;
import com.xiaohunao.heaven_destiny_moment.common.moment.area.Area;
import com.xiaohunao.heaven_destiny_moment.common.moment.moment.DefaultMoment;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;


public abstract class Moment implements IMoment {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final CodecMap<Moment> CODEC = new CodecMap<>("moment");

    public static void register() {
        CODEC.register(DefaultMoment.ID, DefaultMoment.CODEC);
    }


    private ResourceLocation barRenderType;
    private Area<?> area;
    private MomentDataContext momentDataContext;
    private ClientSettingsContext clientSettingsContext;


    public Moment(ResourceLocation barRenderType,Area<?> area, MomentDataContext momentDataContext, ClientSettingsContext clientSettingsContext) {
        this.barRenderType = barRenderType;
        this.momentDataContext = momentDataContext;
        this.clientSettingsContext = clientSettingsContext;
    }


    public abstract MomentInstance newMomentInstance(Level level,ResourceKey<Moment> momentResourceKey);
    public boolean isInArea(ServerLevel level, Player player) {
        return area.contains(level, player);
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
    public Area<?> getCoverage() {
        return area;
    }
}
