package com.xiaohunao.heaven_destiny_moment.common.moment.moment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.client.gui.bar.render.IBarRenderType;
import com.xiaohunao.heaven_destiny_moment.common.context.ClientSettings;
import com.xiaohunao.heaven_destiny_moment.common.context.MomentData;
import com.xiaohunao.heaven_destiny_moment.common.context.TipSettings;
import com.xiaohunao.heaven_destiny_moment.common.init.HDMContextRegister;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import com.xiaohunao.heaven_destiny_moment.common.moment.area.Area;
import com.xiaohunao.heaven_destiny_moment.common.moment.moment.instance.RaidInstance;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class RaidMoment extends Moment<RaidMoment> {
    public static final MapCodec<RaidMoment> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            IBarRenderType.CODEC.optionalFieldOf("bar_render_type").forGetter(Moment::barRenderType),
            Area.CODEC.optionalFieldOf("area").forGetter(Moment::area),
            MomentData.CODEC.optionalFieldOf("moment_data_context").forGetter(Moment::momentData),
            TipSettings.CODEC.optionalFieldOf("tips").forGetter(Moment::tipSettings),
            ClientSettings.CODEC.optionalFieldOf("clientSettingsContext").forGetter(Moment::clientSettings),
            Codec.INT.optionalFieldOf("readyTime",100).forGetter(RaidMoment::readyTime)
    ).apply(instance, RaidMoment::new));

    private final int readyTime;

    public RaidMoment() {
        super();
        this.readyTime = 100;
    }

    public RaidMoment(Optional<IBarRenderType> renderType, Optional<Area> area, Optional<MomentData> momentDataContext,
                      Optional<TipSettings> tipSettingsContext, Optional<ClientSettings> clientSettingsContext,
                      int readyTime) {
        super(renderType, area, momentDataContext, tipSettingsContext, clientSettingsContext);
        this.readyTime = readyTime;
    }

    @Override
    public MomentInstance<RaidMoment> newMomentInstance(Level level, ResourceKey<Moment<?>> momentResourceKey) {
        return new RaidInstance(level,momentResourceKey);
    }

    public int readyTime() {
        return readyTime;
    }

    @Override
    public MapCodec<? extends Moment<?>> codec() {
        return HDMContextRegister.RAID_MOMENT.get();
    }
}
