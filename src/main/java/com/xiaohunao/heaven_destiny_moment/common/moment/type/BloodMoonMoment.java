package com.xiaohunao.heaven_destiny_moment.common.moment.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.context.ClientSettingsContext;
import com.xiaohunao.heaven_destiny_moment.common.context.MomentDataContext;
import com.xiaohunao.heaven_destiny_moment.common.moment.DefaultMoment;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentState;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.function.Function;

public class BloodMoonMoment extends DefaultMoment {
    public static final ResourceLocation ID = HeavenDestinyMoment.asResource("blood_moon");
    public static final Codec<Moment> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.optionalFieldOf("bar_render_type", HeavenDestinyMoment.asResource("default")).forGetter(Moment::barRenderType),
            MomentDataContext.CODEC.fieldOf("moment_data_context").forGetter(Moment::momentDataContext),
            ClientSettingsContext.CODEC.optionalFieldOf("clientSettingsContext",null).forGetter(Moment::clientSettingsContext)
    ).apply(instance, BloodMoonMoment::new));

    public BloodMoonMoment(ResourceLocation barRenderType, MomentDataContext momentDataContext, ClientSettingsContext clientSettingsContext) {
        super(barRenderType, momentDataContext, clientSettingsContext);
    }

    @Override
    public void tick(MomentInstance instance) {
        Level level = instance.getLevel();
        // 计算当前是否已经是di
        long dayTime = level.getDayTime();
        //当前时间是否不在范围（0-13800）内，如果在范围内就设置状态为结束
//        if (dayTime >= 0 && dayTime <= 13800) {
//            // 设置状态为结束
//            instance.setState(MomentState.END);
//        }
    }

    @Override
    public Codec<? extends Moment> getCodec() {
        return CODEC;
    }
}
