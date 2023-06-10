package poptartfrompluto.explots;

import org.bukkit.NamespacedKey;

public enum ExplotPdcKeys {
    PLOTS_BOUGHT("TOTAL_PLOTS_BOUGHT"),
    ;

    NamespacedKey key;

    ExplotPdcKeys(String keyStr) {
        key = new NamespacedKey(EXPlots.getEXPlots(), keyStr);
    }
}
