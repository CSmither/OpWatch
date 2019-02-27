package org.smither.opwatch.spigot.exception;

public class BlockNotSign extends RuntimeException {
    public BlockNotSign(String world, int x, int y, int z) {
        super(String.format("Block at %s - %s, %s, %s is not a sign", world, x, y, z));
    }
}
