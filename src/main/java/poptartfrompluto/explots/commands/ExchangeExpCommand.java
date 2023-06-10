package poptartfrompluto.explots.commands;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import poptartfrompluto.explots.EXPlots;
import poptartfrompluto.explots.UncheckedExplotTransaction;

import java.util.List;
import java.util.Objects;

public class ExchangeExpCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "There must be only 1 argument");
            return false;
        }

        if (!args[0].equalsIgnoreCase("buy")) {
            sender.sendMessage(ChatColor.RED + "Unknown argument. Try /EXPlots buy");
            return true;
        }

        if (!sender.hasPermission("explots.trade")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to trade experience for plots");
            return true;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage("You are not a player and therefore cannot exchange experience for plots");
            return true;
        }

        if (player.getTotalExperience() < EXPlots.plotExperienceCost) {
            sender.sendMessage(ChatColor.RED + "You don't have enough experience! You are missing " + (EXPlots.plotExperienceCost - player.getTotalExperience()));
            return true;
        }

        var resident = TownyAPI.getInstance().getResident(player);
        Objects.requireNonNull(resident);

        if (!resident.hasTown()) {
            sender.sendMessage(ChatColor.RED + "You need to be in a town to purchase plots");
            return true;
        }

        var plotsBeingBought = 1; // May add functionality to purchase multiple plots
        UncheckedExplotTransaction transaction;
        String successMsg;
        try {
            transaction = new UncheckedExplotTransaction(player, resident.getTown());
            successMsg = "Successfully traded " + transaction.getRequiredExp() + " XP for " + 1 + " plot" + (plotsBeingBought != 1 ? "s" : "");
            transaction.payForBonusPlots(1);
        } catch (NotRegisteredException e) {
            successMsg = "Did not succeed in buying plot";
            throw new RuntimeException(e); // Can't throw e, so you have to wrap it which sonarlint doesn't like because "RuntimeException is too generic" tf?
        }

        sender.sendMessage(ChatColor.GREEN + successMsg);
        EXPlots.getEXPlots().getServer().getConsoleSender().sendMessage(player.getName() + " " + successMsg);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("EXPlots") && args.length == 1)
            return List.of("buy");
        return null;
    }
}
