package ch.luca.hydroslide.bungeecord.listener;

import ch.luca.hydroslide.bungeecord.config.MaintenanceConfig;
import ch.luca.hydroslide.bungeecord.config.MotdConfig;
import ch.luca.hydroslide.bungeecord.config.SlotConfig;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ProxyPingListener implements Listener {

    @EventHandler
    public void onProxyPing(ProxyPingEvent e) {
        ServerPing ping = e.getResponse();
        if (MaintenanceConfig.getMaintenance() == true) {
            ping.setDescription(
                    MotdConfig.getMotdMaintenance1().replaceAll("%p%", "➟").replaceAll("&", "§").replaceAll("%s%", "┃")
                            .replaceAll("%x%", "×").replaceAll("%a%", "ä").replaceAll("%o%", "ö").replaceAll("%u%", "ü")
                            + "\n"
                            + MotdConfig.getMotdMaintenance2().replaceAll("%p%", "➟").replaceAll("&", "§")
                            .replaceAll("%s%", "┃").replaceAll("%x%", "×").replaceAll("%a%", "ä")
                            .replaceAll("%o%", "ö").replaceAll("%u%", "ü"));
            ping.setVersion(new ServerPing.Protocol("§cWartungsarbeiten", 0));
        } else {
            ping.setDescription(
                    MotdConfig.getMotdNormal1().replaceAll("%p%", "➟").replaceAll("&", "§").replaceAll("%s%", "┃")
                            .replaceAll("%x%", "×").replaceAll("%a%", "ä").replaceAll("%o%", "ö").replaceAll("%u%", "ü")
                            + "\n"
                            + MotdConfig.getMotdNormal2().replaceAll("%p%", "➟").replaceAll("&", "§")
                            .replaceAll("%s%", "┃").replaceAll("%x%", "×").replaceAll("%a%", "ä")
                            .replaceAll("%o%", "ö").replaceAll("%u%", "ü"));
        }
        ServerPing.PlayerInfo[] playerinfo = new ServerPing.PlayerInfo[]{
                new ServerPing.PlayerInfo("§bHydroSlide §7× §eNetzwerk", "0"),
                new ServerPing.PlayerInfo("    ", "0"),
                new ServerPing.PlayerInfo("§7Discord: §c§odiscord.hydroslide.eu", "0"),
                new ServerPing.PlayerInfo("§7Webseite: §c§ohydroslide.eu", "0"),
                new ServerPing.PlayerInfo("    ", "0"),
                new ServerPing.PlayerInfo("§bHydroSlide §7× §eNetzwerk", "0")};
        ping.setPlayers(new ServerPing.Players(SlotConfig.getSlots(), ProxyServer.getInstance().getOnlineCount(), playerinfo));

    }
}
