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

    public int getRequiredExp() {
        var cost = EXPlots.plotExperienceCost;
        var scale = EXPlots.plotCostScale;
        var plotsBought = getPreviousPlotsBought();

        cost *= Math.pow(scale, plotsBought);

        return cost;
    }

    public int getPreviousPlotsBought() {
        var pdc = player.getPersistentDataContainer();
        var plotsBought = pdc.get(ExplotPdcKeys.PLOTS_BOUGHT.key, PersistentDataType.INTEGER);
        if (plotsBought == null)
            plotsBought = 0;

        return plotsBought;
    }

    public void payForBonusPlots(int plotCount) {
        player.giveExp(-getRequiredExp());
        town.addBonusBlocks(plotCount);

        var pdc = player.getPersistentDataContainer();
        var plotsBought = pdc.get(ExplotPdcKeys.PLOTS_BOUGHT.key, PersistentDataType.INTEGER);
        plotsBought = plotsBought == null
                ? 1
                : plotsBought + 1;
        pdc.set(ExplotPdcKeys.PLOTS_BOUGHT.key, PersistentDataType.INTEGER, plotsBought);
    }
}
