package com.xiaohunao.heaven_destiny_moment.common.moment;

import com.google.common.collect.ImmutableSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.UUID;

public class MomentType<T extends MomentInstance> {
    private final MomentType.MomentSupplier<? extends T> factory;
    private final Set<Class<? extends Moment>> validMoments;

    public MomentType(MomentType.MomentSupplier<? extends T> factory, Set<Class<? extends Moment>> validMoments) {
        this.factory = factory;
        this.validMoments = validMoments;
    }

    @Nullable
    public T create(UUID uuid, Level level, ResourceKey<Moment> moment) {
        return factory.create(uuid, level, moment);
    }

    public boolean isValid(Moment moment) {
        return validMoments.contains(moment.getClass());
    }

    @SafeVarargs
    public static <T extends MomentInstance> Builder<T> builder(MomentSupplier<? extends T> factory, Class<? extends Moment>... validMoments) {
        return new Builder<>(factory, ImmutableSet.copyOf(validMoments));
    }

    public static final class Builder<T extends MomentInstance> {
        private final MomentSupplier<? extends T> factory;
        private final Set<Class<? extends Moment>> validMoments;

        private Builder(MomentSupplier<? extends T> factory, Set<Class<? extends Moment>> validMoments) {
            this.factory = factory;
            this.validMoments = validMoments;
        }

        public MomentType<T> build() {
            return new MomentType<>(factory, validMoments);
        }
    }

    @FunctionalInterface
    public interface MomentSupplier<T extends MomentInstance> {
        T create(UUID uuid, Level level, ResourceKey<Moment> moment);
    }
}
