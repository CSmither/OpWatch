package org.smither.opwatch.utils;

public enum SignChangeReason {
    PLAYER_DESTROY("destroyed by player"),
    AUTO_SUPPRESS("automatically Suppressed"),
    OP_CHANGE("changed by admin"),
    OP_WIPE("wiped by admin"),
    OP_DESTROY("destroyed by admin");

    SignChangeReason(String desc) {
        this.desc = desc;
    }

    private String desc;

    public String getDesc() {
        return desc;
    }
}
