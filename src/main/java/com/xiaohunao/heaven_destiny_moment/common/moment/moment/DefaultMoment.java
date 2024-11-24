package com.xiaohunao.heaven_destiny_moment.common.moment.moment;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.context.ClientSettingsContext;
import com.xiaohunao.heaven_destiny_moment.common.context.MomentDataContext;
import com.xiaohunao.heaven_destiny_moment.common.context.TipSettingsContext;
import com.xiaohunao.heaven_destiny_moment.common.init.ModContextRegister;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import com.xiaohunao.heaven_destiny_moment.common.moment.area.Area;
import com.xiaohunao.heaven_destiny_moment.common.moment.area.LocationArea;
import com.xiaohunao.heaven_destiny_moment.common.moment.moment.instance.DefaultInstance;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class DefaultMoment extends Moment {
    public static final MapCodec<Moment> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.optionalFieldOf("bar_render_type", HeavenDestinyMoment.asResource("terra")).forGetter(Moment::getBarRenderType),
            Area.CODEC.optionalFieldOf("area", LocationArea.EMPTY).forGetter(Moment::getArea),
            MomentDataContext.CODEC.optionalFieldOf("moment_data_context", MomentDataContext.EMPTY).forGetter(Moment::getMomentDataContext),
            TipSettingsContext.CODEC.optionalFieldOf("tips", TipSettingsContext.EMPTY).forGetter(Moment::getTipSettingsContext),
            ClientSettingsContext.CODEC.optionalFieldOf("clientSettingsContext", ClientSettingsContext.EMPTY).forGetter(Moment::getClientSettingsContext)
    ).apply(instance, DefaultMoment::new));

    public DefaultMoment(ResourceLocation barRenderType, Area area, MomentDataContext momentDataContext, TipSettingsContext tipSettingsContext, ClientSettingsContext clientSettingsContext) {
        super(barRenderType, area, momentDataContext, tipSettingsContext, clientSettingsContext);
    }

    @Override
    public MomentInstance newMomentInstance(Level level, ResourceKey<Moment> moment) {
        return new DefaultInstance(level, moment);
    }

    @Override
    public MapCodec<? extends Moment> codec() {
        return ModContextRegister.DEFAULT_MOMENT.get();
    }

    @Override
    public void tick(MomentInstance momentInstance) {

    }
}
