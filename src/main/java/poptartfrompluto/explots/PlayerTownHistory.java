package poptartfrompluto.explots;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.HashMap;

public class PlayerTownHistory {
    Player player;
    HashMap<Town, Integer> joinedTownsWithBoughtPlotsCount = new HashMap<>();

    private static class TownNameAndPlotCountDTO {
        String townName;
        int plotCount;

        public TownNameAndPlotCountDTO(String townName, int plotCount) {
            this.townName = townName;
            this.plotCount = plotCount;
        }
    }

    public void load(Player player) {
        this.player = player;
        joinedTownsWithBoughtPlotsCount.clear();
        var pdc = player.getPersistentDataContainer();
        var fullPdcString = pdc.get(ExplotPdcKeys.TOWNY_JOIN_HISTORY.key, PersistentDataType.STRING);

        if (fullPdcString == null) {
            return;
        }

        Arrays.stream(fullPdcString.split(",")).map(s -> {
            var i = s.lastIndexOf(':'); // may avoid issues if town includes a colon in its name
            var townName = s.substring(0, i);
            int boughtPlots;
            try {
                boughtPlots = Integer.parseInt(s.substring(i));
            } catch (NumberFormatException e) {
                boughtPlots = 0;
            }
            return new TownNameAndPlotCountDTO(townName, boughtPlots);
        }).forEach(dto -> {
            if (dto.townName == null) {
                return;
            }

            var town = TownyAPI.getInstance().getTown(dto.townName);
            if (town == null) {
                return;
            }

            joinedTownsWithBoughtPlotsCount.put(town, dto.plotCount);
        });
    }

    public void save() {
        var pdc = player.getPersistentDataContainer();
        var sb = new StringBuilder();

        joinedTownsWithBoughtPlotsCount.forEach((town, plotCount) -> {
            sb.append(town.getName()).append(':').append(plotCount).append(',');
        });
        sb.deleteCharAt(sb.length() - 1);

        pdc.set(ExplotPdcKeys.TOWNY_JOIN_HISTORY.key, PersistentDataType.STRING, sb.toString());
    }
}
