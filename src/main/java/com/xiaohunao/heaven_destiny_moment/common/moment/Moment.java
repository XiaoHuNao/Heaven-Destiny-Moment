package com.xiaohunao.heaven_destiny_moment.common.moment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.context.ClientSettingsContext;
import com.xiaohunao.heaven_destiny_moment.common.context.MomentDataContext;
import com.xiaohunao.heaven_destiny_moment.common.init.MomentRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


public abstract class Moment
        implements IMoment {
//    public static final Codec<Moment> DATA_CODEC = RecordCodecBuilder.create(instance -> instance.group(
//            ResourceLocation.CODEC.optionalFieldOf("bar_render_type", HeavenDestinyMoment.asResource("default")).forGetter(Moment::barRenderType),
//            MomentDataContext.CODEC.optionalFieldOf("moment_data_context", MomentDataContext.EMPTY).forGetter(Moment::momentDataContext),
//            ClientSettingsContext.CODEC.optionalFieldOf("clientSettingsContext", ClientSettingsContext.EMPTY).forGetter(Moment::clientSettingsContext)
//    ).apply(instance, Moment::new));
    private final ResourceLocation barRenderType;
    private final MomentDataContext momentDataContext;
    private final ClientSettingsContext clientSettingsContext;


    public Moment(ResourceLocation barRenderType, MomentDataContext momentDataContext, ClientSettingsContext clientSettingsContext) {
        this.barRenderType = barRenderType;
        this.momentDataContext = momentDataContext;
        this.clientSettingsContext = clientSettingsContext;
    }


    @Override
    public Codec<? extends IMoment> getCodec() {
        return DATA_CODEC;
    }


    public abstract MomentInstance newMomentInstance(Level level);

    public static ResourceLocation getRegistryName(Moment moment) {
        return MomentRegistry.REGISTRY.getKey(moment);
    }

}
