package com.xiaohunao.heaven_destiny_moment.common.context;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedEntry;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;

public record Weighted<T>(int totalWeight, List<WeightedEntry.Wrapper<T>> list) {
    private static final Random random = new Random();

    private static int calculateTotalWeight(List<? extends WeightedEntry> entries) {
        long total = 0;
        for (WeightedEntry entry : entries) {
            total += entry.getWeight().asInt();
        }
        if (total > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Sum of weights must be <= 2147483647");
        }
        return (int) total;
    }

    public List<T> getRandomSubset() {
        ArrayList<T> subSet = Lists.newArrayList();
        if (this.totalWeight == 0) {
            return subSet;
        } else {
            for (WeightedEntry.Wrapper<T> entry : this.list) {
                if (random.nextInt(this.totalWeight) < entry.getWeight().asInt()) {
                    subSet.add(entry.data());
                }
            }
        }
        return subSet;
    }

    public Optional<WeightedEntry.Wrapper<T>> getRandom() {
        if (this.totalWeight == 0) {
            return Optional.empty();
        } else {
            int index = random.nextInt(this.totalWeight);
            return getWeightedItem(this.list, index);
        }
    }

    public Optional<T> getRandomValue() {
        return getRandom().map(WeightedEntry.Wrapper::data);
    }

    private static <T extends WeightedEntry> Optional<T> getWeightedItem(List<T> entries, int weight) {
        for (T entry : entries) {
            weight -= entry.getWeight().asInt();
            if (weight < 0) {
                return Optional.of(entry);
            }
        }
        return Optional.empty();
    }
    public List<WeightedEntry.Wrapper<T>> unwrap() {
        return this.list;
    }

    public static <T> Codec<Weighted<T>> codec(Codec<T> codec) {
        return WeightedEntry.Wrapper.codec(codec).listOf()
                .xmap(wrappers -> new Weighted<>(calculateTotalWeight(wrappers), wrappers), Weighted::unwrap);
    }

    public static class Builder<T> {
        private final List<WeightedEntry.Wrapper<T>> list = Lists.newArrayList();
        
        public Builder<T> add(T t, int weight) {
            list.add(WeightedEntry.wrap(t, weight));
            return this;
        }

        public Weighted<T> build(){
            return new Weighted<>(calculateTotalWeight(list), list);
        }
    }

}