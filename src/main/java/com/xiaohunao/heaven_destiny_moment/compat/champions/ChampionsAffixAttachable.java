package com.xiaohunao.heaven_destiny_moment.compat.champions;


import com.google.common.collect.Lists;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.common.context.attachable.IAttachable;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Tuple;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import top.theillusivec4.champions.api.*;
import top.theillusivec4.champions.common.capability.ChampionAttachment;
import top.theillusivec4.champions.common.rank.Rank;
import top.theillusivec4.champions.common.util.ChampionBuilder;

import java.util.List;
import java.util.Optional;

public record ChampionsAffixAttachable(List<ResourceKey<IAffix>> affixes) implements IAttachable {
    public final static MapCodec<ChampionsAffixAttachable> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceKey.codec(AffixRegistry.AFFIX_REGISTRY_KEY).listOf().fieldOf("affixes").forGetter(ChampionsAffixAttachable::affixes)
    ).apply(instance, ChampionsAffixAttachable::new));

    @Override
    public void attachToEntity(LivingEntity livingEntity) {
        if (!livingEntity.level().isClientSide()) {
            ChampionAttachment.getAttachment(livingEntity).ifPresent((champion) -> {
                IChampion.Server serverChampion = champion.getServer();
                Optional<Rank> maybeRank = serverChampion.getRank();
                if (maybeRank.isEmpty()) {
                    ChampionBuilder.spawn(champion);
                }

                serverChampion.getAffixes().forEach((affix) -> affix.onSpawn(champion));
                serverChampion.getRank().ifPresent((rank) -> {
                    List<Tuple<Holder<MobEffect>, Integer>> effects = rank.getEffects();
                    effects.forEach((effectPair) -> champion.getLivingEntity().addEffect(new MobEffectInstance(effectPair.getA(), 200, (Integer)effectPair.getB())));
                });
            });
        }
    }

    @Override
    public MapCodec<? extends IAttachable> codec() {
        return MomentRegister.CHAMPIONS_ATTACHABLE.get();
    }

    public static class Builder {
        private List<ResourceKey<IAffix>> affixes = Lists.newArrayList();

        public ChampionsAffixAttachable build(){
            return new ChampionsAffixAttachable(affixes);
        }

        public Builder addAffix(ResourceKey<IAffix> key){
            affixes.add(key);
            return this;
        }
    }
}
