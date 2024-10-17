package com.xiaohunao.heaven_destiny_moment.common.capability;

import com.google.common.collect.Maps;
import com.xiaohunao.heaven_destiny_moment.common.init.ModCapability;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import com.xiaohunao.heaven_destiny_moment.common.moment.coverage.LevelCoverage;
import com.xiaohunao.heaven_destiny_moment.common.network.ModMessages;
import com.xiaohunao.heaven_destiny_moment.common.network.s2c.MomentInstanceAddS2CPacket;
import com.xiaohunao.heaven_destiny_moment.common.network.s2c.MomentInstanceSyncS2CPacket;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;


public class MomentCap  {
    private final Map<UUID, MomentInstance> runMoment = Maps.newHashMap();
    private MomentInstance levelCoverageMomentMoment = null;

    private final Level level;

    public MomentCap(Level level) {
        this.level = level;
    }

    public static MomentCap getCap(Level level) {
        return level.getCapability(ModCapability.MOMENT_CAP).orElse(null);
    }


    public void tick() {
        CopyOnWriteArrayList<MomentInstance> momentInstances = new CopyOnWriteArrayList<>(runMoment.values());
        momentInstances.forEach(instance -> {
            if (instance.shouldEnd()) {
                removeMoment(instance.getID());
            }
            instance.baseTick();
        });
    }

    public void addMoment(MomentInstance instance) {
        Moment moment = instance.getMoment();
        if (moment.isCompatible(runMoment.values())) {
            runMoment.put(instance.getID(), instance);
            if (moment.coverageType() == LevelCoverage.DEFAULT) {
                levelCoverageMomentMoment = instance;
            }
            Map<ResourceKey<Moment>,UUID> map = Maps.newHashMap();
            map.put(instance.getMomentKey(),instance.getID());
            ModMessages.INSTANCE.send(PacketDistributor.ALL.noArg(),new MomentInstanceSyncS2CPacket(map,false));
        }
    }

    public void sync() {
        ModMessages.INSTANCE.send(PacketDistributor.ALL.noArg(),new MomentInstanceSyncS2CPacket(getMomentMap(),true));
    }
    public Map<ResourceKey<Moment>,UUID> getMomentMap() {
        Map<ResourceKey<Moment>,UUID> map = Maps.newHashMap();
        for (MomentInstance instance : runMoment.values()) {
            map.put(instance.getMomentKey(),instance.getID());
        }
        return map;
    }


    public void removeMoment(UUID uuid) {
        runMoment.remove(uuid);
    }

    public Map<UUID, MomentInstance> getRunMoment() {
        return runMoment;
    }

    public MomentInstance getLevelCoverageMomentMoment() {
        return levelCoverageMomentMoment;
    }

    public void setLevelCoverageMomentMoment(MomentInstance levelCoverageMomentMoment) {
        this.levelCoverageMomentMoment = levelCoverageMomentMoment;
    }

    public void addMomentKillCount(LivingEntity livingEntity){
        CompoundTag tag = livingEntity.getPersistentData();
        if (tag.contains("moment")) {
            UUID moment = tag.getUUID("moment");
            runMoment.values().stream()
                    .filter(momentInstance -> momentInstance.getID().equals(moment))
                    .forEach(momentInstance -> momentInstance.addKillCount(livingEntity));
        }
    }


    public CompoundTag serializeNBT() {
        CompoundTag compoundTag = new CompoundTag();
        ListTag listTag = new ListTag();
        runMoment.values().forEach(momentInstance -> listTag.add(momentInstance.serializeNBT()));
        compoundTag.put("moment", listTag);
        return compoundTag;
    }

    public void deserializeNBT(CompoundTag tags) {
        ListTag listTag = tags.getList("moment", 10);
        listTag.forEach(tag -> {
            MomentInstance momentInstance = MomentInstance.createFromCompoundTag((CompoundTag) tag, level);
            if (momentInstance != null) {
                addMoment(momentInstance);
            }
        });
    }

    public void clear() {
        runMoment.clear();
        levelCoverageMomentMoment = null;
    }


    public static class Provider implements ICapabilitySerializable<CompoundTag>{
        private final MomentCap cap;

        public Provider(Level level) {
            this.cap = new MomentCap(level);
        }

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return ModCapability.MOMENT_CAP.orEmpty(cap, LazyOptional.of(() -> this.cap)).cast();
        }

        @Override
        public CompoundTag serializeNBT() {
            return cap.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            cap.deserializeNBT(nbt);
        }
    }
}
