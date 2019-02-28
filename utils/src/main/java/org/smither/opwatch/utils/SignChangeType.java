package org.smither.opwatch.utils;

public enum SignChangeType {
  PLAYER_DESTROY("destroyed by player"),
  AUTO_SUPPRESS("automatically suppressed"),
  OP_CHANGE("changed by admin"),
  OP_WIPE("wiped by admin"),
  OP_DESTROY("destroyed by admin"),
  SIGN_DESTROYED("sign has disappeared, scary...");

  SignChangeType(String desc) {
    this.desc = desc;
  }

  private String desc;

  public String getDesc() {
    return desc;
  }
}
