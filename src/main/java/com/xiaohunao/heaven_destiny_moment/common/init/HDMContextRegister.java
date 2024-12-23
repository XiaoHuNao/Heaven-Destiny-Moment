package com.xiaohunao.heaven_destiny_moment.common.init;

import com.mojang.serialization.MapCodec;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.context.amount.IAmount;
import com.xiaohunao.heaven_destiny_moment.common.context.amount.IntegerAmount;
import com.xiaohunao.heaven_destiny_moment.common.context.amount.RandomAmount;
import com.xiaohunao.heaven_destiny_moment.common.context.attachable.CommonAttachable;
import com.xiaohunao.heaven_destiny_moment.common.context.attachable.IAttachable;
import com.xiaohunao.heaven_destiny_moment.common.context.condition.*;
import com.xiaohunao.heaven_destiny_moment.common.context.entity_info.EntityInfo;
import com.xiaohunao.heaven_destiny_moment.common.context.entity_info.IEntityInfo;
import com.xiaohunao.heaven_destiny_moment.common.context.entity_info.SlimeInfo;
import com.xiaohunao.heaven_destiny_moment.common.context.equippable_slot.IEquippableSlot;
import com.xiaohunao.heaven_destiny_moment.common.context.equippable_slot.VanillaEquippableSlot;
import com.xiaohunao.heaven_destiny_moment.common.context.reward.*;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import com.xiaohunao.heaven_destiny_moment.common.moment.area.Area;
import com.xiaohunao.heaven_destiny_moment.common.moment.area.LocationArea;
import com.xiaohunao.heaven_destiny_moment.common.moment.moment.DefaultMoment;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class HDMContextRegister {
    public static final DeferredRegister<MapCodec<? extends Area>> AREA_CODEC = DeferredRegister.create(HDMRegistries.Keys.AREA_CODEC, HeavenDestinyMoment.MODID);
    public static final DeferredRegister<MapCodec<? extends Moment>> MOMENT_CODEC = DeferredRegister.create(HDMRegistries.Keys.MOMENT_CODEC, HeavenDestinyMoment.MODID);
    public static final DeferredRegister<MapCodec<? extends IAmount>> AMOUNT_CODEC = DeferredRegister.create(HDMRegistries.Keys.AMOUNT_CODEC, HeavenDestinyMoment.MODID);
    public static final DeferredRegister<MapCodec<? extends ICondition>> CONDITION_CODEC = DeferredRegister.create(HDMRegistries.Keys.CONDITION_CODEC, HeavenDestinyMoment.MODID);
    public static final DeferredRegister<MapCodec<? extends IEntityInfo>> ENTITY_INFO_CODEC = DeferredRegister.create(HDMRegistries.Keys.ENTITY_INFO_CODEC, HeavenDestinyMoment.MODID);
    public static final DeferredRegister<MapCodec<? extends IReward>> REWARD_CODEC = DeferredRegister.create(HDMRegistries.Keys.REWARD_CODEC, HeavenDestinyMoment.MODID);
    public static final DeferredRegister<MapCodec<? extends IEquippableSlot>> EQUIPPABLE_SLOT_CODEC = DeferredRegister.create(HDMRegistries.Keys.EQUIPPABLE_SLOT_CODEC, HeavenDestinyMoment.MODID);
    public static final DeferredRegister<MapCodec<? extends IAttachable>> ATTACHABLE_CODEC = DeferredRegister.create(HDMRegistries.Keys.ATTACHABLE_CODEC, HeavenDestinyMoment.MODID);


    public static final DeferredHolder<MapCodec<? extends Area>, MapCodec<? extends Area>> LOCATION_AREA = AREA_CODEC.register("location", () -> LocationArea.CODEC);

    public static final DeferredHolder<MapCodec<? extends Moment>, MapCodec<? extends Moment>> DEFAULT_MOMENT = MOMENT_CODEC.register("default", () -> DefaultMoment.CODEC);


    public static final DeferredHolder<MapCodec<? extends IAmount>, MapCodec<? extends IAmount>> INTEGER_AMOUNT = AMOUNT_CODEC.register("integer", () -> IntegerAmount.CODEC);
    public static final DeferredHolder<MapCodec<? extends IAmount>, MapCodec<? extends IAmount>> RANDOM_AMOUNT = AMOUNT_CODEC.register("random", () -> RandomAmount.CODEC);

    public static final DeferredHolder<MapCodec<? extends ICondition>, MapCodec<? extends ICondition>> TIME_CONDITION = CONDITION_CODEC.register("time", () -> TimeCondition.CODEC);
    public static final DeferredHolder<MapCodec<? extends ICondition>, MapCodec<? extends ICondition>> LOCATION_CONDITION = CONDITION_CODEC.register("location", () -> LocationCondition.CODEC);
    public static final DeferredHolder<MapCodec<? extends ICondition>, MapCodec<? extends ICondition>> WORLD_UNIQUE_MOMENT = CONDITION_CODEC.register("world_unique_moment", () -> WorldUniqueMomentCondition.CODEC);
    public static final DeferredHolder<MapCodec<? extends ICondition>, MapCodec<? extends ICondition>> AUTO_PROBABILITY = CONDITION_CODEC.register("auto_probability", () -> AutoProbabilityCondition.CODEC);
    public static final DeferredHolder<MapCodec<? extends ICondition>, MapCodec<? extends ICondition>> PLAYER = CONDITION_CODEC.register("player", () -> PlayerCondition.CODEC);

    public static final DeferredHolder<MapCodec<? extends IEntityInfo>, MapCodec<? extends IEntityInfo>> ENTITY_INFO = ENTITY_INFO_CODEC.register("entity_info", () -> EntityInfo.CODEC);
    public static final DeferredHolder<MapCodec<? extends IEntityInfo>, MapCodec<? extends IEntityInfo>> SLIME_INFO = ENTITY_INFO_CODEC.register("slime_info", () -> SlimeInfo.CODEC);


    public static final DeferredHolder<MapCodec<? extends IReward>, MapCodec<? extends IReward>> XP_REWARD = REWARD_CODEC.register("xp", () -> XpReward.CODEC);
    public static final DeferredHolder<MapCodec<? extends IReward>, MapCodec<? extends IReward>> EFFECT_REWARD = REWARD_CODEC.register("effect", () -> EffectReward.CODEC);
    public static final DeferredHolder<MapCodec<? extends IReward>, MapCodec<? extends IReward>> ATTRIBUTE_REWARD = REWARD_CODEC.register("attribute", () -> AttributeReward.CODEC);
    public static final DeferredHolder<MapCodec<? extends IReward>, MapCodec<? extends IReward>> ITEM_REWARD = REWARD_CODEC.register("item", () -> ItemReward.CODEC);


    public static final DeferredHolder<MapCodec<? extends IAttachable>, MapCodec<? extends IAttachable>> COMMON_ATTACHABLE = ATTACHABLE_CODEC.register("common", () -> CommonAttachable.CODEC);


    public static final DeferredHolder<MapCodec<? extends IEquippableSlot>, MapCodec<? extends IEquippableSlot>> VANILLA_EQUIPPABLE_SLOT = EQUIPPABLE_SLOT_CODEC.register("vanilla", () -> VanillaEquippableSlot.CODEC);

    public static void register(IEventBus modEventBus) {
        AMOUNT_CODEC.register(modEventBus);
        CONDITION_CODEC.register(modEventBus);
        ENTITY_INFO_CODEC.register(modEventBus);
        REWARD_CODEC.register(modEventBus);
        AREA_CODEC.register(modEventBus);
        MOMENT_CODEC.register(modEventBus);
    }
}
