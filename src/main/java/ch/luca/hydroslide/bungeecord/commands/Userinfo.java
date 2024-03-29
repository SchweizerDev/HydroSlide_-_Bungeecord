package ch.luca.hydroslide.bungeecord.commands;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import ch.luca.hydroslide.bungeecord.mysql.repository.BanRepository;
import ch.luca.hydroslide.bungeecord.mysql.repository.MuteRepository;
import ch.luca.hydroslide.bungeecord.mysql.repository.OnlineTimeRepository;
import ch.luca.hydroslide.bungeecord.uuid.UUIDFetcher;
import de.crafter75.perms.bungee.api.BungeeRankAPI;
import de.crafter75.perms.global.rank.Rank;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Userinfo extends Command {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy 'um' HH:mm:ss 'Uhr'");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy 'um' HH:mm 'Uhr'");

    public Userinfo(String command) {
        super(command);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(HydroSlide.getInstance().getNoPlayer());
            return;
        }
        ProxiedPlayer p = (ProxiedPlayer) sender;
        if (!p.hasPermission("hydroslide.userinfo")) {
            p.sendMessage(HydroSlide.getInstance().getNoPermission());
            return;
        }
        if (args.length == 1) {
            ProxyServer.getInstance().getScheduler().runAsync(HydroSlide.getInstance(), () -> {
                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
                String name = args[0];
                HydroSlide.getPlayerInfoRepository().getUUID(name, uuid -> {
                    if (uuid == null) {
                        p.sendMessage(HydroSlide.getInstance().getPlayerNeverOnline());
                        return;
                    }
                    Rank rank = BungeeRankAPI.getRankSync(UUIDFetcher.getUUID(args[0]));
                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "Wird geladen...");
                    HydroSlide.getPlayerInfoRepository().getBanPoints(uuid, banPoints -> {
                        HydroSlide.getPlayerInfoRepository().getMutePoints(uuid, mutePoints -> {
                            HydroSlide.getPlayerInfoRepository().getFirstJoin(uuid, date -> {
                                HydroSlide.getLogTeamRepository().getIsActivated(uuid, activated -> {
                                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "Name: §e" + name);
                                    if (ProxyServer.getInstance().getPlayer(name) != null) {
                                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "Ping: §6" + target.getPing());
                                    } else {
                                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "Ping: §c-");
                                    }
                                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "Rang: " + rank.getRankColor() + rank.getRankName());
                                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "Spielzeit: §e" + OnlineTimeRepository.getFormattedPlayTime(UUIDFetcher.getUUID(name)));
                                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "Erstes mal gejoint: §5" + date.format(FORMATTER));
                                    if (ProxyServer.getInstance().getPlayer(name) != null) {
                                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "Status: §aOnline §8(§e" + ProxyServer.getInstance().getPlayer(target.getName()).getServer().getInfo().getName() + "§8)");
                                    } else {
                                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "Status: §cOffline §8(§7Zuletzt online: §c" + DATE_FORMAT.format(HydroSlide.getPlayerInfoRepository().getLastOnline(uuid)) + "§8)");
                                    }
                                    if (activated == 1) {
                                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "TeamSystem: §aaktiviert");
                                    } else if (activated == 0) {
                                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "TeamSystem: §cdeaktiviert");
                                    }
                                    if (BanRepository.getIsBanned(UUIDFetcher.getUUID(name).toString())) {
                                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt: §a✔");
                                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "  Gebannt von: §a" + BanRepository.getBanner(UUIDFetcher.getUUID(name).toString()));
                                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "  Grund: §6" + BanRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "  Verbleibende Zeit: §b" + BanRepository.getReamainingTime(UUIDFetcher.getUUID(name).toString()));
                                    } else {
                                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt: §c✘");
                                    }
                                    if (MuteRepository.getIsMuted(UUIDFetcher.getUUID(name).toString())) {
                                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "Gemutet: §a✔");
                                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "  Gemutet von: §a" + MuteRepository.getMuter(UUIDFetcher.getUUID(name).toString()));
                                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "  Grund: §6" + MuteRepository.getReason(UUIDFetcher.getUUID(name).toString()));
                                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "  Verbleibende Zeit: §b" + MuteRepository.getReamainingTime(UUIDFetcher.getUUID(name).toString()));
                                    } else {
                                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "Gemutet: §c✘");
                                    }
                                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "Banpunkte: §3" + banPoints);
                                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "Mutepunkte: §3" + mutePoints);
                                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "History: §e/history");
                                });
                            });
                        });
                    });
                });
            });
        } else {
            p.sendMessage(HydroSlide.getInstance().getPrefixUse() + "userinfo <Spieler>");
        }
    }
}
