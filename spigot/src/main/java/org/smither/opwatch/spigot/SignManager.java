package org.smither.opwatch.spigot;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.smither.opwatch.spigot.exception.BlockNotSign;
import org.smither.opwatch.utils.SocketDTO.SignChangeRequestDTO;
import org.smither.opwatch.utils.sharedDTO.ErrorDTO;
import org.smither.opwatch.utils.sharedDTO.SignEventPostDTO;

import java.time.LocalDateTime;
import java.util.*;

public class SignManager {
    private static Set<Material> signMat = Utils.getSignSet();

    public static void changeSign(String world, int x, int y, int z, List<String> lines) {
        if (lines.size() != 4) {
            throw new IllegalArgumentException("Sign Change request MUST contain 4 lines");
        }
        Block block = Bukkit.getWorld(world).getBlockAt(x, y, z);
        if (!signMat.contains(block.getState().getType())) {
            throw new BlockNotSign(world, x, y, z);
        }
        Sign sign = ((Sign) block);
        for (int i = 0; i < 4; i++) {
            sign.setLine(i, lines.get(i));
        }
        sign.update(true);
    }

    public static void destroySign(String world, int x, int y, int z) {
        Block block = Bukkit.getWorld(world).getBlockAt(x, y, z);
        if (!signMat.contains(block.getState().getType())) {
            throw new BlockNotSign(world, x, y, z);
        }
        if (!block.breakNaturally()) {
            BlockState state = block.getState();
            state.setType(Material.AIR);
            state.update(true);
        }
    }

    public static void changeSign(SignChangeRequestDTO dto) {
        try {
            switch (dto.getEventType()) {
                case AUTO_SUPPRESS:
                case OP_WIPE:
                case OP_CHANGE:
                    changeSign(dto.getWorld(), dto.getX(), dto.getY(), dto.getZ(),
                            Arrays.asList(dto.getNewLine1(), dto.getNewLine2(), dto.getNewLine3(), dto.getNewLine4()));
                    break;
                case OP_DESTROY:
                case PLAYER_DESTROY:
                    destroySign(dto.getWorld(), dto.getX(), dto.getY(), dto.getZ());
                    break;
                default:
                    return;
            }
            RestController.sendDto(SignEventPostDTO.builder()
                    .id(dto.getId())
                    .dateTime(LocalDateTime.now())
                    .editor(dto.getActioner())
                    .eventType(dto.getEventType())
                    .newLine1(dto.getNewLine1())
                    .newLine2(dto.getNewLine2())
                    .newLine3(dto.getNewLine3())
                    .newLine4(dto.getNewLine4())
                    .reason(dto.getReason())
                    .server(UUID.fromString(Plugin.getInstance().getConfig().getString("userID")))
                    .world(dto.getWorld())
                    .x(dto.getX())
                    .y(dto.getY())
                    .z(dto.getZ())
                    .build());
        } catch (Exception ex) {
            Plugin.getInstance().getLogger().warning(String.format("Sign change request FAILED due to an exception, %s", ex.getMessage()));
            RestController.sendDto(
                    ErrorDTO.builder()
                            .message(String.format("Sign change request FAILED due to an exception, %s", ex.getMessage()))
                            .build());
        }
    }
}
