package com.xiaohunao.heaven_destiny_moment.common.moment;


import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.codec.CodecMap;
import com.xiaohunao.heaven_destiny_moment.common.codec.CodecProvider;
import com.xiaohunao.heaven_destiny_moment.common.context.ClientSettingsContext;
import com.xiaohunao.heaven_destiny_moment.common.context.MomentDataContext;
import com.xiaohunao.heaven_destiny_moment.common.moment.coverage.AreaCoverage;
import com.xiaohunao.heaven_destiny_moment.common.moment.coverage.LevelCoverage;
import com.xiaohunao.heaven_destiny_moment.common.moment.type.BloodMoonMoment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collection;
import java.util.Objects;

public abstract class Moment implements CodecProvider<Moment> {
    public static final CodecMap<Moment> CODEC = new CodecMap<>("moment");

    public static void register() {
        CODEC.register(DefaultMoment.ID, DefaultMoment.CODEC);
        CODEC.register(BloodMoonMoment.ID, BloodMoonMoment.CODEC);
    }


    protected final ResourceLocation barRenderType;
    protected final MomentCoverage coverageType;
    protected final MomentDataContext momentDataContext;
    protected final ClientSettingsContext clientSettingsContext;

    public Moment(ResourceLocation barRenderType, MomentDataContext momentDataContext, ClientSettingsContext clientSettingsContext) {
        this.barRenderType = barRenderType;
        this.coverageType = checkCoverage(momentDataContext,clientSettingsContext);
        this.momentDataContext = momentDataContext;
        this.clientSettingsContext = clientSettingsContext;
    }

    public MomentCoverage checkCoverage(MomentDataContext momentDataContext, ClientSettingsContext clientSettingsContext) {
        if (momentDataContext.checkLevelCoverage() || clientSettingsContext.checkLevelCoverage()){
            return LevelCoverage.DEFAULT;
        }
        return AreaCoverage.DEFAULT;
    }


    public boolean isCompatible(Collection<MomentInstance> runMoment){
        if (runMoment.isEmpty()) return true;

        if (coverageType == LevelCoverage.DEFAULT) {
            int count = 0;
            Moment moment = null;
            for (MomentInstance momentInstance : runMoment) {
                Moment moment1 = momentInstance.getMoment();
                if (moment1.coverageType() == LevelCoverage.DEFAULT) {
                    moment = moment1;
                    count++;
                }
            }
            if (count > 1) {
                HeavenDestinyMoment.LOGGER.debug("Currently there is a LevelCoverage Moment that has not ended, and it cannot be added. Its id is {}", moment);
                return false;
            }
        }
        return true;
    }

    public boolean shouldEnd(MomentInstance instance) {
        return instance.state == MomentState.END;
    }

    public void tick(MomentInstance instance){

    }

    public ResourceLocation barRenderType() {
        return barRenderType;
    }

    public MomentCoverage coverageType() {
        return coverageType;
    }

    public MomentDataContext momentDataContext() {
        return momentDataContext;
    }
    public ClientSettingsContext clientSettingsContext(){
        return clientSettingsContext;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Moment) obj;
        return Objects.equals(this.barRenderType, that.barRenderType) &&
                Objects.equals(this.momentDataContext, that.momentDataContext);
    }

    @Override
    public int hashCode() {
        return Objects.hash(barRenderType, momentDataContext);
    }

//    @Override
//    public String toString() {
//        return "Moment[" +
//                "barRenderType=" + barRenderType + ", " +
//                "momentDataContext=" + momentDataContext + ']';
//    }

    public void finalizeSpawn(MomentInstance momentInstance, LivingEntity livingEntity) {
        CompoundTag compoundTag = livingEntity.getPersistentData();
        compoundTag.putUUID("moment", momentInstance.getID());
    }

//    private ResourceLocation getRegisterKey(Level level) {
//        return level.registryAccess().registryOrThrow(ModMoments.MOMENT_KEY).getKey(this);
//    }
}
