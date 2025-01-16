package com.xiaohunao.heaven_destiny_moment.common.moment.moment.instance;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.xiaohunao.heaven_destiny_moment.common.context.EntitySpawnSettings;
import com.xiaohunao.heaven_destiny_moment.common.context.MomentData;
import com.xiaohunao.heaven_destiny_moment.common.init.HDMMomentTypes;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentState;
import com.xiaohunao.heaven_destiny_moment.common.moment.moment.RaidMoment;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class RaidInstance extends MomentInstance<RaidMoment> {
    protected Vec3 originalPos;
    protected IntArrayList enemies = new IntArrayList();
    protected int currentWave = -1;
    private int totalWaves;
    protected int totalEnemy;
    private int readyTime;



    public RaidInstance(Level level, ResourceKey<Moment<?>> momentKey) {
        super(HDMMomentTypes.RAID.get(), level, momentKey);
    }


    public RaidInstance(UUID uuid, Level level, ResourceKey<Moment<?>> momentResourceKey) {
        super(HDMMomentTypes.RAID.get(), uuid, level, momentResourceKey);
    }

    @Override
    public void initSpawnPosList() {
        if (this.originalPos != null) {
            spawnPosList.add(this.originalPos);
        }
    }

    @Override
    public void init() {
        super.init();
        moment().ifPresent(raidMoment -> {
            this.readyTime = raidMoment.readyTime();
            this.totalWaves = raidMoment.momentData()
                                    .flatMap(MomentData::entitySpawnSettings)
                                    .flatMap(EntitySpawnSettings::entitySpawnList)
                                    .orElse(Lists.newArrayList())
                                    .size();

            raidMoment.momentData()
                    .flatMap(MomentData::entitySpawnSettings)
                    .flatMap(EntitySpawnSettings::entitySpawnList)
                    .ifPresent(entitySpawnList -> {
                        this.totalWaves = entitySpawnList.size();
                    });
        });
    }

    @Override
    protected void ready() {
        if (this.bar != null){
            int readyTime = moment().map(RaidMoment::readyTime).orElse(100);

            if (this.readyTime <= 0) {
                setState(MomentState.START);
            }
            updateBarProgress(1 - (float) this.readyTime / readyTime);
            this.readyTime--;
        }else {
            setState(MomentState.END);
        }
    }

    @Override
    protected void ongoing() {
        checkNextWave();
        updateWave();
        debug();

    }
    public void debug(){
        System.out.println("Wave: " + currentWave + " / " + totalWaves);
        System.out.println("Progress: " + enemies.size() + " / " + totalEnemy);
        if (!enemies.isEmpty()){
            enemies.forEach(id -> {
                Entity entity = level.getEntity(id);
                System.out.println(entity.position());
            });
        }
    }


    @Override
    public void deserializeNBT(CompoundTag compoundTag) {
        super.deserializeNBT(compoundTag);
        this.enemies = new IntArrayList(compoundTag.getIntArray("enemies"));
        this.currentWave = compoundTag.getInt("currentWave");
        this.totalWaves = compoundTag.getInt("totalWaves");
        this.totalEnemy = compoundTag.getInt("totalEnemy");
        this.readyTime = compoundTag.getInt("readyTime");
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundTag = super.serializeNBT();
        compoundTag.put("enemies",new IntArrayTag(enemies));
        compoundTag.put("currentWave",IntTag.valueOf(currentWave));
        compoundTag.put("totalWaves",IntTag.valueOf(totalWaves));
        compoundTag.put("totalEnemy",IntTag.valueOf(totalEnemy));
        compoundTag.put("readyTime",IntTag.valueOf(readyTime));
        return compoundTag;
    }

    protected void checkNextWave(){
        if (enemies.isEmpty()){
            if(this.currentWave >= this.totalWaves - 1) {
                setState(MomentState.VICTORY);
            } else {
                currentWave++;
                totalEnemy = 0;
            }
        }
    }

    protected void updateWave() {
        if (enemies.isEmpty() && state == MomentState.ONGOING){
            moment().flatMap(Moment::momentData)
                    .flatMap(MomentData::entitySpawnSettings)
                    .map(entitySpawnSettings -> entitySpawnSettings.spawnList(level, currentWave))
                    .ifPresent(entities -> entities.forEach(entity -> {
                        enemies.add(entity.getId());
                        entity.setGlowingTag(true);
                        spawnEntity(entity);
                        totalEnemy++;
                    }));
        }
        enemies.removeIf(id -> {
            Entity entity = level.getEntity(id);
            updateBarProgress(enemies.size() / (float) totalEnemy);
            return entity == null;
        });
    }

    public int getReadyTime() {
        return readyTime;
    }

    public RaidInstance setOriginalPos(Vec3 originalPos) {
        this.originalPos = originalPos;
        return this;
    }
}
