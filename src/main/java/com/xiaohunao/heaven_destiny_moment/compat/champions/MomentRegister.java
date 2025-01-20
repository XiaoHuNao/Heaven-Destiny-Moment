package com.xiaohunao.heaven_destiny_moment.compat.champions;

import com.mojang.serialization.MapCodec;
import com.xiaohunao.heaven_destiny_moment.common.context.attachable.IAttachable;
import com.xiaohunao.heaven_destiny_moment.common.init.HDMRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.Champions;

public class MomentRegister {
    public static final DeferredRegister<MapCodec<? extends IAttachable>> ATTACHABLE_CODEC = DeferredRegister.create(HDMRegistries.Keys.ATTACHABLE_CODEC, Champions.MODID);
    public static final DeferredHolder<MapCodec<? extends IAttachable>, MapCodec<? extends IAttachable>> CHAMPIONS_ATTACHABLE = ATTACHABLE_CODEC.register("champions", () -> ChampionsAffixAttachable.CODEC);
}
