package poptartfrompluto.explots;

import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public class UncheckedExplotTransaction {
    private final Player player;
    private final Town town;

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

    public int getMaxPossibleAmountOfPlots() {
        var exp = player.getTotalExperience();
        var plots = 0;

        while (exp >= getRequiredExp(plots + 1)) {
            plots += 1;
        }

        return plots;
    }

    public int getPreviousPlotsBought() {
        var townHistory = new PlayerTownHistory();
        townHistory.load(player);
        return townHistory.joinedTownsWithBoughtPlotsCount.getOrDefault(town, 0);
    }

    public long getTimeUntilExplotPurchaseCooldownEnds() {
        var pdc = player.getPersistentDataContainer();
        var purchaseTime = pdc.get(ExplotPdcKeys.LAST_PURCHASE_TIME.key, PersistentDataType.LONG);
        if (purchaseTime == null) {
            return Long.MIN_VALUE;
        }

        return purchaseTime + EXPlots.plotCooldown - System.currentTimeMillis();
    }

    public void payForBonusPlots(int plotCount) {
        player.giveExp(-getRequiredExp(plotCount));
        town.addBonusBlocks(plotCount);

        var towns = new PlayerTownHistory();
        towns.load(player);
        towns.joinedTownsWithBoughtPlotsCount.merge(town, plotCount, Math::addExact);
        towns.save();

        var pdc = player.getPersistentDataContainer();
        pdc.set(ExplotPdcKeys.LAST_PURCHASE_TIME.key, PersistentDataType.LONG, System.currentTimeMillis());
    }
}
