package com.xiaohunao.heaven_destiny_moment.common.context;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import java.util.List;

public record MobSpawnListContext(List<EntityType<?>> entityTypes, List<TagKey<EntityType<?>>> tagKeys , Boolean isBlackList) {
    public static final MobSpawnListContext EMPTY = new MobSpawnListContext(Lists.newArrayList(), Lists.newArrayList(),true);

    public static final Codec<MobSpawnListContext> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    BuiltInRegistries.ENTITY_TYPE.byNameCodec().listOf().optionalFieldOf("entity_types", Lists.newArrayList()).forGetter(MobSpawnListContext::entityTypes),
                    TagKey.codec(Registries.ENTITY_TYPE).listOf().optionalFieldOf("tag_keys", Lists.newArrayList()).forGetter(MobSpawnListContext::tagKeys),
                    Codec.BOOL.optionalFieldOf("is_black_list", true).forGetter(MobSpawnListContext::isBlackList)
            ).apply(builder, MobSpawnListContext::new)
    );

    public MobSpawnListContext addEntityType(EntityType<?> entityType) {
        this.entityTypes.add(entityType);
        return this;
    }
    public MobSpawnListContext addTagKey(TagKey<EntityType<?>> tagKey) {
        this.tagKeys.add(tagKey);
        return this;
    }
    public MobSpawnListContext switchListType(){
        return new MobSpawnListContext(entityTypes,tagKeys,!isBlackList);
    }
    public boolean contains(EntityType<?> entityType){
        return entityTypes.contains(entityType) || tagKeys.stream().anyMatch(entityType::is);
    }

}
