package com.xiaohunao.heaven_destiny_moment.common.terra_moment.init;

import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.context.ClientSettingsContext;
import com.xiaohunao.heaven_destiny_moment.common.context.MomentDataContext;
import com.xiaohunao.heaven_destiny_moment.common.context.condition.TimeConditionContext;
import com.xiaohunao.heaven_destiny_moment.common.context.reward.XpRewardContext;
import com.xiaohunao.heaven_destiny_moment.common.init.MomentRegistry;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentState;
import com.xiaohunao.heaven_destiny_moment.common.terra_moment.moment.BloodMoonMoment;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMoment {
    public static final DeferredRegister<Moment> MOMENT = DeferredRegister.create(MomentRegistry.KEY, "terra_moment");

    public static final DeferredHolder<Moment, Moment> BLOOD_MOON = MOMENT.register("blood_moon", () -> new BloodMoonMoment(HeavenDestinyMoment.asResource("terra"),
        new MomentDataContext.Builder()
                .readyTime(100)
                .addCondition(new TimeConditionContext("FullMoon",3))
                .addReward(new XpRewardContext(100))
                .spawnMultiplier(MobCategory.MONSTER, 3.0)
                .allowOriginalBiomeSpawnSettings(true)
                .ignoreLightLevel()
                .build(),
        new ClientSettingsContext.Builder()
                .addTip(MomentState.READY, Component.translatable(HeavenDestinyMoment.asDescriptionId("moment.blood_moon.tip.ready")),0xff0000)
                .addSound(MomentState.READY, SoundEvents.GOAT_HORN_SOUND_VARIANTS.get(2))
                .environmentColor(0xff0000)
                .build()));
}
