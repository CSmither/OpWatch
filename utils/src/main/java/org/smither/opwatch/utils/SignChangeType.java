package org.smither.opwatch.utils;

public enum SignChangeType {
  CHANGE("changed"),
  DELETE("deleted");

  SignChangeType(String desc) {
    this.desc = desc;
  }

  private String desc;

  public String getDesc() {
    return desc;
  }
}
