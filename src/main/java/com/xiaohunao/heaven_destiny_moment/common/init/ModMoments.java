package com.xiaohunao.heaven_destiny_moment.common.init;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.context.ClientSettingsContext;
import com.xiaohunao.heaven_destiny_moment.common.context.MomentDataContext;
import com.xiaohunao.heaven_destiny_moment.common.context.condition.KillStatisticsConditionContext;
import com.xiaohunao.heaven_destiny_moment.common.context.condition.TimeConditionContext;
import com.xiaohunao.heaven_destiny_moment.common.context.reward.XpRewardContext;
import com.xiaohunao.heaven_destiny_moment.common.moment.DefaultMoment;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentState;
import com.xiaohunao.heaven_destiny_moment.common.moment.coverage.AreaCoverage;
import com.xiaohunao.heaven_destiny_moment.common.moment.coverage.LevelCoverage;
import com.xiaohunao.heaven_destiny_moment.common.moment.type.BloodMoonMoment;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.Map;
import java.util.function.Supplier;

public class ModMoments {
    public static final BiMap<ResourceKey<Moment>, Moment> MOMENTS = HashBiMap.create();
    public static final ResourceKey<Registry<Moment>> MOMENT_KEY = ResourceKey.createRegistryKey(HeavenDestinyMoment.asResource("moment"));

    public static final ResourceKey<Moment> BLOOD_MOON = register("blood_moon", new BloodMoonMoment(HeavenDestinyMoment.asResource("terra"),
            new MomentDataContext.Builder()
            .readyTime(100)
//            .addCondition(new TimeConditionContext("FullMoon",3))
            .addReward(new XpRewardContext(100))
            .spawnMultiplier(MobCategory.MONSTER, 3.0)
            .addBlackWhiteListEntityType(EntityType.ZOMBIE)
            .switchListType()
            .allowOriginalBiomeSpawnSettings(true)
            .ignoreLightLevel()
            .build(),
            new ClientSettingsContext.Builder()
                    .addTip(MomentState.READY, Component.translatable(HeavenDestinyMoment.asDescriptionId("moment.blood_moon.tip.ready")),0xff0000)
                    .addSound(MomentState.READY, SoundEvents.GOAT_HORN_SOUND_VARIANTS.get(2))
                    .environmentColor(0xff0000)
                    .build()
    ));


    public static ResourceKey<Moment> register(String key, Moment moment) {
        ResourceKey<Moment> resourceKey = ResourceKey.create(MOMENT_KEY, HeavenDestinyMoment.asResource(key));
        MOMENTS.put(resourceKey, moment);
        return resourceKey;
    }
    public static ResourceKey<Moment> create(String key) {
        return ResourceKey.create(MOMENT_KEY, HeavenDestinyMoment.asResource(key));
    }

    public static void init() {
    }

    public static void bootstrap(BootstapContext<Moment> context) {
        MOMENTS.forEach(context::register);
    }
}
