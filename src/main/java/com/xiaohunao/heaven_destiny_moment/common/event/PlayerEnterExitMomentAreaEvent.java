package com.xiaohunao.heaven_destiny_moment.common.event;

import com.xiaohunao.heaven_destiny_moment.common.moment.area.Area;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public abstract class PlayerEnterExitMomentAreaEvent extends PlayerEvent {
    public Area area;

    public PlayerEnterExitMomentAreaEvent(Player player,Area area) {
        super(player);
        this.area = area;
    }


    public static class Enter extends PlayerEnterExitMomentAreaEvent{

        public Enter(Player player, Area area) {
            super(player, area);
        }
    }
    public static class Exit extends PlayerEnterExitMomentAreaEvent{

        public Exit(Player player, Area area) {
            super(player, area);
        }
    }
}
