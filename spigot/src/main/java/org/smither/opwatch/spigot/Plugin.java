package org.smither.opwatch.spigot;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.smither.opwatch.spigot.sign.SignListener;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class Plugin extends JavaPlugin {

	@Getter
	@Setter(AccessLevel.PRIVATE)
	private static Plugin instance;

	@Override
	public void onLoad() {
		setInstance(this);
		if (!this.getDataFolder().exists()) {
			this.getDataFolder().mkdir();
		}
		try {
			if (this.getResource("config.yml") == null || this.getResource("config.yml").available() == 0) {
				this.saveDefaultConfig();
			}
		} catch (IOException e) {
			getLogger().warning(e.getMessage());
			this.saveDefaultConfig();
		}
		getServer().getPluginManager().registerEvents(new SignListener(new RestController()), this);
		Bukkit.broadcastMessage("OpWatch loaded");
	}

	@Override
	public void onDisable() {
		this.saveConfig();
	}

	@Override
	public void onEnable() {
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		return Collections.emptyList();
	}

}
