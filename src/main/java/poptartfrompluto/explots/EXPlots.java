package poptartfrompluto.explots;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import poptartfrompluto.explots.commands.ExchangeExpCommand;

import java.util.Objects;

public final class EXPlots extends JavaPlugin {

    public static EXPlots plugin;

    public static int plotExperienceCost;

    @Override
    public void onEnable() {
        // Plugin startup logic

        plugin = this;

        var cfg = getConfig();
        plotExperienceCost = cfg.getInt("experienceCost");

        var cmd = new ExchangeExpCommand();
        Objects.requireNonNull(getServer().getPluginCommand("buyPlotWithXp")).setExecutor(cmd);
        Objects.requireNonNull(getServer().getPluginCommand("buyPlotWithXp")).setTabCompleter(cmd);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
