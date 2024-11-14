package com.xiaohunao.heaven_destiny_moment.common.event;

import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentState;
import net.neoforged.bus.api.Event;

public abstract class MomentEvent extends Event {
    private final MomentInstance momentInstance;

    public MomentEvent(MomentInstance momentInstance) {
        this.momentInstance = momentInstance;
    }

    public MomentInstance getMomentInstance() {
        return momentInstance;
    }

    public static class Tick extends MomentEvent {
        public Tick(MomentInstance momentInstance) {
            super(momentInstance);
        }
    }

    public static class Start extends MomentEvent {
        public Start(MomentInstance momentInstance) {
            super(momentInstance);
        }
    }

    public static class Ready extends MomentEvent {
        public Ready(MomentInstance momentInstance) {
            super(momentInstance);
        }
    }

    public static class OnGoing extends MomentEvent {
        public OnGoing(MomentInstance momentInstance) {
            super(momentInstance);
        }
    }

    public static class Victory extends MomentEvent {
        public Victory(MomentInstance momentInstance) {
            super(momentInstance);
        }
    }

    public static class Lose extends MomentEvent {
        public Lose(MomentInstance momentInstance) {
            super(momentInstance);
        }
    }

    public static class Celebrating extends MomentEvent {
        public Celebrating(MomentInstance momentInstance) {
            super(momentInstance);
        }
    }

    public static class End extends MomentEvent {
        public End(MomentInstance momentInstance) {
            super(momentInstance);
        }
    }

    public static MomentEvent getEventToPost(MomentInstance momentInstance, MomentState state) {
        return switch (state) {
            case READY -> new Ready(momentInstance);
            case START -> new Start(momentInstance);
            case ONGOING -> new OnGoing(momentInstance);
            case VICTORY -> new Victory(momentInstance);
            case LOSE -> new Lose(momentInstance);
            case CELEBRATING -> new Celebrating(momentInstance);
            case END -> new End(momentInstance);
            default -> throw new IllegalArgumentException("Invalid state: " + state);
        };
    }

}