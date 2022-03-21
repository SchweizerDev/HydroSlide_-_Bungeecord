package ch.luca.hydroslide.bungeecord.commands;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import ch.luca.hydroslide.bungeecord.mysql.repository.BanMuteHistoryRepository;
import ch.luca.hydroslide.bungeecord.mysql.repository.MuteRepository;
import ch.luca.hydroslide.bungeecord.uuid.UUIDFetcher;
import de.crafter75.perms.bungee.api.BungeeRankAPI;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Mute extends Command {

    public Mute(String command) {
        super(command);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) sender;
            if (!p.hasPermission("hydroslide.mute")) {
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
                                    all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6Versuchtes Muten eines Administrator/Inhaber");
                                }
                            });
                        }
                        p.disconnect("Du wurdest vom §bHydroSlide §7Netzwerk gekickt! \n§7Grund: §6Versuchtes Bannen eines Administrator/Inhaber");
                        return;
                    }
                    String name = args[0];
                    HydroSlide.getPlayerInfoRepository().getUUID(name, uuid -> {
                        HydroSlide.getPlayerInfoRepository().getMutePoints(uuid, mutePoints -> {
                            try {
                                if (!MuteRepository.getIsMuted(UUIDFetcher.getUUID(name).toString())) {
                                    if (args[1].equalsIgnoreCase("1")) {
                                        if (mutePoints == 0) {
                                            MuteRepository.mute(UUIDFetcher.getUUID(name).toString(), name, "Chatverhalten", 3600, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.MUTE, uuid, name, "Chatverhalten", p.getName());
                                            HydroSlide.getTeamStatsRepository().addMutes(p.getUniqueId(), 1);
                                            HydroSlide.getPlayerInfoRepository().addMutePoints(uuid, 20);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgemutet§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + MuteRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §b1 Stunde");
                                                    }
                                                });
                                            }
                                        } else if (mutePoints == 20) {
                                            MuteRepository.mute(UUIDFetcher.getUUID(name).toString(), name, "Chatverhalten", 21600, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.MUTE, uuid, name, "Chatverhalten", p.getName());
                                            HydroSlide.getTeamStatsRepository().addMutes(p.getUniqueId(), 1);
                                            HydroSlide.getPlayerInfoRepository().addMutePoints(uuid, 20);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgemutet§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + MuteRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §b6 Stunden");
                                                    }
                                                });
                                            }
                                        } else if (mutePoints == 40) {
                                            //Mute
                                            MuteRepository.mute(UUIDFetcher.getUUID(name).toString(), name, "Chatverhalten", 86400, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.MUTE, uuid, name, "Chatverhalten", p.getName());
                                            HydroSlide.getTeamStatsRepository().addMutes(p.getUniqueId(), 1);
                                            HydroSlide.getPlayerInfoRepository().addMutePoints(uuid, 20);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgemutet§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + MuteRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §b1 Tag");
                                                    }
                                                });
                                            }
                                        } else if (mutePoints == 60) {
                                            //Mute
                                            MuteRepository.mute(UUIDFetcher.getUUID(name).toString(), name, "Chatverhalten", 604800, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.MUTE, uuid, name, "Chatverhalten", p.getName());
                                            HydroSlide.getTeamStatsRepository().addMutes(p.getUniqueId(), 1);
                                            HydroSlide.getPlayerInfoRepository().addMutePoints(uuid, 20);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgemutet§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + MuteRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §b7 Tage");
                                                    }
                                                });
                                            }
                                        } else if (mutePoints == 80) {
                                            MuteRepository.mute(UUIDFetcher.getUUID(name).toString(), name, "Chatverhalten", 2592000, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.MUTE, uuid, name, "Chatverhalten", p.getName());
                                            HydroSlide.getTeamStatsRepository().addMutes(p.getUniqueId(), 1);
                                            HydroSlide.getPlayerInfoRepository().addMutePoints(uuid, 20);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgemutet§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + MuteRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §b30 Tage");
                                                    }
                                                });
                                            }
                                        } else if (mutePoints >= 100) {
                                            MuteRepository.mute(UUIDFetcher.getUUID(name).toString(), name, "Chatverhalten", 5184000, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.MUTE, uuid, name, "Chatverhalten", p.getName());
                                            HydroSlide.getTeamStatsRepository().addMutes(p.getUniqueId(), 1);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgemutet§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + MuteRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §b60 Tage");
                                                    }
                                                });
                                            }
                                        }
                                    } else if (args[1].equalsIgnoreCase("2")) {
                                        if (mutePoints == 0) {
                                            MuteRepository.mute(UUIDFetcher.getUUID(name).toString(), name, "Sexismus", 3600, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.MUTE, uuid, name, "Sexismus", p.getName());
                                            HydroSlide.getTeamStatsRepository().addMutes(p.getUniqueId(), 1);
                                            HydroSlide.getPlayerInfoRepository().addMutePoints(uuid, 20);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgemutet§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + MuteRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §b1 Stunde");
                                                    }
                                                });
                                            }
                                        } else if (mutePoints == 20) {
                                            MuteRepository.mute(UUIDFetcher.getUUID(name).toString(), name, "Sexismus", 21600, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.MUTE, uuid, name, "Sexismus", p.getName());
                                            HydroSlide.getPlayerInfoRepository().addMutePoints(uuid, 20);
                                            HydroSlide.getTeamStatsRepository().addMutes(p.getUniqueId(), 1);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgemutet§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + MuteRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §b6 Stunden");
                                                    }
                                                });
                                            }
                                        } else if (mutePoints == 40) {
                                            MuteRepository.mute(UUIDFetcher.getUUID(name).toString(), name, "Sexismus", 86400, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.MUTE, uuid, name, "Sexismus", p.getName());
                                            HydroSlide.getPlayerInfoRepository().addMutePoints(uuid, 20);
                                            HydroSlide.getTeamStatsRepository().addMutes(p.getUniqueId(), 1);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgemutet§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + MuteRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §b1 Tag");
                                                    }
                                                });
                                            }
                                        } else if (mutePoints == 60) {
                                            MuteRepository.mute(UUIDFetcher.getUUID(name).toString(), name, "Sexismus", 604800, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.MUTE, uuid, name, "Sexismus", p.getName());
                                            HydroSlide.getPlayerInfoRepository().addMutePoints(uuid, 20);
                                            HydroSlide.getTeamStatsRepository().addMutes(p.getUniqueId(), 1);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgemutet§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + MuteRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §b7 Tage");
                                                    }
                                                });
                                            }
                                        } else if (mutePoints == 80) {
                                            MuteRepository.mute(UUIDFetcher.getUUID(name).toString(), name, "Sexismus", 2592000, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.MUTE, uuid, name, "Sexismus", p.getName());
                                            HydroSlide.getPlayerInfoRepository().addMutePoints(uuid, 20);
                                            HydroSlide.getTeamStatsRepository().addMutes(p.getUniqueId(), 1);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgemutet§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + MuteRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §b30 Tage");
                                                    }
                                                });
                                            }
                                        } else if (mutePoints >= 100) {
                                            MuteRepository.mute(UUIDFetcher.getUUID(name).toString(), name, "Sexismus", 5184000, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.MUTE, uuid, name, "Sexismus", p.getName());
                                            HydroSlide.getTeamStatsRepository().addMutes(p.getUniqueId(), 1);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgemutet§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + MuteRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §b60 Tage");
                                                    }
                                                });
                                            }
                                        }
                                    } else if (args[1].equalsIgnoreCase("3")) {
                                        if (mutePoints == 0) {
                                            MuteRepository.mute(UUIDFetcher.getUUID(name).toString(), name, "Rassismus", 3600, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.MUTE, uuid, name, "Rassismus", p.getName());
                                            HydroSlide.getPlayerInfoRepository().addMutePoints(uuid, 20);
                                            HydroSlide.getTeamStatsRepository().addMutes(p.getUniqueId(), 1);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgemutet§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + MuteRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §b1 Stunde");
                                                    }
                                                });
                                            }
                                        } else if (mutePoints == 20) {
                                            MuteRepository.mute(UUIDFetcher.getUUID(name).toString(), name, "Rassismus", 21600, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.MUTE, uuid, name, "Rassismus", p.getName());
                                            HydroSlide.getPlayerInfoRepository().addMutePoints(uuid, 20);
                                            HydroSlide.getTeamStatsRepository().addMutes(p.getUniqueId(), 1);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgemutet§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + MuteRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §b6 Stunden");
                                                    }
                                                });
                                            }
                                        } else if (mutePoints == 40) {
                                            MuteRepository.mute(UUIDFetcher.getUUID(name).toString(), name, "Rassismus", 86400, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.MUTE, uuid, name, "Rassismus", p.getName());
                                            HydroSlide.getPlayerInfoRepository().addMutePoints(uuid, 20);
                                            HydroSlide.getTeamStatsRepository().addMutes(p.getUniqueId(), 1);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgemutet§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + MuteRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §b1 Tag");
                                                    }
                                                });
                                            }
                                        } else if (mutePoints == 60) {
                                            MuteRepository.mute(UUIDFetcher.getUUID(name).toString(), name, "Rassismus", 604800, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.MUTE, uuid, name, "Rassismus", p.getName());
                                            HydroSlide.getPlayerInfoRepository().addMutePoints(uuid, 20);
                                            HydroSlide.getTeamStatsRepository().addMutes(p.getUniqueId(), 1);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgemutet§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + MuteRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §b7 Tage");
                                                    }
                                                });
                                            }
                                        } else if (mutePoints == 80) {
                                            MuteRepository.mute(UUIDFetcher.getUUID(name).toString(), name, "Rassismus", 2592000, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.MUTE, uuid, name, "Rassismus", p.getName());
                                            HydroSlide.getPlayerInfoRepository().addMutePoints(uuid, 20);
                                            HydroSlide.getTeamStatsRepository().addMutes(p.getUniqueId(), 1);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgemutet§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + MuteRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §b30 Tage");
                                                    }
                                                });
                                            }
                                        } else if (mutePoints >= 100) {
                                            MuteRepository.mute(UUIDFetcher.getUUID(name).toString(), name, "Rassismus", 5184000, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.MUTE, uuid, name, "Rassismus", p.getName());
                                            HydroSlide.getTeamStatsRepository().addMutes(p.getUniqueId(), 1);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgemutet§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + MuteRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §b60 Tage");
                                                    }
                                                });
                                            }
                                        }
                                    } else if (args[1].equalsIgnoreCase("4")) {
                                        MuteRepository.mute(UUIDFetcher.getUUID(name).toString(), name, "Muteumgehung", 2592000, p.getName());
                                        HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.MUTE, uuid, name, "Muteumgehung", p.getName());
                                        HydroSlide.getPlayerInfoRepository().addMutePoints(uuid, 20);
                                        HydroSlide.getTeamStatsRepository().addMutes(p.getUniqueId(), 1);
                                        for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                            HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                    all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgemutet§7!");
                                                    all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                    all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + MuteRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                    all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §b30 Tage");
                                                }
                                            });
                                        }
                                    } else if (args[1].equalsIgnoreCase("5")) {
                                        if (p.hasPermission("cubeslide.admin")) {
                                            MuteRepository.mute(UUIDFetcher.getUUID(name).toString(), name, "Hausverbot", -1, p.getName());
                                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.MUTE, uuid, name, "Hausverbot", p.getName());
                                            HydroSlide.getPlayerInfoRepository().addMutePoints(uuid, 20);
                                            HydroSlide.getTeamStatsRepository().addMutes(p.getUniqueId(), 1);
                                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgemutet§7!");
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + p.getName());
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + MuteRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §4Permamanet");
                                                    }
                                                });
                                            }
                                        } else {
                                            p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDieser Mutegrund ist für dich gesperrt.");
                                        }
                                    } else {
                                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "Chatverhalten §b➼ §e1");
                                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "Sexismus §b➼ §e2");
                                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "Rassismus §b➼ §e3");
                                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "Muteumgehung §b➼ §e4");
                                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "Hausverbot §b➼ §e5");
                                    }
                                } else {
                                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDieser Spieler ist bereits gemutet.");
                                }
                            } catch (Exception ex) {
                                p.sendMessage(HydroSlide.getInstance().getPlayerNeverOnline());
                            }
                        });
                    });
                }));
            } else {
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "Chatverhalten §b➼ §e1");
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "Sexismus §b➼ §e2");
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "Rassismus §b➼ §e3");
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "Muteumgehung §b➼ §e4");
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "Hausverbot §b➼ §e5");
            }
        } else {
            if (args.length == 2) {
                String name = args[0];
                HydroSlide.getPlayerInfoRepository().getUUID(name, uuid -> {
                    if (!MuteRepository.getIsMuted(UUIDFetcher.getUUID(name).toString())) {
                        if (args[1].equalsIgnoreCase("1")) {
                            MuteRepository.mute(UUIDFetcher.getUUID(name).toString(), name, "Hausverbot", -1, "System");
                            HydroSlide.getBanMuteHistoryRepository().addHistory(BanMuteHistoryRepository.HistoryType.MUTE, uuid, name, "Hausverbot", "System");
                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §cgemutet§7!");
                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §aSystem");
                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + MuteRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Dauer: §4Permamanet");
                                    }
                                });
                            }
                        } else {
                            sender.sendMessage(HydroSlide.getInstance().getPrefix() + "Hausverbot §b➼ §e5");
                        }
                    } else {
                        sender.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDieser Spieler ist bereits gemutet.");
                    }
                });
            } else {
                sender.sendMessage(HydroSlide.getInstance().getPrefix() + "Hausverbot §b➼ §e5");
            }
        }
    }
}