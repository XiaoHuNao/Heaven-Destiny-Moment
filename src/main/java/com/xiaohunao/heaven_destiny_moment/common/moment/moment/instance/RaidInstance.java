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
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import java.util.Set;
import java.util.UUID;

public class RaidInstance extends MomentInstance<RaidMoment> {
    private int totalWaves;

    protected Set<Integer> enemies = Sets.newLinkedHashSet();
    protected int currentWave = -1;
    protected int totalEnemy = 0;
    private int readyTime;



    public RaidInstance(Level level, ResourceKey<Moment<?>> momentKey) {
        super(HDMMomentTypes.RAID.get(), level, momentKey);
    }


    public RaidInstance(UUID uuid, Level level, ResourceKey<Moment<?>> momentResourceKey) {
        super(HDMMomentTypes.RAID.get(), uuid, level, momentResourceKey);
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
        });
    }

    @Override
    protected void ready() {
        if (this.bar != null){
            int readyTime = moment().map(RaidMoment::readyTime).orElse(100);

            if (this.readyTime <= 0) {
                setState(MomentState.START);
            }
            this.bar.updateProgress(1 - (float) this.readyTime / readyTime);
            this.readyTime--;
        }else {
            setState(MomentState.END);
        }
    }

    @Override
    protected void ongoing() {
        checkNextWave();
        updateWave();
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
                    .ifPresent(entitySpawnSettings -> {
                        entitySpawnSettings.spawnList(level, currentWave).forEach(entity -> {
                            enemies.add(entity.getId());
                            entity.setGlowingTag(true);
                            spawnEntity(entity);
                        });
                    });
        }
        enemies.removeIf(id -> {
            Entity entity = level.getEntity(id);
            this.bar.updateProgress(enemies.size() / (float) totalEnemy);
            return entity == null;
        });
    }

    public int getReadyTime() {
        return readyTime;
    }
}
