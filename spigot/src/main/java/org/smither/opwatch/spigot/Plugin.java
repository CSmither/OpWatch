package org.smither.opwatch.spigot;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class Plugin extends JavaPlugin {

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private static Plugin instance;

    @Override
    public void onLoad() {
        setInstance(this);
    }

    @Override
    public void onDisable() {}

    @Override
    public void onEnable() {}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

}
