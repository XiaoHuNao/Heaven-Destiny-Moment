package com.xiaohunao.heaven_destiny_moment.common.context.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.Structure;

public class StructureConditionContext extends ConditionContext {
    public static final ResourceLocation ID = HeavenDestinyMoment.asResource("structure");
    public static final Codec<StructureConditionContext> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("structure").forGetter(StructureConditionContext::getStructure)
    ).apply(instance, StructureConditionContext::new));
    private final ResourceLocation structure;

    public StructureConditionContext(ResourceLocation structure) {
        this.structure = structure;
    }

    public StructureConditionContext(ResourceKey<Structure> resourceKey) {
        this(resourceKey.location());
    }

    public ResourceLocation getStructure() {
        return structure;
    }
    public ResourceKey<Structure> getResourceKey() {
        return ResourceKey.create(Registries.STRUCTURE, structure);
    }
    @Override
    public boolean test(MomentInstance moment, Level level, BlockPos pos, Player player) {
        if (level instanceof ServerLevel serverLevel){
            ResourceKey<Structure> key = this.getResourceKey();
            return serverLevel.structureManager().getAllStructuresAt(pos)
                    .containsKey(level.registryAccess().registryOrThrow(Registries.STRUCTURE).get(key));
        }
        return false;
    }

    @Override
    public Codec<StructureConditionContext> getCodec() {
        return CODEC;
    }

}
