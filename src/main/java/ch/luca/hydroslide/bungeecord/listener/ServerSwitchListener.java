package ch.luca.hydroslide.bungeecord.listener;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import ch.luca.hydroslide.bungeecord.config.SlotConfig;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.util.concurrent.TimeUnit;

public class ServerSwitchListener implements Listener {

    @EventHandler
    public void onSwitch(ServerSwitchEvent e) {
        try {
            ProxyServer.getInstance().getScheduler().schedule((Plugin) HydroSlide.getInstance(), new Runnable() {
                @Override
                public void run() {
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
                }
            }, 15, TimeUnit.MILLISECONDS);
        } catch (Exception var3_3) {}
    }
}

