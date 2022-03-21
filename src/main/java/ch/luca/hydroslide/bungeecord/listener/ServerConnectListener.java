package ch.luca.hydroslide.bungeecord.listener;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import ch.luca.hydroslide.bungeecord.config.SlotConfig;
import ch.luca.hydroslide.bungeecord.mysql.repository.BanRepository;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ServerConnectListener implements Listener {

    @EventHandler
    public void onServerConnectMotd(ServerConnectEvent e) {
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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onServerConnect(ServerConnectEvent e) {
        ProxiedPlayer p = e.getPlayer();
        if (BanRepository.getIsBanned(p.getUniqueId().toString())) {
            long current = System.currentTimeMillis();
            long end = BanRepository.getEnd(p.getUniqueId().toString()).longValue();
            if (((current < end ? 1 : 0) | (end == -1L ? 1 : 0)) != 0) {
                e.setCancelled(true);
                p.disconnect("§cDu wurdest von §bHydroSlide.eu §cgebannt!\n\n§7Grund: §6" + BanRepository.getReason(p.getUniqueId().toString()) +
                        "\n§7Verbleibende Zeit: §b" + BanRepository.getReamainingTime(p.getUniqueId().toString()) +
                        " \n\n§7Du kannst einen Entbannungsantrag auf §ewww.hydroslide.eu §7schreiben.");
            } else {
                BanRepository.unban(p.getUniqueId().toString());
                HydroSlide.getLogTeamRepository().getIsActivated(p.getUniqueId(), activated -> {
                    for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                        if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                            all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + p.getName() + " §7wurde §aentbannt§7.");
                            all.sendMessage(HydroSlide.getInstance().getPrefix() + "Die Zeit der Strafe wurde abgesessen.");
                        }
                    }
                });
            }
        }
        if(ProxyServer.getInstance().getOnlineCount() >= SlotConfig.getSlots()) {
            if(!p.hasPermission("hydroslide.premiumjoin")) {
                p.disconnect("§cDas Netzwerk ist voll! \n§7Du willst dennoch spielen? Besuche dafür unseren Shop. \n\n§7Store: §estore.cubeslide.net");
            }
        }
    }
}
