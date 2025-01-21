package com.xiaohunao.heaven_destiny_moment.compat.champions;

import com.google.common.collect.Lists;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.common.context.attachable.IAttachable;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.AffixRegistry;
import top.theillusivec4.champions.api.IAffix;
import top.theillusivec4.champions.common.capability.ChampionAttachment;
import top.theillusivec4.champions.common.util.ChampionBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public record ChampionsAffixAttachable(List<ResourceKey<IAffix>> affixes, int tier) implements IAttachable {
    public final static MapCodec<ChampionsAffixAttachable> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceKey.codec(AffixRegistry.AFFIX_REGISTRY_KEY).listOf().fieldOf("affixes").forGetter(ChampionsAffixAttachable::affixes),
            ExtraCodecs.POSITIVE_INT.optionalFieldOf("tier", 1).forGetter(ChampionsAffixAttachable::tier)
    ).apply(instance, ChampionsAffixAttachable::new));


    @Override
    public void attachToEntity(LivingEntity livingEntity) {
        ChampionAttachment.getAttachment(livingEntity).ifPresent(champion -> {
            List<IAffix> affixList = new ArrayList<>();

            for (ResourceKey<IAffix> key : affixes) {
                Champions.API.getAffix(key.location()).ifPresent(affix -> {
                    if (affix.canApply(champion)) {
                        affixList.add(affix);
                    }
                });
            }

            if (!affixList.isEmpty()) {
                ChampionBuilder.spawnPreset(champion, tier, affixList);
            }
        });
    }

    @Override
    public MapCodec<? extends IAttachable> codec() {
        return MomentRegister.CHAMPIONS_ATTACHABLE.get();
    }


    public static class Builder {
        private final List<ResourceKey<IAffix>> affixes = Lists.newArrayList();
        private int tier = 1;


        public Builder tier(int tier) {
            this.tier = Math.max(1, Math.min(5, tier));
            return this;
        }

        public Builder addAffix(ResourceKey<IAffix>... key) {
            if (key != null) {
                affixes.addAll(List.of(key));
            }
            return this;
        }


        public Builder addAffixes(Collection<ResourceKey<IAffix>> keys) {
            if (keys != null) {
                keys.forEach(this::addAffix);
            }
            return this;
        }
        public ChampionsAffixAttachable build() {
            return new ChampionsAffixAttachable(affixes, tier);
        }
    }
}
