package com.xiaohunao.heaven_destiny_moment.common.init;

import com.mojang.serialization.MapCodec;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.context.amount.IAmountContext;
import com.xiaohunao.heaven_destiny_moment.common.context.amount.IntegerAmountContext;
import com.xiaohunao.heaven_destiny_moment.common.context.amount.RandomAmountContext;
import com.xiaohunao.heaven_destiny_moment.common.context.attachable.CommonAttachable;
import com.xiaohunao.heaven_destiny_moment.common.context.attachable.IAttachable;
import com.xiaohunao.heaven_destiny_moment.common.context.condition.IConditionContext;
import com.xiaohunao.heaven_destiny_moment.common.context.condition.LocationConditionContext;
import com.xiaohunao.heaven_destiny_moment.common.context.condition.TimeConditionContext;
import com.xiaohunao.heaven_destiny_moment.common.context.entity_info.EntityInfoContext;
import com.xiaohunao.heaven_destiny_moment.common.context.entity_info.IEntityInfoContext;
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

public class ModContextRegister {
    public static final DeferredRegister<MapCodec<? extends Area>> AREA_CODEC = DeferredRegister.create(MomentRegistries.Keys.AREA_CODEC, HeavenDestinyMoment.MODID);
    public static final DeferredRegister<MapCodec<? extends Moment>> MOMENT_CODEC = DeferredRegister.create(MomentRegistries.Keys.MOMENT_CODEC, HeavenDestinyMoment.MODID);
    public static final DeferredRegister<MapCodec<? extends IAmountContext>> AMOUNT_CODEC = DeferredRegister.create(MomentRegistries.Keys.AMOUNT_CODEC, HeavenDestinyMoment.MODID);
    public static final DeferredRegister<MapCodec<? extends IConditionContext>> CONDITION_CODEC = DeferredRegister.create(MomentRegistries.Keys.CONDITION_CODEC, HeavenDestinyMoment.MODID);
    public static final DeferredRegister<MapCodec<? extends IEntityInfoContext>> ENTITY_INFO_CODEC = DeferredRegister.create(MomentRegistries.Keys.ENTITY_INFO_CODEC, HeavenDestinyMoment.MODID);
    public static final DeferredRegister<MapCodec<? extends IRewardContext>> REWARD_CODEC = DeferredRegister.create(MomentRegistries.Keys.REWARD_CODEC, HeavenDestinyMoment.MODID);
    public static final DeferredRegister<MapCodec<? extends IEquippableSlot>> EQUIPPABLE_SLOT_CODEC = DeferredRegister.create(MomentRegistries.Keys.EQUIPPABLE_SLOT_CODEC, HeavenDestinyMoment.MODID);
    public static final DeferredRegister<MapCodec<? extends IAttachable>> ATTACHABLE_CODEC = DeferredRegister.create(MomentRegistries.Keys.ATTACHABLE_CODEC, HeavenDestinyMoment.MODID);


    public static final DeferredHolder<MapCodec<? extends Area>, MapCodec<? extends Area>> LOCATION_AREA = AREA_CODEC.register("location",() -> LocationArea.CODEC);

    public static final DeferredHolder<MapCodec<? extends Moment>, MapCodec<? extends Moment>> DEFAULT_MOMENT = MOMENT_CODEC.register("default", ()-> DefaultMoment.CODEC);


    public static final DeferredHolder<MapCodec<? extends IAmountContext>, MapCodec<? extends IAmountContext>> INTEGER_AMOUNT = AMOUNT_CODEC.register("integer", () -> IntegerAmountContext.CODEC);
    public static final DeferredHolder<MapCodec<? extends IAmountContext>, MapCodec<? extends IAmountContext>> RANDOM_AMOUNT = AMOUNT_CODEC.register("random", () -> RandomAmountContext.CODEC);

    public static final DeferredHolder<MapCodec<? extends IConditionContext>, MapCodec<? extends IConditionContext>> TIME_CONDITION = CONDITION_CODEC.register("time",() -> TimeConditionContext.CODEC);
    public static final DeferredHolder<MapCodec<? extends IConditionContext>, MapCodec<? extends IConditionContext>> LOCATION_CONDITION = CONDITION_CODEC.register("location",() -> LocationConditionContext.CODEC);

    public static final DeferredHolder<MapCodec<? extends IEntityInfoContext>, MapCodec<? extends IEntityInfoContext>> ENTITY_INFO = ENTITY_INFO_CODEC.register("entity_info",() -> EntityInfoContext.CODEC);


    public static final DeferredHolder<MapCodec<? extends IRewardContext>, MapCodec<? extends IRewardContext>> XP_REWARD = REWARD_CODEC.register("xp",() -> XpRewardContext.CODEC);
    public static final DeferredHolder<MapCodec<? extends IRewardContext>, MapCodec<? extends IRewardContext>> EFFECT_REWARD = REWARD_CODEC.register("effect",() -> EffectRewardContext.CODEC);
    public static final DeferredHolder<MapCodec<? extends IRewardContext>, MapCodec<? extends IRewardContext>> ATTRIBUTE_REWARD = REWARD_CODEC.register("attribute",() -> AttributeRewardContext.CODEC);
    public static final DeferredHolder<MapCodec<? extends IRewardContext>, MapCodec<? extends IRewardContext>> ITEM_REWARD = REWARD_CODEC.register("item",() -> ItemRewardContext.CODEC);


    public static final DeferredHolder<MapCodec<? extends IAttachable>, MapCodec<? extends IAttachable>> COMMON_ATTACHABLE = ATTACHABLE_CODEC.register("common",() -> CommonAttachable.CODEC);


    public static final DeferredHolder<MapCodec<? extends IEquippableSlot>, MapCodec<? extends IEquippableSlot>> VANILLA_EQUIPPABLE_SLOT = EQUIPPABLE_SLOT_CODEC.register("vanilla",() -> VanillaEquippableSlot.CODEC);

    public static void register(IEventBus modEventBus) {
        AMOUNT_CODEC.register(modEventBus);
        CONDITION_CODEC.register(modEventBus);
        ENTITY_INFO_CODEC.register(modEventBus);
        REWARD_CODEC.register(modEventBus);
        AREA_CODEC.register(modEventBus);
        MOMENT_CODEC.register(modEventBus);
    }




}
