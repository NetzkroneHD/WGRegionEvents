package de.netzkronehd.wgregionevents.events;

import java.util.Objects;

public class MovementWay {

    public static final MovementWay MOVE = new MovementWay("MOVE", 0, true);
    public static final MovementWay TELEPORT = new MovementWay("TELEPORT", 1, true);
    public static final MovementWay SPAWN = new MovementWay("SPAWN", 2, false);
    public static final MovementWay DISCONNECT = new MovementWay("DISCONNECT", 3, false);

    private static final MovementWay[] values = {MOVE, TELEPORT, SPAWN, DISCONNECT};

    private final String name;
    private final int ordinal;
    private final boolean cancellable;

    public MovementWay(String name, int ordinal, boolean cancellable) {
        this.name = name;
        this.ordinal = ordinal;
        this.cancellable = cancellable;
    }

    public String getName() {
        return name;
    }

    public int ordinal() {
        return ordinal;
    }

    public boolean isCancellable() {
        return cancellable;
    }

    public String name() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovementWay that = (MovementWay) o;
        return ordinal == that.ordinal && cancellable == that.cancellable && Objects.equals(name, that.name);
    }

    public static MovementWay[] values() {
        return values;
    }

}
