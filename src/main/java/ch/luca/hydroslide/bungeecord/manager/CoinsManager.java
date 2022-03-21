package ch.luca.hydroslide.bungeecord.manager;

import java.text.DecimalFormat;

public class CoinsManager {

    public static String asString(int coins) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(coins).replace(",", "'");
    }
}
