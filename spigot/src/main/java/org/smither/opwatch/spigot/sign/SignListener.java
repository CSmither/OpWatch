package org.smither.opwatch.spigot.sign;

import com.google.common.collect.Sets;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.smither.opwatch.spigot.RestController;

import java.util.Set;

public class SignListener implements Listener {

  private RestController restController;

  private Set<Material> signSet =
      Sets.newHashSet(
          Material.SIGN,
          Material.WALL_SIGN,
          Material.LEGACY_SIGN,
          Material.LEGACY_WALL_SIGN,
          Material.LEGACY_SIGN_POST);

  public SignListener(RestController restController) {
    this.restController = restController;
  }

  @EventHandler
  public void onSignChange(SignChangeEvent sce) {
    restController.sendSignToServer(sce);
  }

  @EventHandler
  public void onBlockBreakEvent(BlockBreakEvent bbe) {
    if (signSet.contains(bbe.getBlock().getType())){
        restController.deleteSignFromServer(bbe.getBlock(), bbe.getPlayer());
    }
  }
}
