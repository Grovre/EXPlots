package poptartfrompluto.explots;

import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public class UncheckedExplotTransaction {
    private Player player;
    private Town town;

    public UncheckedExplotTransaction(Player player, Town town) {
        this.player = player;
        this.town = town;
    }

    public int getRequiredExp(int plotCount) {
        var scale = EXPlots.plotCostScale;
        var baseExpCost = EXPlots.plotExperienceCost;
        var plotsBought = getPreviousPlotsBought();
        var cost = 0;

        for (var i = 0; i < plotCount; i++)
            cost += baseExpCost * Math.pow(scale, (double)plotsBought + i);

        return cost;
    }

    public int getPossibleAmountOfPlots() {
        var exp = player.getTotalExperience();
        var plots = 0;

        while (exp >= getRequiredExp(plots + 1)) {
            plots += 1;
        }

        return plots;
    }

    public int getPreviousPlotsBought() {
        var pdc = player.getPersistentDataContainer();
        var plotsBought = pdc.get(ExplotPdcKeys.PLOTS_BOUGHT.key, PersistentDataType.INTEGER);
        if (plotsBought == null)
            plotsBought = 0;

        return plotsBought;
    }

    public void payForBonusPlots(int plotCount) {
        player.giveExp(-getRequiredExp(plotCount));
        town.addBonusBlocks(plotCount);

        var pdc = player.getPersistentDataContainer();
        var plotsBought = pdc.get(ExplotPdcKeys.PLOTS_BOUGHT.key, PersistentDataType.INTEGER);
        plotsBought = plotsBought == null
                ? 1
                : plotsBought + plotCount;
        pdc.set(ExplotPdcKeys.PLOTS_BOUGHT.key, PersistentDataType.INTEGER, plotsBought);
    }
}
