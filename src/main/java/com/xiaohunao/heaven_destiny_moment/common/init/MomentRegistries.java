package com.xiaohunao.heaven_destiny_moment.common.init;

import com.mojang.serialization.MapCodec;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.client.gui.bar.render.IBarRenderType;
import com.xiaohunao.heaven_destiny_moment.common.context.amount.IAmountContext;
import com.xiaohunao.heaven_destiny_moment.common.context.attachable.IAttachable;
import com.xiaohunao.heaven_destiny_moment.common.context.condition.IConditionContext;
import com.xiaohunao.heaven_destiny_moment.common.context.entity_info.IEntityInfoContext;
import com.xiaohunao.heaven_destiny_moment.common.context.equippable_slot.IEquippableSlot;
import com.xiaohunao.heaven_destiny_moment.common.context.reward.IRewardContext;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentType;
import com.xiaohunao.heaven_destiny_moment.common.moment.area.Area;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.Objects;
import java.util.function.Supplier;

public class MomentRegistries {
    public static final Registry<MomentType<?>> MOMENT_TYPE = new RegistryBuilder<>(Keys.MOMENT_TYPE).create();
    public static final Registry<IBarRenderType> BAR_RENDER_TYPE = new RegistryBuilder<>(Keys.BAR_RENDER_TYPE).create();

    public static final Registry<MapCodec<? extends Area>> AREA_CODEC = new RegistryBuilder<>(Keys.AREA_CODEC).create();
    public static final Registry<MapCodec<? extends Moment>> MOMENT_CODEC = new RegistryBuilder<>(Keys.MOMENT_CODEC).create();
    public static final Registry<MapCodec<? extends IAmountContext>> AMOUNT_CODEC = new RegistryBuilder<>(Keys.AMOUNT_CODEC).create();
    public static final Registry<MapCodec<? extends IConditionContext>> CONDITION_CODEC = new RegistryBuilder<>(Keys.CONDITION_CODEC).create();
    public static final Registry<MapCodec<? extends IEntityInfoContext>> ENTITY_INFO_CODEC = new RegistryBuilder<>(Keys.ENTITY_INFO_CODEC).create();
    public static final Registry<MapCodec<? extends IRewardContext>> REWARD_CODEC = new RegistryBuilder<>(Keys.REWARD_CODEC).create();
    public static final Registry<MapCodec<? extends IAttachable>> ATTACHABLE_CODEC = new RegistryBuilder<>(Keys.ATTACHABLE_CODEC).create();
    public static final Registry<MapCodec<? extends IEquippableSlot>> EQUIPPABLE_SLOT_CODEC = new RegistryBuilder<>(Keys.EQUIPPABLE_SLOT_CODEC).create();

    public static final class Keys {
        public static final ResourceKey<Registry<MomentType<?>>> MOMENT_TYPE = HeavenDestinyMoment.asResourceKey("moment_type");
        public static final ResourceKey<Registry<IBarRenderType>> BAR_RENDER_TYPE = HeavenDestinyMoment.asResourceKey("bar_render_type");

        public static final ResourceKey<Registry<MapCodec<? extends Area>>> AREA_CODEC = HeavenDestinyMoment.asResourceKey("area_codec");
        public static final ResourceKey<Registry<MapCodec<? extends Moment>>> MOMENT_CODEC = HeavenDestinyMoment.asResourceKey("moment_codec");
        public static final ResourceKey<Registry<MapCodec<? extends IAmountContext>>> AMOUNT_CODEC = HeavenDestinyMoment.asResourceKey("amount_codec");
        public static final ResourceKey<Registry<MapCodec<? extends IConditionContext>>> CONDITION_CODEC = HeavenDestinyMoment.asResourceKey("condition_codec");
        public static final ResourceKey<Registry<MapCodec<? extends IEntityInfoContext>>> ENTITY_INFO_CODEC = HeavenDestinyMoment.asResourceKey("entity_info_codec");
        public static final ResourceKey<Registry<MapCodec<? extends IRewardContext>>> REWARD_CODEC = HeavenDestinyMoment.asResourceKey("reward_codec");
        public static final ResourceKey<Registry<MapCodec<? extends IAttachable>>> ATTACHABLE_CODEC = HeavenDestinyMoment.asResourceKey("attachable_codec");
        public static final ResourceKey<Registry<MapCodec<? extends IEquippableSlot>>> EQUIPPABLE_SLOT_CODEC = HeavenDestinyMoment.asResourceKey("equippable_slot_codec");


        public static final ResourceKey<Registry<Moment>> MOMENT = HeavenDestinyMoment.asResourceKey("moment");

    }

    public static final class Suppliers {
        public static final Supplier<Registry<MapCodec<? extends Area>>> AREA_CODEC = supplyRegistry(Keys.AREA_CODEC);
        public static final Supplier<Registry<MapCodec<? extends Moment>>> MOMENT_CODEC = supplyRegistry(Keys.MOMENT_CODEC);
        public static final Supplier<Registry<MapCodec<? extends IAmountContext>>> AMOUNT_CODEC = supplyRegistry(Keys.AMOUNT_CODEC);
        public static final Supplier<Registry<MapCodec<? extends IConditionContext>>> CONDITION_CODEC = supplyRegistry(Keys.CONDITION_CODEC);
        public static final Supplier<Registry<MapCodec<? extends IEntityInfoContext>>> ENTITY_INFO_CODEC = supplyRegistry(Keys.ENTITY_INFO_CODEC);
        public static final Supplier<Registry<MapCodec<? extends IRewardContext>>> REWARD_CODEC = supplyRegistry(Keys.REWARD_CODEC);
        public static final Supplier<Registry<MapCodec<? extends IAttachable>>> ATTACHABLE_CODEC = supplyRegistry(Keys.ATTACHABLE_CODEC);
        public static final Supplier<Registry<MapCodec<? extends IEquippableSlot>>> EQUIPPABLE_SLOT_CODEC = supplyRegistry(Keys.EQUIPPABLE_SLOT_CODEC);
    }


    public static final RegistrySetBuilder REGISTRY_SET_BUILDER = new RegistrySetBuilder().add(MomentRegistries.Keys.MOMENT, ModMoments::bootstrap);


    public static void registerRegistries(NewRegistryEvent event) {
        event.register(AREA_CODEC);
        event.register(MOMENT_CODEC);
        event.register(MOMENT_TYPE);
        event.register(BAR_RENDER_TYPE);
        event.register(AMOUNT_CODEC);
        event.register(CONDITION_CODEC);
        event.register(ENTITY_INFO_CODEC);
        event.register(REWARD_CODEC);
        event.register(ATTACHABLE_CODEC);
        event.register(EQUIPPABLE_SLOT_CODEC);
    }


    public static void registerDataPackRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(Keys.MOMENT, Moment.CODEC, Moment.CODEC);
    }

    static <T> Supplier<T> supplyRegistry(ResourceKey<T> key) {
        return com.google.common.base.Suppliers.memoize(() -> Objects.requireNonNull((T) BuiltInRegistries.REGISTRY.get((ResourceKey) key)));
    }
}
