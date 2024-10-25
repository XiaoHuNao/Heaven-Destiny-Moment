package com.xiaohunao.heaven_destiny_moment.common.context;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedEntry;

import java.util.List;
import java.util.Optional;

public class WeightedContext<T> {
    private final int totalWeight;
    private final List<WeightedEntry.Wrapper<T>> list;

    private WeightedContext(List<WeightedEntry.Wrapper<T>> list) {
        this.list = Lists.newArrayList(list);
        this.totalWeight = calculateTotalWeight(list);
    }

    public static <T> WeightedContext<T> create() {
        return new WeightedContext<>(Lists.newLinkedList());
    }
    public WeightedContext<T> add(T t, int weight) {
        list.add(WeightedEntry.wrap(t, weight));
        return this;
    }

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

    public Optional<WeightedEntry.Wrapper<T>> getRandom(RandomSource random) {
        if (this.totalWeight == 0) {
            return Optional.empty();
        } else {
            int index = random.nextInt(this.totalWeight);
            return getWeightedItem(this.list, index);
        }
    }
    public Optional<T> getRandomValue(RandomSource random) {
        return getRandom(random).map(WeightedEntry.Wrapper::data);
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
    public static <T> Codec<WeightedContext<T>> codec(Codec<T> codec) {
        return WeightedEntry.Wrapper.codec(codec).listOf().xmap(WeightedContext::new, WeightedContext::unwrap);
    }
}