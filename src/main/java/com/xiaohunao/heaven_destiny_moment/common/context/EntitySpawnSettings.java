package com.xiaohunao.heaven_destiny_moment.common.context;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.common.context.entity_info.IEntityInfo;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public record EntitySpawnSettings(Optional<List<List<IEntityInfo>>> entitySpawnList, Optional<BiomeEntitySpawnSettings> biomeEntitySpawnSettings, Optional<MobSpawnRule> rule) {
    public static final EntitySpawnSettings EMPTY = new EntitySpawnSettings(Optional.empty(),Optional.empty(),Optional.empty());

    public static final Codec<EntitySpawnSettings> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    IEntityInfo.CODEC.listOf().listOf().optionalFieldOf("entity_spawn_list").forGetter(EntitySpawnSettings::entitySpawnList),
                    BiomeEntitySpawnSettings.CODEC.optionalFieldOf("biome_entity_Spawn_settings").forGetter(EntitySpawnSettings::biomeEntitySpawnSettings),
                    MobSpawnRule.CODEC.optionalFieldOf("spawn_rule").forGetter(EntitySpawnSettings::rule)
            ).apply(builder, EntitySpawnSettings::new)
    );

    public WeightedRandomList<MobSpawnSettings.SpawnerData> adjustmentBiomeEntitySpawnSettings(MobCategory mobCategory, List<MobSpawnSettings.SpawnerData> originalSpawnerData) {
        biomeEntitySpawnSettings.flatMap(BiomeEntitySpawnSettings::biomeMobSpawnSettings)
                .map(mobSpawnSettings -> mobSpawnSettings.spawners.get(mobCategory).unwrap())
                .ifPresent(newSpawnerData -> {
                    boolean allowOriginal = rule.flatMap(MobSpawnRule::allowOriginalBiomeSpawnSettings).orElse(false);
                    if (allowOriginal) {
                        mergeSpawnerData(originalSpawnerData, newSpawnerData);
                    } else {
                        originalSpawnerData.clear();
                        originalSpawnerData.addAll(newSpawnerData);
                    }
                });

        applyBlackOrWhiteListFilter(originalSpawnerData);

        return WeightedRandomList.create(originalSpawnerData);
    }

    private void mergeSpawnerData(List<MobSpawnSettings.SpawnerData> original, List<MobSpawnSettings.SpawnerData> newData) {
        Set<EntityType<?>> originalTypes = original.stream()
                .map(data -> data.type)
                .collect(Collectors.toSet());

        for (MobSpawnSettings.SpawnerData data : newData) {
            EntityType<?> newDataType = data.type;
            if (!originalTypes.contains(newDataType)) {
                original.add(data);
            } else {
                original.removeIf(d -> d.type.equals(newDataType));
                original.add(data);
            }
        }
    }

    private void applyBlackOrWhiteListFilter(List<MobSpawnSettings.SpawnerData> spawnerData) {
        biomeEntitySpawnSettings.flatMap(BiomeEntitySpawnSettings::entitySpawnListContext)
                .ifPresent(list -> {
                    Predicate<MobSpawnSettings.SpawnerData> filterPredicate = data ->
                            list.contains(data.type) && list.isBlackList().isPresent() && list.isBlackList().get();
                    spawnerData.removeIf(filterPredicate);
                });
    }


    public static class Builder {
        private List<List<IEntityInfo>> entitySpawnList;
        private BiomeEntitySpawnSettings biomeEntitySpawnSettings;
        private MobSpawnRule rule;

        public Builder biomeEntitySpawnSettings(Function<BiomeEntitySpawnSettings.Builder,BiomeEntitySpawnSettings.Builder> biomeEntitySpawnSettings) {
            this.biomeEntitySpawnSettings = biomeEntitySpawnSettings.apply(new BiomeEntitySpawnSettings.Builder()).build();
            return this;
        }

        public Builder entitySpawnList(IEntityInfo... entityInfoContexts) {
            if (entitySpawnList.isEmpty()){
                entitySpawnList = Lists.newArrayList();
            }
            Collections.addAll(entitySpawnList, List.of(entityInfoContexts));
            return this;
        }

        public Builder rule(Function<MobSpawnRule.Builder,MobSpawnRule.Builder> rule){
            this.rule = rule.apply(new MobSpawnRule.Builder()).build();
            return this;
        }


        public EntitySpawnSettings build() {
            return new EntitySpawnSettings(Optional.ofNullable(entitySpawnList),Optional.ofNullable(biomeEntitySpawnSettings),Optional.ofNullable(rule));
        }
    }


}
