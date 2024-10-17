package com.xiaohunao.heaven_destiny_moment.common.context.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;


public class BiomesConditionContext extends ConditionContext {
    public static final ResourceLocation ID = HeavenDestinyMoment.asResource("biomes");
    public static final Codec<BiomesConditionContext> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("biomes").forGetter(BiomesConditionContext::getBiomes)
    ).apply(instance, BiomesConditionContext::new));
    private final ResourceLocation biomes;

    public BiomesConditionContext(ResourceLocation biomes) {
        this.biomes = biomes;
    }
    public BiomesConditionContext(ResourceKey<Biome> resourceKey) {
        this(resourceKey.location());
    }
    @Override
    public boolean test(MomentInstance moment, Level level, BlockPos pos, Player player) {
        Biome biome = ForgeRegistries.BIOMES.getValue(biomes);
        return level.getBiome(pos).get() == biome;
    }
    public ResourceLocation getBiomes() {
        return biomes;
    }

    @Override
    public Codec<BiomesConditionContext> getCodec() {
        return CODEC;
    }
}
