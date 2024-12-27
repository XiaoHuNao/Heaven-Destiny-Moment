package com.xiaohunao.heaven_destiny_moment.common.moment;

import com.google.common.collect.ImmutableSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.UUID;

public class MomentType {
    private final MomentSupplier factory;

    public MomentType(MomentSupplier factory) {
        this.factory = factory;
    }

    @Nullable
    public MomentInstance create(UUID uuid, Level level, ResourceKey<Moment<?>> moment) {
        return factory.create(uuid, level, moment);
    }


    public static final class Builder {
        private final MomentSupplier factory;
        public Builder(MomentSupplier factory) {
            this.factory = factory;
        }

        public MomentType build() {
            return new MomentType(factory);
        }
    }

    @FunctionalInterface
    public interface MomentSupplier {
        MomentInstance create(UUID uuid, Level level, ResourceKey<Moment<?>> moment);
    }
}
