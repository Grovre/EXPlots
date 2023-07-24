package poptartfrompluto.explots;

import org.bukkit.plugin.java.JavaPlugin;
import poptartfrompluto.explots.commands.ExchangeExpCommand;

import java.util.Objects;

public final class EXPlots extends JavaPlugin {

    private static EXPlots plugin;

    public static int plotExperienceCost;
    public static double plotCostScale;
    public static long plotCooldown;

    @Override
    public void onEnable() {
        // Plugin startup logic

        plugin = this;

        this.saveDefaultConfig();
        var cfg = getConfig();
        plotExperienceCost = cfg.getInt("experienceCost");
        plotCostScale = cfg.getDouble("scalePerPurchase");
        plotCooldown = cfg.getLong("cooldown");

        var cmd = new ExchangeExpCommand();
        Objects.requireNonNull(getCommand("EXPlots")).setExecutor(cmd);
        Objects.requireNonNull(getCommand("EXPlots")).setTabCompleter(cmd);
    }

    public static EXPlots getEXPlots() {
        return plugin;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
