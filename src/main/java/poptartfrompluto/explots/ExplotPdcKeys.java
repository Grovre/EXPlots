package poptartfrompluto.explots;

import org.bukkit.NamespacedKey;

public enum ExplotPdcKeys {
    @Deprecated
    PLOTS_BOUGHT("TOTAL_PLOTS_BOUGHT"),
    TOWNY_JOIN_HISTORY("EXPLOT_ALL_TOWNS_JOINED"),
    LAST_PURCHASE_TIME("EXPLOT_LAST_PURCHASE_TIME")
    ;

    final NamespacedKey key;

    ExplotPdcKeys(String keyStr) {
        key = new NamespacedKey(EXPlots.getEXPlots(), keyStr);
    }
}
