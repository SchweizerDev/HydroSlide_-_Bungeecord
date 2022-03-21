package ch.luca.hydroslide.bungeecord.listener;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import ch.luca.hydroslide.bungeecord.mysql.repository.*;
import de.crafter75.perms.bungee.api.BungeeRankAPI;
import de.crafter75.perms.global.rank.RankManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;

public class PostLoginListener implements Listener {

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        ProxiedPlayer p = event.getPlayer();
        //MySQL
        HydroSlide.getPlayerInfoRepository().updatePlayerInfo(p.getUniqueId(), p.getName(), p.getAddress().getAddress().getHostAddress());
        HydroSlide.getCoinsRepository().updateCoins(p.getUniqueId(), p.getName());
        OnlineTimeRepository.onJoin(p);
        BungeeRankAPI.getRankAsync(p.getUniqueId(), rank -> {
            if(rank.isHigherEqualsLevel(RankManager.getRank("Helpmate", false))) {
                HydroSlide.getLogTeamRepository().updateLogTeam(p.getUniqueId(), p.getName());
                HydroSlide.getTeamStatsRepository().updateTeamStats(p.getUniqueId(), p.getName());
            }
        });

        //JoinMessage
        for (int i = 0; i < 10; i++) {
            p.sendMessage(" ");
        }
        p.sendMessage(HydroSlide.getInstance().getPrefix() + "Willkommen auf §bHydroSlide§7!");
        p.sendMessage(HydroSlide.getInstance().getPrefix() + "Das Team wünscht dir viel Spass. Bei Fragen wende dich gerne an den Support.");

        //TeamMessage
        if(p.hasPermission("hydroslide.team")) {
            HydroSlide.getLogTeamRepository().getIsActivated(p.getUniqueId(), activated -> {
                if(activated == 1) {
                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "Du bist eingeloggt im Teamsystem.");
                    if(HydroSlide.reports.size() == 0) {
                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "Momentan gibt es keine offenen Reports.");
                    } else {
                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "Momentan gibt es §e" + HydroSlide.reports.size() + " §7offene Reports§7.");
                    }
                } else if(activated == 0) {
                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "Du bist ausgeloggt im Teamsystem. Bitte logge dich ein.");
                }
            });
        }
    }

    //ConnectwithBan
    @EventHandler
    public void onConnectwithBan(PostLoginEvent e) {
        final ProxiedPlayer p = e.getPlayer();
        if (!BanRepository.getIsBanned(p.getUniqueId().toString())) {
            ProxyServer.getInstance().getScheduler().runAsync(HydroSlide.getInstance(), new Runnable() {
                @Override
                public void run() {
                    HydroSlide.getPlayerInfoRepository().getUUID(p.getName(), uuid -> {
                        HydroSlide.getPlayerInfoRepository().getIP(uuid, ip -> {
                            List<String> playerNames = BanRepository.getBannedPlayersNamesWithSameIP(ip);
                            if (playerNames.size() > 0) {
                                TextComponent ban = new TextComponent("§7[§aBestrafen§7]");
                                ban.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ban " + p.getName() + " 12"));
                                TextComponent info = new TextComponent("§7[§eUserinfo§7]");
                                info.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/userinfo " + p.getName()));
                                TextComponent accountInfo = new TextComponent("§7[§6Accountinfo§7]");
                                accountInfo.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/accountinfo " + p.getName()));
                                for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                    HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                        if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                            all.sendMessage(HydroSlide.getInstance().getPrefix() + "Ein Spieler wurde auf der gleichen IP-Adresse bereits gebannt!");
                                            all.sendMessage(HydroSlide.getInstance().getPrefix() + "Spieler: §e" + p.getName());
                                            all.sendMessage(new TextComponent(HydroSlide.getInstance().getPrefix() + "§7Funktionen: "), new TextComponent(info), new TextComponent(" §7| "), new TextComponent(ban), new TextComponent(" §7| "), new TextComponent(accountInfo));
                                        }
                                    });
                                }
                            }
                        });
                    });
                }
            });
        }
    }

    @EventHandler
    public void onConnectwithMute(PostLoginEvent e) {
        final ProxiedPlayer p = e.getPlayer();
        if (!MuteRepository.getIsMuted(p.getUniqueId().toString())) {
            ProxyServer.getInstance().getScheduler().runAsync(HydroSlide.getInstance(), new Runnable() {
                @Override
                public void run() {
                    HydroSlide.getPlayerInfoRepository().getUUID(p.getName(), uuid -> {
                        HydroSlide.getPlayerInfoRepository().getIP(uuid, ip -> {
                            List<String> playerNames = MuteRepository.getMutedPlayersNamesWithSameIP(ip);
                            if (playerNames.size() > 0) {
                                TextComponent mute = new TextComponent("§7[§aBestrafen§7]");
                                mute.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mute " + p.getName() + " 4"));
                                TextComponent info = new TextComponent("§7[§eUserinfo§7]");
                                info.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/userinfo " + p.getName()));
                                TextComponent accountInfo = new TextComponent("§7[§6Accountinfo§7]");
                                accountInfo.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/accountinfo " + p.getName()));
                                for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                    HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                        if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                            all.sendMessage(HydroSlide.getInstance().getPrefix() + "Ein Spieler wurde auf der gleichen IP-Adresse bereits gemutet!");
                                            all.sendMessage(HydroSlide.getInstance().getPrefix() + "Spieler: §e" + p.getName());
                                            all.sendMessage(new TextComponent(HydroSlide.getInstance().getPrefix() + "§7Funktionen: "), new TextComponent(info), new TextComponent(" §7| "), new TextComponent(mute), new TextComponent(" §7| "), new TextComponent(accountInfo));
                                        }
                                    });
                                }
                            }
                        });
                    });
                }
            });
        }
    }
}
