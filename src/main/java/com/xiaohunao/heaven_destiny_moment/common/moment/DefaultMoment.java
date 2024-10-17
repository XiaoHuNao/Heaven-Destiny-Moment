package com.xiaohunao.heaven_destiny_moment.common.moment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.context.ClientSettingsContext;
import com.xiaohunao.heaven_destiny_moment.common.context.MomentDataContext;
import com.xiaohunao.heaven_destiny_moment.common.context.condition.ConditionContext;
import com.xiaohunao.heaven_destiny_moment.common.moment.coverage.AreaCoverage;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.compress.utils.Sets;

import java.util.List;

public class DefaultMoment extends Moment {
    public static final ResourceLocation ID = HeavenDestinyMoment.asResource("default");
    public static final Codec<Moment> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ResourceLocation.CODEC.optionalFieldOf("bar_render_type", HeavenDestinyMoment.asResource("default")).forGetter(Moment::barRenderType),
        MomentDataContext.CODEC.fieldOf("moment_data_context").forGetter(Moment::momentDataContext),
            ClientSettingsContext.CODEC.optionalFieldOf("clientSettingsContext",null).forGetter(Moment::clientSettingsContext)
    ).apply(instance, DefaultMoment::new));

    public List<BlockPos> spawnPos = Lists.newArrayList();


    public DefaultMoment(ResourceLocation barRenderType, MomentDataContext momentDataContext,ClientSettingsContext clientSettingsContext) {
        super(barRenderType, momentDataContext,clientSettingsContext);
    }

    @Override
    public Codec<? extends Moment> getCodec() {
        return CODEC;
    }
}
