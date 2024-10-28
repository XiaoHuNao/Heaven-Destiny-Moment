package com.xiaohunao.heaven_destiny_moment.common.init;

import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.context.ClientSettingsContext;
import com.xiaohunao.heaven_destiny_moment.common.context.MomentDataContext;
import com.xiaohunao.heaven_destiny_moment.common.context.condition.TimeConditionContext;
import com.xiaohunao.heaven_destiny_moment.common.context.reward.XpRewardContext;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentState;
import com.xiaohunao.heaven_destiny_moment.common.moment.moment.BloodMoonMoment;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModMoments {
    public static final ResourceKey<Moment> BLOOD_MOON = HeavenDestinyMoment.asResourceKey(MomentRegistries.Keys.MOMENT,"blood_moon");


    public static void bootstrap(BootstrapContext<Moment> context) {
        context.register(BLOOD_MOON,new BloodMoonMoment(HeavenDestinyMoment.asResource("terra"),
                new MomentDataContext.Builder()
                        .readyTime(100)
                        .addCondition(new TimeConditionContext("FullMoon",3))
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
                        .build()));
    }
}
