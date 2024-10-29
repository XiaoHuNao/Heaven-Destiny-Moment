package com.xiaohunao.heaven_destiny_moment.common.moment.moment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.context.ClientSettingsContext;
import com.xiaohunao.heaven_destiny_moment.common.context.MomentDataContext;
import com.xiaohunao.heaven_destiny_moment.common.moment.IMoment;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import com.xiaohunao.heaven_destiny_moment.common.moment.moment.instance.DefaultInstance;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class DefaultMoment extends Moment {
     public static final Codec<Moment> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.optionalFieldOf("bar_render_type", HeavenDestinyMoment.asResource("terra")).forGetter(Moment::getBarRenderType),
            MomentDataContext.CODEC.optionalFieldOf("moment_data_context", MomentDataContext.EMPTY).forGetter(Moment::getMomentDataContext),
            ClientSettingsContext.CODEC.optionalFieldOf("clientSettingsContext", ClientSettingsContext.EMPTY).forGetter(Moment::getClientSettingsContext)
     ).apply(instance, DefaultMoment::new));
    public static final ResourceLocation ID = HeavenDestinyMoment.asResource("blood_moon");

    public DefaultMoment(ResourceLocation barRenderType, MomentDataContext momentDataContext, ClientSettingsContext clientSettingsContext) {
        super(barRenderType, momentDataContext, clientSettingsContext);
    }

    @Override
    public MomentInstance newMomentInstance(Level level, ResourceKey<Moment> moment) {
        return new DefaultInstance(level,moment);
    }

    @Override
    public void tick(MomentInstance momentInstance) {

    }

    @Override
    public Codec<? extends IMoment> getCodec() {
        return CODEC;
    }
}
