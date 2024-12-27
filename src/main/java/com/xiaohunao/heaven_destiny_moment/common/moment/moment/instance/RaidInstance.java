package com.xiaohunao.heaven_destiny_moment.common.moment.moment.instance;

import com.xiaohunao.heaven_destiny_moment.common.init.HDMMomentTypes;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentState;
import com.xiaohunao.heaven_destiny_moment.common.moment.moment.RaidMoment;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class RaidInstance extends MomentInstance<RaidMoment> {
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
        this.readyTime = moment().map(RaidMoment::readyTime).orElse(100);
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
        }
    }

    public int getReadyTime() {
        return readyTime;
    }
}
