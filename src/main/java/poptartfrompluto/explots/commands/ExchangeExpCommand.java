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
import java.util.stream.IntStream;

public class ExchangeExpCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1 && args.length != 2) {
            sender.sendMessage(ChatColor.RED + "There must be 2 arguments (buy #)");
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
            sender.sendMessage(ChatColor.RED + "You don't have enough experience! For 1 plot, you need at least " + (EXPlots.plotExperienceCost - player.getTotalExperience()) + " experience");
            return true;
        }

        var resident = TownyAPI.getInstance().getResident(player);
        Objects.requireNonNull(resident);

        if (!resident.hasTown()) {
            sender.sendMessage(ChatColor.RED + "You need to be in a town to purchase plots");
            return true;
        }

        int plotsBeingBought;
        try {
            plotsBeingBought = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage("Your second argument needs to be a number. For example, /explots buy 1");
            return true;
        }

        UncheckedExplotTransaction transaction;
        String successMsg;
        try {
            transaction = new UncheckedExplotTransaction(player, resident.getTown());
            successMsg = "Successfully traded " + transaction.getRequiredExp(plotsBeingBought) + " XP for " + 1 + " plot" + (plotsBeingBought != 1 ? "s" : "");
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
        if (!label.equalsIgnoreCase("EXPlots"))
            return null;

        if (!(sender instanceof Player player)) {
            return null;
        }

        var transaction = new UncheckedExplotTransaction(player, TownyAPI.getInstance().getTown(player));

        if (args.length == 1)
            return List.of("buy");
        if (args.length == 2)
            return IntStream
                    .range(1, transaction.getPossibleAmountOfPlots())
                    .mapToObj(Integer::toString)
                    .toList();

        return null;
    }
}
