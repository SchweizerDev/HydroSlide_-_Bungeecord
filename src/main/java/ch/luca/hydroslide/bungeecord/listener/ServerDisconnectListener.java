package ch.luca.hydroslide.bungeecord.listener;

import ch.luca.hydroslide.bungeecord.config.SlotConfig;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerDisconnectListener implements Listener {

    @EventHandler
    public void onDisconnect(ServerDisconnectEvent e) {
        try {
            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                String header =
                        "§8§l§m*-*-*-*-*-*-*-*§b HydroSlide §8§l§m*-*-*-*-*-*-*-*"
                                + "\n"
                                + "§7Aktueller Server §8➸ §6§o" + all.getServer().getInfo().getName()
                                + "\n"
                                + "§7Spieler online §8➸ §e§o" + ProxyServer.getInstance().getPlayers().size() + "§8/§e§o" + SlotConfig.getSlots()
                                + "\n";
                //Userlist
                String footer =
                        "\n"
                                + "§7Du brauchst Hilfe? Nutze §e§o/support §7oder unseren §e§oDiscord§7."
                                + "\n"
                                + "\n"
                                + "§7•●• Webseite: §e§ohydroslide.eu §7•●•"
                                + "\n"
                                + "§8§l§m*-*-*-*-*-*-*-*§b HydroSlide §8§l§m*-*-*-*-*-*-*-*";
                all.setTabHeader((BaseComponent) new TextComponent(header), (BaseComponent) new TextComponent(footer));
            }
        } catch (Exception all) {
        }
    }
}
