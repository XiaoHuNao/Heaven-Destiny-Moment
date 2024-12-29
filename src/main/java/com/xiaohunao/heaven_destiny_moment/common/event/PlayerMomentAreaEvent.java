package com.xiaohunao.heaven_destiny_moment.common.event;

import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public abstract class PlayerMomentAreaEvent extends PlayerEvent {
    private final MomentInstance momentInstance;

    public PlayerMomentAreaEvent(Player player, MomentInstance momentInstance) {
        super(player);
        this.momentInstance = momentInstance;
    }


    public static class Enter extends PlayerMomentAreaEvent {
        public Enter(Player player, MomentInstance momentInstance) {
            super(player, momentInstance);
        }
    }

    public static class Exit extends PlayerMomentAreaEvent {
        public Exit(Player player, MomentInstance momentInstance) {
            super(player, momentInstance);
        }
    }
}
