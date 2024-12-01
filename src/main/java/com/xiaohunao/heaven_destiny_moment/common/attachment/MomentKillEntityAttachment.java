package com.xiaohunao.heaven_destiny_moment.common.attachment;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

import java.util.Map;

public final class MomentKillEntityAttachment {
    public static final Codec<MomentKillEntityAttachment> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(BuiltInRegistries.ENTITY_TYPE.byNameCodec(), Codec.INT).optionalFieldOf("entity_kills", Maps.newHashMap()).forGetter(MomentKillEntityAttachment::getEntityKills),
            Codec.INT.optionalFieldOf("counter", 0).forGetter(MomentKillEntityAttachment::getCounter)
    ).apply(instance, MomentKillEntityAttachment::new));
    private final Map<EntityType<?>, Integer> entity_kills;
    private Integer counter;

    public MomentKillEntityAttachment(Map<EntityType<?>, Integer> entity_kills, Integer counter) {
        this.entity_kills = entity_kills;
        this.counter = counter;
    }

    public MomentKillEntityAttachment addKillCount(LivingEntity livingEntity) {
        counter++;
        entity_kills.merge(livingEntity.getType(), 1, Integer::sum);
        return this;
    }

    public Map<EntityType<?>, Integer> getEntityKills() {
        return entity_kills;
    }
    public int getEntityKills(EntityType<?> entityType) {
        return entity_kills.getOrDefault(entityType,0);
    }

    public Integer getCounter() {
        return counter;
    }
}
