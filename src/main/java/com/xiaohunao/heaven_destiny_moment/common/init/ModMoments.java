package com.xiaohunao.heaven_destiny_moment.common.init;

import com.google.common.collect.Maps;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.context.MomentDataContext;
import com.xiaohunao.heaven_destiny_moment.common.context.condition.TimeConditionContext;
import com.xiaohunao.heaven_destiny_moment.common.context.reward.XpRewardContext;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.Map;
import java.util.function.Supplier;

public class ModMoments {
    public static final Map<ResourceKey<Moment>, Moment> MOMENTS = Maps.newHashMap();
    public static final ResourceKey<Registry<Moment>> MOMENT_KEY = ResourceKey.createRegistryKey(HeavenDestinyMoment.asResource("moment"));

    public static final ResourceKey<Moment> BLOOD_MOON = register("blood_moon", new Moment(HeavenDestinyMoment.asResource("terra"),
            new MomentDataContext.Builder()
            .readyTime(100)
            .addCondition(new TimeConditionContext("FullMoon",3))
            .addReward(new XpRewardContext(100))
            .spawnMultiplier(MobCategory.MONSTER, 3.0)
            .addBlackWhiteListEntityType(EntityType.ZOMBIE)
            .switchListType()
            .allowOriginalBiomeSpawnSettings(true)
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
