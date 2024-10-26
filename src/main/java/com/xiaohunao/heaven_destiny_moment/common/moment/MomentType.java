package com.xiaohunao.heaven_destiny_moment.common.moment;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Set;

public class MomentType<T extends MomentInstance> {
    private final MomentType.MomentSupplier<? extends T> factory;
    private final Set<Class<? extends Moment>> validMoments;

    public MomentType(MomentType.MomentSupplier<? extends T> factory, Set<Class<? extends Moment>> validMoments) {
        this.factory = factory;
        this.validMoments = validMoments;
    }

    @Nullable
    public T create(Level level, Moment moment) {
        return this.factory.create(level,moment);
    }

    public boolean isValid(Moment moment) {
        return this.validMoments.contains(moment.getClass());
    }

    @FunctionalInterface
    public interface MomentSupplier<T extends MomentInstance> {
        T create(Level level,Moment moment);
    }

    public static final class Builder<T extends MomentInstance> {
        private final MomentSupplier<? extends T> factory;
        final Set<Class<? extends Moment>> validMoments;

        private Builder(MomentSupplier<? extends T> factory, Set<Class<? extends Moment>> validMoments) {
            this.factory = factory;
            this.validMoments = validMoments;
        }

        public static <T extends MomentInstance> Builder<T> of(MomentSupplier<? extends T> factory, Class<? extends Moment>... validMoments) {
            return new Builder<>(factory, ImmutableSet.copyOf(validMoments));
        }

        public MomentType<T> build() {
            return new MomentType<>(this.factory, this.validMoments);
        }
    }
}
