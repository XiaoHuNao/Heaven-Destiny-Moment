package com.xiaohunao.heaven_destiny_moment.common.context;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public record EntitySpawnListContext(Optional<List<EntityType<?>>> entityTypes, Optional<List<TagKey<EntityType<?>>>> tagKeys, Optional<Boolean> isBlackList) {
    public static final Codec<EntitySpawnListContext> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    BuiltInRegistries.ENTITY_TYPE.byNameCodec().listOf().optionalFieldOf("entity_types").forGetter(EntitySpawnListContext::entityTypes),
                    TagKey.codec(Registries.ENTITY_TYPE).listOf().optionalFieldOf("tag_keys").forGetter(EntitySpawnListContext::tagKeys),
                    Codec.BOOL.optionalFieldOf("is_black_list").forGetter(EntitySpawnListContext::isBlackList)
            ).apply(builder, EntitySpawnListContext::new)
    );

    public boolean contains(EntityType<?> entityType) {
        if (entityTypes.isPresent()) {
            boolean contains = entityTypes.get().contains(entityType);
            if (tagKeys.isPresent()){
                boolean anyMatch = tagKeys.stream().flatMap(Collection::stream).anyMatch(entityType::is);
                return contains && anyMatch;
            }
        }
        return false;
    }


    public static class Builder {
        private List<EntityType<?>> entityTypes;
        private List<TagKey<EntityType<?>>> tagKeys;
        private Boolean isBlackList;

        public EntitySpawnListContext build() {
            return new EntitySpawnListContext(Optional.ofNullable(entityTypes),Optional.ofNullable(tagKeys),Optional.ofNullable(isBlackList));
        }

        public Builder entityType(EntityType<?>... entityType){
            if (entityTypes == null){
                entityTypes = Lists.newArrayList();
            }
            entityTypes.addAll(List.of(entityType));
            return this;
        }

        public Builder tagKey(TagKey<EntityType<?>> tagKey){
            if (tagKeys == null){
                tagKeys = Lists.newArrayList();
            }
            tagKeys.addAll(List.of(tagKey));
            return this;
        }
        public Builder isBlackList(boolean isBlackList){
            this.isBlackList = isBlackList;
            return this;
        }
    }
}
