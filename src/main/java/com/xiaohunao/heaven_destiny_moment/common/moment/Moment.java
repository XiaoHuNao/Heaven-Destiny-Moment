package com.xiaohunao.heaven_destiny_moment.common.moment;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.xiaohunao.heaven_destiny_moment.common.codec.CodecMap;
import com.xiaohunao.heaven_destiny_moment.common.context.ClientSettingsContext;
import com.xiaohunao.heaven_destiny_moment.common.context.MomentDataContext;
import com.xiaohunao.heaven_destiny_moment.common.init.MomentRegistries;
import com.xiaohunao.heaven_destiny_moment.common.moment.moment.BloodMoonMoment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;

import java.util.function.Function;


public abstract class Moment implements IMoment {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final CodecMap<Moment> CODEC = new CodecMap<>("moment");

    public static void register() {
        CODEC.register(BloodMoonMoment.ID, BloodMoonMoment.CODEC);
    }


    private ResourceLocation barRenderType;
    private MomentDataContext momentDataContext;
    private ClientSettingsContext clientSettingsContext;


    public Moment(ResourceLocation barRenderType, MomentDataContext momentDataContext, ClientSettingsContext clientSettingsContext) {
        this.barRenderType = barRenderType;
        this.momentDataContext = momentDataContext;
        this.clientSettingsContext = clientSettingsContext;
    }


    public abstract MomentInstance newMomentInstance(Level level,ResourceKey<Moment> momentResourceKey);

    public ResourceLocation getBarRenderType() {
        return barRenderType;
    }

    public MomentDataContext getMomentDataContext() {
        return momentDataContext;
    }

    public ClientSettingsContext getClientSettingsContext() {
        return clientSettingsContext;
    }
}
