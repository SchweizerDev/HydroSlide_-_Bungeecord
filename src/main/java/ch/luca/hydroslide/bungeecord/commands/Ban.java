package ch.luca.hydroslide.bungeecord.commands;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import ch.luca.hydroslide.bungeecord.mysql.repository.BanMuteHistoryRepository;
import ch.luca.hydroslide.bungeecord.mysql.repository.BanRepository;
import ch.luca.hydroslide.bungeecord.mysql.repository.TeamStatsRepository;
import ch.luca.hydroslide.bungeecord.uuid.UUIDFetcher;
import de.crafter75.perms.bungee.api.BungeeRankAPI;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Ban extends Command {

    public Ban(String command) {
        super(command);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) sender;
            if (!p.hasPermission("hydroslide.ban")) {
                p.sendMessage(HydroSlide.getInstance().getNoPermission());
                return;
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase(p.getName())) {
                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDu kannst dich selber nicht bestrafen.");
                    return;
                }
                BungeeRankAPI.getRankAsync(args[0], rank -> BungeeRankAPI.getRankAsync(p.getUniqueId(), playerRank -> {
                    if (rank.isHigherEqualsLevel(playerRank)) {
                        for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                            HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                    all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + p.getName() + " §7wurde §cgekickt§7!");
                                    all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gekickt von: §aSystem");
                                    all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6Versuchtes Bannen eines Administrator/Inhaber");
                                }
                            });
                        }
                        p.disconnect("Du wurdest vom §bHydroSlide §7Netzwerk gekickt! \n§7Grund: §6Versuchtes Bannen eines Administrator/Inhaber");
                        return;
                    }
                    String name = args[0];
                    HydroSlide.getPlayerInfoRepository().getUUID(name, uuid -> {
                        HydroSlide.getPlayerInfoRepository().getBanPoints(uuid, banPoints -> {
                            try {
                                if (!BanRepository.getIsBanned(UUIDFetcher.getUUID(name).toString())) {
                                    if (args[1].equalsIgnoreCase("1")) {
                                        if (banPoints == 0) {
                                            BanRepository.ban(UUIDFetcher.getUUID(name).toString(), name, "Hacking", 2592000, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.BAN, uuid, name, "Hacking", p.getName());
                                            HydroSlide.getTeamStatsRepository().addBans(p.getUniqueId(), 1);
                                            HydroSlide.getPlayerInfoRepository().addBanPoints(uuid, 50);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgebannt§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + BanRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §b30 Tage");
                                                    }
                                                });
                                            }
                                        } else if (banPoints == 50) {
                                            BanRepository.ban(UUIDFetcher.getUUID(name).toString(), name, "Hacking", 5184000, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.BAN, uuid, name, "Hacking", p.getName());
                                            HydroSlide.getTeamStatsRepository().addBans(p.getUniqueId(), 1);
                                            HydroSlide.getPlayerInfoRepository().addBanPoints(uuid, 50);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgebannt§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + BanRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §b60 Tage");
                                                    }
                                                });
                                            }
                                        } else if (banPoints >= 100) {
                                            BanRepository.ban(UUIDFetcher.getUUID(name).toString(), name, "Hacking", -1, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.BAN, uuid, name, "Hacking", p.getName());
                                            HydroSlide.getTeamStatsRepository().addBans(p.getUniqueId(), 1);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgebannt§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + BanRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §4Permanent");
                                                    }
                                                });
                                            }
                                        }
                                    } else if (args[1].equalsIgnoreCase("2")) {
                                        //Abfrage
                                        if (banPoints == 0) {
                                            BanRepository.ban(UUIDFetcher.getUUID(name).toString(), name, "Bugusing", 1209600, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.BAN, uuid, name, "Bugusing", p.getName());
                                            HydroSlide.getTeamStatsRepository().addBans(p.getUniqueId(), 1);
                                            HydroSlide.getPlayerInfoRepository().addBanPoints(uuid, 50);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgebannt§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + BanRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §b14 Tage");
                                                    }
                                                });
                                            }
                                        } else if (banPoints == 50) {
                                            BanRepository.ban(UUIDFetcher.getUUID(name).toString(), name, "Bugusing", 2419200, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.BAN, uuid, name, "Bugusing", p.getName());
                                            HydroSlide.getTeamStatsRepository().addBans(p.getUniqueId(), 1);
                                            HydroSlide.getPlayerInfoRepository().addBanPoints(uuid, 50);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgebannt§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + BanRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §b28 Tage");
                                                    }
                                                });
                                            }
                                        } else if (banPoints >= 100) {
                                            BanRepository.ban(UUIDFetcher.getUUID(name).toString(), name, "Bugusing", -1, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.BAN, uuid, name, "Bugusing", p.getName());
                                            HydroSlide.getTeamStatsRepository().addBans(p.getUniqueId(), 1);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgebannt§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + BanRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §4Permanent");
                                                    }
                                                });
                                            }
                                        }
                                    } else if (args[1].equalsIgnoreCase("3")) {
                                        if (banPoints == 0) {
                                            BanRepository.ban(UUIDFetcher.getUUID(name).toString(), name, "Werbung", 10368000, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.BAN, uuid, name, "Werbung", p.getName());
                                            HydroSlide.getTeamStatsRepository().addBans(p.getUniqueId(), 1);
                                            HydroSlide.getPlayerInfoRepository().addBanPoints(uuid, 50);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgebannt§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + BanRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §b120 Tage");
                                                    }
                                                });
                                            }
                                        } else if (banPoints == 50) {
                                            BanRepository.ban(UUIDFetcher.getUUID(name).toString(), name, "Werbung", 20736000, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.BAN, uuid, name, "Werbung", p.getName());
                                            HydroSlide.getTeamStatsRepository().addBans(p.getUniqueId(), 1);
                                            HydroSlide.getPlayerInfoRepository().addBanPoints(uuid, 50);
                                            //TeamMessage
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgebannt§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + BanRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §b240 Tage");
                                                    }
                                                });
                                            }
                                        } else if (banPoints >= 100) {
                                            BanRepository.ban(UUIDFetcher.getUUID(name).toString(), name, "Werbung", -1, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.BAN, uuid, name, "Werbung", p.getName());
                                            HydroSlide.getTeamStatsRepository().addBans(p.getUniqueId(), 1);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgebannt§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + BanRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §4Permanent");
                                                    }
                                                });
                                            }
                                        }
                                    } else if (args[1].equalsIgnoreCase("4")) {
                                        if (banPoints == 0) {
                                            BanRepository.ban(UUIDFetcher.getUUID(name).toString(), name, "Anstoeßiger-Skin", 86400, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.BAN, uuid, name, "Anstoeßiger-Skin", p.getName());
                                            HydroSlide.getTeamStatsRepository().addBans(p.getUniqueId(), 1);
                                            HydroSlide.getPlayerInfoRepository().addBanPoints(uuid, 50);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgebannt§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + BanRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §b1 Tag");
                                                    }
                                                });
                                            }
                                        } else if (banPoints == 50) {
                                            BanRepository.ban(UUIDFetcher.getUUID(name).toString(), name, "Anstoeßiger-Skin", 172800, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.BAN, uuid, name, "Anstoeßiger-Skin", p.getName());
                                            HydroSlide.getTeamStatsRepository().addBans(p.getUniqueId(), 1);
                                            HydroSlide.getPlayerInfoRepository().addBanPoints(uuid, 50);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgebannt§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + BanRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §b2 Tage");
                                                    }
                                                });
                                            }
                                        } else if (banPoints >= 100) {
                                            BanRepository.ban(UUIDFetcher.getUUID(name).toString(), name, "Anstoeßiger-Skin", -1, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.BAN, uuid, name, "Anstoeßiger-Skin", p.getName());
                                            HydroSlide.getTeamStatsRepository().addBans(p.getUniqueId(), 1);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgebannt§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + BanRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §4Permanent");
                                                    }
                                                });
                                            }
                                        }
                                    } else if (args[1].equalsIgnoreCase("5")) {
                                        if (banPoints == 0) {
                                            BanRepository.ban(UUIDFetcher.getUUID(name).toString(), name, "X-Ray", 604800, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.BAN, uuid, name, "X-Ray", p.getName());
                                            HydroSlide.getTeamStatsRepository().addBans(p.getUniqueId(), 1);
                                            HydroSlide.getPlayerInfoRepository().addBanPoints(uuid, 50);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgebannt§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + BanRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §b7 Tage");
                                                    }
                                                });
                                            }
                                        } else if (banPoints == 50) {
                                            BanRepository.ban(UUIDFetcher.getUUID(name).toString(), name, "X-Ray", 1209600, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.BAN, uuid, name, "X-Ray", p.getName());
                                            HydroSlide.getTeamStatsRepository().addBans(p.getUniqueId(), 1);
                                            HydroSlide.getPlayerInfoRepository().addBanPoints(uuid, 50);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgebannt§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + BanRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §b14 Tage");
                                                    }
                                                });
                                            }
                                        } else if (banPoints >= 100) {
                                            BanRepository.ban(UUIDFetcher.getUUID(name).toString(), name, "X-Ray", -1, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.BAN, uuid, name, "X-Ray", p.getName());
                                            HydroSlide.getTeamStatsRepository().addBans(p.getUniqueId(), 1);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgebannt§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + BanRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §4Permanent");
                                                    }
                                                });
                                            }
                                        }
                                    } else if (args[1].equalsIgnoreCase("6")) {
                                        if (banPoints == 0) {
                                            BanRepository.ban(UUIDFetcher.getUUID(name).toString(), name, "Teaming", 604800, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.BAN, uuid, name, "Teaming", p.getName());
                                            HydroSlide.getTeamStatsRepository().addBans(p.getUniqueId(), 1);
                                            HydroSlide.getPlayerInfoRepository().addBanPoints(uuid, 50);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgebannt§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + BanRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §b7 Tage");
                                                    }
                                                });
                                            }
                                        } else if (banPoints == 50) {
                                            BanRepository.ban(UUIDFetcher.getUUID(name).toString(), name, "Teaming", 1209600, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.BAN, uuid, name, "Teaming", p.getName());
                                            HydroSlide.getTeamStatsRepository().addBans(p.getUniqueId(), 1);
                                            HydroSlide.getPlayerInfoRepository().addBanPoints(uuid, 50);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgebannt§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + BanRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §b14 Tage");
                                                    }
                                                });
                                            }
                                        } else if (banPoints >= 100) {
                                            BanRepository.ban(UUIDFetcher.getUUID(name).toString(), name, "Teaming", -1, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.BAN, uuid, name, "Teaming", p.getName());
                                            HydroSlide.getTeamStatsRepository().addBans(p.getUniqueId(), 1);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgebannt§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + BanRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §4Permanent");
                                                    }
                                                });
                                            }
                                        }
                                    } else if (args[1].equalsIgnoreCase("7")) {
                                        if (banPoints == 0) {
                                            BanRepository.ban(UUIDFetcher.getUUID(name).toString(), name, "Anstoeßiges-Bauwerk", 345600, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.BAN, uuid, name, "Anstoeßiges-Bauwerk", p.getName());
                                            HydroSlide.getTeamStatsRepository().addBans(p.getUniqueId(), 1);
                                            HydroSlide.getPlayerInfoRepository().addBanPoints(uuid, 50);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgebannt§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + BanRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §b4 Tage");
                                                    }
                                                });
                                            }
                                        } else if (banPoints == 50) {
                                            BanRepository.ban(UUIDFetcher.getUUID(name).toString(), name, "Anstoeßiges-Bauwerk", 691200, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.BAN, uuid, name, "Anstoeßiges-Bauwerk", p.getName());
                                            HydroSlide.getTeamStatsRepository().addBans(p.getUniqueId(), 1);
                                            HydroSlide.getPlayerInfoRepository().addBanPoints(uuid, 50);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgebannt§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + BanRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §b8 Tage");
                                                    }
                                                });
                                            }
                                        } else if (banPoints >= 100) {
                                            BanRepository.ban(UUIDFetcher.getUUID(name).toString(), name, "Anstoeßiges-Bauwerk", -1, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.BAN, uuid, name, "Anstoeßiges-Bauwerk", p.getName());
                                            HydroSlide.getTeamStatsRepository().addBans(p.getUniqueId(), 1);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgebannt§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + BanRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §4Permanent");
                                                    }
                                                });
                                            }
                                        }
                                    } else if (args[1].equalsIgnoreCase("8")) {
                                        if (banPoints == 0) {
                                            BanRepository.ban(UUIDFetcher.getUUID(name).toString(), name, "Statsboosting", 345600, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.BAN, uuid, name, "Statsboosting", p.getName());
                                            HydroSlide.getTeamStatsRepository().addBans(p.getUniqueId(), 1);
                                            HydroSlide.getPlayerInfoRepository().addBanPoints(uuid, 50);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgebannt§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + BanRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §b4 Tage");
                                                    }
                                                });
                                            }
                                        } else if (banPoints == 50) {
                                            BanRepository.ban(UUIDFetcher.getUUID(name).toString(), name, "Statsboosting", 691200, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.BAN, uuid, name, "Statsboosting", p.getName());
                                            HydroSlide.getTeamStatsRepository().addBans(p.getUniqueId(), 1);
                                            HydroSlide.getPlayerInfoRepository().addBanPoints(uuid, 50);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgebannt§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + BanRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §b8 Tage");
                                                    }
                                                });
                                            }
                                        } else if (banPoints >= 100) {
                                            BanRepository.ban(UUIDFetcher.getUUID(name).toString(), name, "Statsboosting", -1, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.BAN, uuid, name, "Statsboosting", p.getName());
                                            HydroSlide.getTeamStatsRepository().addBans(p.getUniqueId(), 1);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgebannt§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + BanRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §4Permanent");
                                                    }
                                                });
                                            }
                                        }
                                    } else if (args[1].equalsIgnoreCase("9")) {
                                        if (banPoints == 0) {
                                            BanRepository.ban(UUIDFetcher.getUUID(name).toString(), name, "Supportausnutzung", 86400, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.BAN, uuid, name, "Supportausnutzung", p.getName());
                                            HydroSlide.getTeamStatsRepository().addBans(p.getUniqueId(), 1);
                                            HydroSlide.getPlayerInfoRepository().addBanPoints(uuid, 50);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgebannt§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + BanRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §b1 Tag");
                                                    }
                                                });
                                            }
                                        } else if (banPoints == 50) {
                                            BanRepository.ban(UUIDFetcher.getUUID(name).toString(), name, "Supportausnutzung", 259200, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.BAN, uuid, name, "Supportausnutzung", p.getName());
                                            HydroSlide.getTeamStatsRepository().addBans(p.getUniqueId(), 1);
                                            HydroSlide.getPlayerInfoRepository().addBanPoints(uuid, 50);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgebannt§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + BanRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §b3 Tage");
                                                    }
                                                });
                                            }
                                        } else if (banPoints >= 100) {
                                            BanRepository.ban(UUIDFetcher.getUUID(name).toString(), name, "Supportausnutzung", 2592000, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.BAN, uuid, name, "Supportausnutzung", p.getName());
                                            HydroSlide.getTeamStatsRepository().addBans(p.getUniqueId(), 1);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgebannt§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + BanRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §b30 Tage");
                                                    }
                                                });
                                            }
                                        }
                                    } else if (args[1].equalsIgnoreCase("10")) {
                                        if (banPoints == 0) {
                                            BanRepository.ban(UUIDFetcher.getUUID(name).toString(), name, "Reportausnutzung", 86400, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.BAN, uuid, name, "Reportausnutzung", p.getName());
                                            HydroSlide.getTeamStatsRepository().addBans(p.getUniqueId(), 1);
                                            HydroSlide.getPlayerInfoRepository().addBanPoints(uuid, 50);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgebannt§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + BanRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §b1 Tag");
                                                    }
                                                });
                                            }
                                        } else if (banPoints == 50) {
                                            BanRepository.ban(UUIDFetcher.getUUID(name).toString(), name, "Reportausnutzung", 259200, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.BAN, uuid, name, "Reportausnutzung", p.getName());
                                            HydroSlide.getTeamStatsRepository().addBans(p.getUniqueId(), 1);
                                            HydroSlide.getPlayerInfoRepository().addBanPoints(uuid, 50);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgebannt§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + BanRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §b3 Tage");
                                                    }
                                                });
                                            }
                                        } else if (banPoints >= 100) {
                                            BanRepository.ban(UUIDFetcher.getUUID(name).toString(), name, "Reportausnutzung", 2592000, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.BAN, uuid, name, "Reportausnutzung", p.getName());
                                            HydroSlide.getTeamStatsRepository().addBans(p.getUniqueId(), 1);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgebannt§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + BanRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §b30 Tage");
                                                    }
                                                });
                                            }
                                        }
                                    } else if (args[1].equalsIgnoreCase("11")) {
                                        BanRepository.ban(UUIDFetcher.getUUID(name).toString(), name, "Crashversuch", -1, p.getName());
                                        HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.BAN, uuid, name, "Crashversuch", p.getName());
                                        HydroSlide.getTeamStatsRepository().addBans(p.getUniqueId(), 1);
                                        for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                            HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                    all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgebannt§7!");
                                                    all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                    all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + BanRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                    all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §4Permanent");
                                                }
                                            });
                                        }
                                    } else if (args[1].equalsIgnoreCase("12")) {
                                        BanRepository.ban(UUIDFetcher.getUUID(name).toString(), name, "Banumgehung", -1, p.getName());
                                        HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.BAN, uuid, name, "Banumgehung", p.getName());
                                        HydroSlide.getTeamStatsRepository().addBans(p.getUniqueId(), 1);
                                        for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                            HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                    all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgebannt§7!");
                                                    all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                    all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + BanRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                    all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §4Permanent");
                                                }
                                            });
                                        }
                                    } else if (args[1].equalsIgnoreCase("13")) {
                                        if (p.hasPermission("hydroslide.admin")) {
                                            BanRepository.ban(UUIDFetcher.getUUID(name).toString(), name, "Hausverbot", -1, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.BAN, uuid, name, "Hausverbot", p.getName());
                                            HydroSlide.getTeamStatsRepository().addBans(p.getUniqueId(), 1);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgebannt§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + BanRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §4Permanent");
                                                    }
                                                });
                                            }
                                        } else {
                                            p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDieser Bangrund ist für dich gesperrt.");
                                        }
                                    } else {
                                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "Hacking §b➼ §e1");
                                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "Bugusing §b➼ §e2");
                                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "Werbung §b➼ §e3");
                                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "Anstoeßiger-Skin §b➼ §e4");
                                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "X-Ray §b➼ §e5");
                                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "Teaming §b➼ §e6");
                                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "Anstoeßiges-Bauwerk §b➼ §e7");
                                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "Statsboosting §b➼ §e8");
                                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "Supportausnutzung §b➼ §e9");
                                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "Reportausnutzung §b➼ §e10");
                                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "Crashversuch §b➼ §e11");
                                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "Banumgehung §b➼ §e12");
                                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "Hausverbot §b➼ §e13");
                                    }
                                } else {
                                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDieser Spieler ist bereits gebannt.");
                                }
                            } catch (Exception ex) {
                                p.sendMessage(HydroSlide.getInstance().getPlayerNeverOnline());
                            }
                        });
                    });
                }));
            } else {
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "Hacking §b➼ §e1");
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "Bugusing §b➼ §e2");
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "Werbung §b➼ §e3");
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "Anstoeßiger-Skin §b➼ §e4");
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "X-Ray §b➼ §e5");
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "Teaming §b➼ §e6");
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "Anstoeßiges-Bauwerk §b➼ §e7");
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "Statsboosting §b➼ §e8");
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "Supportausnutzung §b➼ §e9");
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "Reportausnutzung §b➼ §e10");
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "Crashversuch §b➼ §e11");
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "Banumgehung §b➼ §e12");
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "Hausverbot §b➼ §e13");
            }
        } else {
            if (args.length == 2) {
                String name = args[0];
                HydroSlide.getPlayerInfoRepository().getUUID(name, uuid -> {
                    if (!BanRepository.getIsBanned(UUIDFetcher.getUUID(name).toString())) {
                        if (args[1].equalsIgnoreCase("1")) {
                            BanRepository.ban(UUIDFetcher.getUUID(name).toString(), name, "Hausverbot", -1, "System");
                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.BAN, uuid, name, "Hausverbot", "System");
                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgebannt§7!");
                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §aSystem");
                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + BanRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §4Permanent");
                                    }
                                });
                            }
                        } else {
                            sender.sendMessage(HydroSlide.getInstance().getPrefix() + "Hausverbot §b➼ §e1");
                        }
                    } else {
                        sender.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDieser Spieler ist bereits gebannt.");
                    }
                });
            } else {
                sender.sendMessage(HydroSlide.getInstance().getPrefix() + "Hausverbot §b➼ §e1");
            }
        }
    }
}
