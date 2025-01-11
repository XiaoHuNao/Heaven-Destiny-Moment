package com.xiaohunao.heaven_destiny_moment.common.context;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

import java.util.function.IntFunction;

public record SpawnCategoryMultiplierModifier(ResourceLocation id, double amount, Operation operation) {
    public static final MapCodec<SpawnCategoryMultiplierModifier> MAP_CODEC = RecordCodecBuilder.mapCodec((p_349989_) ->
            p_349989_.group(
                    ResourceLocation.CODEC.fieldOf("id").forGetter(SpawnCategoryMultiplierModifier::id),
                    Codec.DOUBLE.fieldOf("amount").forGetter(SpawnCategoryMultiplierModifier::amount),
                    Operation.CODEC.fieldOf("operation").forGetter(SpawnCategoryMultiplierModifier::operation)
            ).apply(p_349989_, SpawnCategoryMultiplierModifier::new));

    public static final Codec<SpawnCategoryMultiplierModifier> CODEC = MAP_CODEC.codec();

    public static final StreamCodec<ByteBuf, SpawnCategoryMultiplierModifier> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, SpawnCategoryMultiplierModifier::id,
            ByteBufCodecs.DOUBLE, SpawnCategoryMultiplierModifier::amount,
            Operation.STREAM_CODEC, SpawnCategoryMultiplierModifier::operation,
            SpawnCategoryMultiplierModifier::new
    );


    public enum Operation implements StringRepresentable {
        ADD_VALUE("add_value", 0),
        ADD_MULTIPLIED_BASE("add_multiplied_base", 1),
        ADD_MULTIPLIED_TOTAL("add_multiplied_total", 2);

        public static final IntFunction<Operation> BY_ID = ByIdMap.continuous(Operation::id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
        public static final StreamCodec<ByteBuf, Operation> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Operation::id);
        public static final Codec<Operation> CODEC = StringRepresentable.fromEnum(Operation::values);
        private final String name;
        private final int id;

        private Operation(String name, int value) {
            this.name = name;
            this.id = value;
        }

        public int id() {
            return this.id;
        }

        public String getSerializedName() {
            return this.name;
        }
    }
}
