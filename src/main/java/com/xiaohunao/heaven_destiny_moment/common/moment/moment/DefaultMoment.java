package com.xiaohunao.heaven_destiny_moment.common.moment.moment;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.util.Function5;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.client.gui.bar.render.IBarRenderType;
import com.xiaohunao.heaven_destiny_moment.common.context.ClientSettingsContext;
import com.xiaohunao.heaven_destiny_moment.common.context.MomentDataContext;
import com.xiaohunao.heaven_destiny_moment.common.context.TipSettingsContext;
import com.xiaohunao.heaven_destiny_moment.common.init.HDMContextRegister;
import com.xiaohunao.heaven_destiny_moment.common.init.HDMRegistries;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import com.xiaohunao.heaven_destiny_moment.common.moment.area.Area;
import com.xiaohunao.heaven_destiny_moment.common.moment.area.LocationArea;
import com.xiaohunao.heaven_destiny_moment.common.moment.moment.instance.DefaultInstance;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class DefaultMoment extends Moment {
    public static final MapCodec<Moment> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            HDMRegistries.BAR_RENDER_TYPE.byNameCodec().optionalFieldOf("bar_render_type").forGetter(Moment::barRenderType),
            Area.CODEC.optionalFieldOf("area").forGetter(Moment::area),
            MomentDataContext.CODEC.optionalFieldOf("moment_data_context").forGetter(Moment::momentDataContext),
            TipSettingsContext.CODEC.optionalFieldOf("tips").forGetter(Moment::tipSettingsContext),
            ClientSettingsContext.CODEC.optionalFieldOf("clientSettingsContext").forGetter(Moment::clientSettingsContext)
    ).apply(instance, (resourceLocation, area, momentDataContext, tipSettingsContext, clientSettingsContext) -> new DefaultMoment()));




    public DefaultMoment() {
    }

    public DefaultMoment(IBarRenderType barRenderType, Area area, MomentDataContext momentDataContext, TipSettingsContext tipSettingsContext, ClientSettingsContext clientSettingsContext) {
        super(barRenderType, area, momentDataContext, tipSettingsContext, clientSettingsContext);
    }

    @Override
    public MomentInstance newMomentInstance(Level level, ResourceKey<Moment> moment) {
        return new DefaultInstance(level, moment);
    }

    @Override
    public MapCodec<? extends Moment> codec() {
        return HDMContextRegister.DEFAULT_MOMENT.get();
    }
}
