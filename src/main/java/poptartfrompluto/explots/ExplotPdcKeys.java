package poptartfrompluto.explots;

import org.bukkit.NamespacedKey;

public enum ExplotPdcKeys {
    @Deprecated
    PLOTS_BOUGHT("TOTAL_PLOTS_BOUGHT"),
    TOWNY_JOIN_HISTORY("ALL_TOWNS_JOINED")
    ;

    final NamespacedKey key;

    ExplotPdcKeys(String keyStr) {
        key = new NamespacedKey(EXPlots.getEXPlots(), keyStr);
    }
}
