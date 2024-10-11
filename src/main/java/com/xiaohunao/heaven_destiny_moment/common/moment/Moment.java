package com.xiaohunao.heaven_destiny_moment.common.moment;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.context.MomentDataContext;
import net.minecraft.resources.ResourceLocation;

public record Moment(ResourceLocation barRenderType, MomentDataContext momentDataContext) {
    public static final Codec<Moment> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.optionalFieldOf("bar_render_type", HeavenDestinyMoment.asResource("default")).forGetter(Moment::barRenderType),
            MomentDataContext.CODEC.fieldOf("moment_data_context").forGetter(Moment::momentDataContext)
    ).apply(instance, Moment::new));

}
