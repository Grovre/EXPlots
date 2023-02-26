package poptartfrompluto.explots.commands;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import java.util.Collections;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import poptartfrompluto.explots.EXPlots;

import java.util.List;
import java.util.Objects;

public class ExchangeExpCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!label.equalsIgnoreCase("EXPlots")) {
            return false;
        }

        if (args.length != 1)
            return false;

        if (!args[0].equalsIgnoreCase("buy")) {
            sender.sendMessage(Color.RED + "Unknown command. Try /EXPlots buy");
        }

        if (!sender.hasPermission("explots.trade")) {
            sender.sendMessage(Color.RED + "You don't have permission to trade experience for plots");
            return true;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage("You are not a player and therefore cannot exchange experience for plots");
            return true;
        }

        if (player.getTotalExperience() < EXPlots.plotExperienceCost) {
            sender.sendMessage(Color.RED + "You don't have enough experience! You are missing " + (EXPlots.plotExperienceCost - player.getTotalExperience()));
            return true;
        }

        var resident = TownyAPI.getInstance().getResident(player);
        Objects.requireNonNull(resident);

        if (!resident.hasTown()) {
            sender.sendMessage(Color.RED + "You need to be in a town to purchase plots");
            return true;
        }

        player.giveExp(-EXPlots.plotExperienceCost);
        try {
            resident.getTown().addBonusBlocks(1);
        } catch (NotRegisteredException e) {
            throw new RuntimeException(e);
        }

        var plotsBought = 1; // May add functionality to purchase multiple plots
        sender.sendMessage(Color.GREEN + "Successfully traded " + EXPlots.plotExperienceCost + " XP for " + plotsBought + " plot" + (plotsBought != 1 ? "s" : ""));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("EXPlots") && args.length == 1)
            return List.of("buy");
        return null;
    }
}