package com.xiaohunao.heaven_destiny_moment.common.moment;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.xiaohunao.heaven_destiny_moment.common.init.MomentRegistry;
import com.xiaohunao.heaven_destiny_moment.common.init.MomentTypeRegistry;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.Optional;

public abstract class MomentInstance {
    private static final Logger LOGGER = LogUtils.getLogger();


    protected final Level level;
    protected final MomentType<?> type;
    private final Moment moment;

    public MomentInstance(MomentType<?> type,Level level, Moment moment) {
        this.type = type;
        this.level = level;
        this.moment = moment;
    }





    @Nullable
    public static MomentInstance loadStatic(ServerLevel level, CompoundTag compoundTag) {
        String id = compoundTag.getString("id");
        ResourceLocation resourcelocation = ResourceLocation.tryParse(id);
        if (resourcelocation == null) {
            LOGGER.error("MomentInstance has invalid type: {}", id);
            return null;
        } else {
            return MomentTypeRegistry.REGISTRY.getOptional(resourcelocation).map((momentType) -> {
                try {
                    Tag tag = compoundTag.get("moment");
                    Optional<Pair<Moment, Tag>> result = MomentRegistry.REGISTRY.byNameCodec().decode(NbtOps.INSTANCE, tag).result();
                    return momentType.create(level,result.get().getFirst());
                } catch (Throwable throwable) {
                    LOGGER.error("Failed to create MomentInstance {}", id, throwable);
                    return null;
                }
            }).map((momentType) -> {
                try {
                    momentType.deserializeNBT(compoundTag);
                    return momentType;
                } catch (Throwable throwable) {
                    LOGGER.error("Failed to load data for MomentInstance {}", id, throwable);
                    return null;
                }
            }).orElseGet(() -> {
                LOGGER.warn("Skipping MomentInstance with id {}", id);
                return null;
            });
        }
    }

    public CompoundTag serializeNBT() {
        CompoundTag compoundTag = new CompoundTag();

        compoundTag.putString("id",MomentInstance.getRegistryName(type).toString());
        MomentRegistry.REGISTRY.byNameCodec().encodeStart(NbtOps.INSTANCE,moment)
                .resultOrPartial(partialValue ->LOGGER.error("Failed to encode moment: {}", partialValue))
                .ifPresent(tag -> compoundTag.put("moment",tag));


        return compoundTag;

    }
    public void deserializeNBT(CompoundTag compoundTag) {

    }

    public static ResourceLocation getRegistryName(MomentType<?> momentType) {
        return MomentTypeRegistry.REGISTRY.getKey(momentType);
    }

    public Level getLevel() {
        return level;
    }
}
