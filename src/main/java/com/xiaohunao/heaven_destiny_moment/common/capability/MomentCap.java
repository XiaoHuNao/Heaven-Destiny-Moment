package com.xiaohunao.heaven_destiny_moment.common.capability;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.init.ModCapability;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;


public class MomentCap implements INBTSerializable<ListTag> {
    private final Multimap<UUID, MomentInstance> runMoment = LinkedHashMultimap.create();


    private final Level level;

    public MomentCap(Level level) {
        this.level = level;
    }

    public static LazyOptional<MomentCap> getCap(Level level) {
        return level.getCapability(ModCapability.MOMENT_CAP);
    }


    @Override
    public ListTag serializeNBT() {
        ListTag tags = new ListTag();
        runMoment.values().forEach(moment -> tags.add(moment.serializeNBT()));
        return tags;
    }

    @Override
    public void deserializeNBT(ListTag tags) {
        tags.stream()
                .filter(tag -> tag instanceof CompoundTag)
                .map(tag -> (CompoundTag) tag)
                .map(compoundTag -> MomentInstance.createFromCompoundTag(compoundTag,level))
                .filter(Objects::nonNull)
                .forEach(moment -> runMoment.put(moment.getID(), moment));
    }

    public void tick() {
        runMoment.values().forEach(MomentInstance::baseTick);
    }

    public void addMoment(MomentInstance moment) {
        runMoment.put(moment.getID(), moment);
        System.out.println(runMoment);



//        long count = runMoment.values().stream()
//                .filter(moment1 -> !moment1.isMobSpawnSettingsEmpty())
//                .count();
//        if (count < 2) {
//            runMoment.put(moment.getID(), moment);
//        } else {
//            HeavenDestinyMoment.LOGGER.debug("Only one Moment object that has modified MobSpawnSettings can exist at the same time, The current {} Moment object will not be added", moment.getRegistryName());
//        }
    }

    public void removeMoment(UUID uuid) {
        runMoment.removeAll(uuid);
    }
    public MomentInstance getOnlyModifiedMobSpawnSettingsMoment() {
        return runMoment.values().stream()
//                .filter(moment -> !moment.isMobSpawnSettingsEmpty())
                .findFirst()
                .orElse(null);
    }



    public static class Provider implements ICapabilitySerializable<ListTag> {
        private final MomentCap cap;

        public Provider(Level level) {
            this.cap = new MomentCap(level);
        }

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return ModCapability.MOMENT_CAP.orEmpty(cap, LazyOptional.of(() -> this.cap)).cast();
        }

        @Override
        public ListTag serializeNBT() {
            return cap.serializeNBT();
        }

        @Override
        public void deserializeNBT(ListTag nbt) {
            cap.deserializeNBT(nbt);
        }
    }
}
