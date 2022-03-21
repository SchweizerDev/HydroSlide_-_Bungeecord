package ch.luca.hydroslide.bungeecord.commands;
import ch.luca.hydroslide.bungeecord.HydroSlide;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Reset extends Command {

    public Reset(String command) {
        super(command);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(HydroSlide.getInstance().getNoPlayer());
            return;
        }
        ProxiedPlayer p = (ProxiedPlayer) sender;
        if (!p.hasPermission("hydroslide.reset")) {
            p.sendMessage(HydroSlide.getInstance().getNoPermission());
            return;
        }
        if (args.length == 2) {
            String name = args[0];
            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
            if (args[1].equalsIgnoreCase("teamstats")) {
                HydroSlide.getPlayerInfoRepository().getUUID(name, uuid -> {
                    if (uuid == null) {
                        p.sendMessage(HydroSlide.getInstance().getPlayerNeverOnline());
                        return;
                    }
                    HydroSlide.getTeamStatsRepository().resetBans(uuid);
                    HydroSlide.getTeamStatsRepository().resetKicks(uuid);
                    HydroSlide.getTeamStatsRepository().resetMutes(uuid);
                    HydroSlide.getTeamStatsRepository().resetReports(uuid);
                    HydroSlide.getTeamStatsRepository().resetSupports(uuid);
                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "Du hast die Teamstatistiken von §e" + name + " §7zurückgesetzt.");
                });
            } else if (args[1].equalsIgnoreCase("banpoints")) {
                HydroSlide.getPlayerInfoRepository().getUUID(name, uuid -> {
                    if (uuid == null) {
                        p.sendMessage(HydroSlide.getInstance().getPlayerNeverOnline());
                        return;
                    }
                    HydroSlide.getPlayerInfoRepository().resetBanPoints(uuid);
                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "Du hast die Banpunkte von §e" + name + " §7zurückgesetzt.");
                });
            } else if (args[1].equalsIgnoreCase("mutepoints")) {
                HydroSlide.getPlayerInfoRepository().getUUID(name, uuid -> {
                    if (uuid == null) {
                        p.sendMessage(HydroSlide.getInstance().getPlayerNeverOnline());
                        return;
                    }
                    HydroSlide.getPlayerInfoRepository().resetMutePoints(uuid);
                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "Du hast die Mutepunkte von §e" + name + " §7zurückgesetzt.");
                });
            } else if (args[1].equalsIgnoreCase("coins")) {
                HydroSlide.getPlayerInfoRepository().getUUID(name, uuid -> {
                    if (uuid == null) {
                        p.sendMessage(HydroSlide.getInstance().getPlayerNeverOnline());
                        return;
                    }
                    HydroSlide.getCoinsRepository().resetCoins(uuid);
                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "Du hast die Coins von §e" + target.getName() + " §7zurückgesetzt.");
                });
            } else if (args[1].equalsIgnoreCase("logteam")) {
                    HydroSlide.getPlayerInfoRepository().getUUID(name, uuid -> {
                        if (uuid == null) {
                            p.sendMessage(HydroSlide.getInstance().getPlayerNeverOnline());
                            return;
                        }
                        HydroSlide.getLogTeamRepository().setInactive(uuid);
                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "Du hast §e" + target.getName() + " §7ausgeloggt aus dem Teamsystem.");
                    });
            } else {
                p.sendMessage(HydroSlide.getInstance().getPrefixUse() + "reset <Spieler> <teamstats|banpoins|mutepoints|coins|logteam>");
            }
        } else {
            p.sendMessage(HydroSlide.getInstance().getPrefixUse() + "reset <Spieler> <teamstats|banpoins|mutepoints|coins|logteam>");
        }
    }
}
