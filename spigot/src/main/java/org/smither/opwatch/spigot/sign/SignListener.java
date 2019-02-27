package org.smither.opwatch.spigot.sign;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.smither.opwatch.spigot.RestController;
import org.smither.opwatch.spigot.Utils;
import org.smither.opwatch.utils.SignChangeType;

import java.util.Set;

public class SignListener implements Listener {

    private RestController restController;

    Set<Material> signSet = Utils.getSignSet();

    public SignListener(RestController restController) {
        this.restController = restController;
    }

    @EventHandler
    public void onSignChange(SignChangeEvent sce) {
        restController.sendSignToServer(sce);
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent bbe) {
        if (signSet.contains(bbe.getBlock().getType())) {
            restController.deleteSignFromServer(bbe.getBlock(), bbe.getPlayer(), SignChangeType.PLAYER_DESTROY, "destroyed in game");
        }
    }
}
