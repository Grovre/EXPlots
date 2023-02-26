package poptartfrompluto.explots.commands;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import poptartfrompluto.explots.EXPlots;

import java.awt.*;
import java.util.List;
import java.util.Objects;

public class ExchangeExpCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!label.equalsIgnoreCase("buyPlotWithXp")) {
            return false;
        }

        if (!sender.hasPermission("explots.trade")) {
            sender.sendMessage(Color.RED + "You don't have permission to trade experience for plots");
            return false;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("You are not a player and therefore cannot exchange experience for plots");
            return false;
        }

        Player player = (Player) sender;

        if (player.getTotalExperience() < EXPlots.plotExperienceCost) {
            sender.sendMessage(Color.RED + "You don't have enough experience! You are missing " + (EXPlots.plotExperienceCost - player.getTotalExperience()));
            return false;
        }

        var resident = TownyAPI.getInstance().getResident(player);
        Objects.requireNonNull(resident);

        if (!resident.hasTown()) {
            sender.sendMessage(Color.RED + "You need to be in a town to purchase plots");
            return false;
        }

        player.setTotalExperience(player.getTotalExperience() - EXPlots.plotExperienceCost);
        try {
            resident.getTown().addBonusBlocks(1);
        } catch (NotRegisteredException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}
