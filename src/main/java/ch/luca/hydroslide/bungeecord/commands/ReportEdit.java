package ch.luca.hydroslide.bungeecord.commands;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class ReportEdit extends Command {

    public ReportEdit(String command) {
        super(command);
    }

    @SuppressWarnings("deprecation")
    public void execute(CommandSender sender, final String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(HydroSlide.getInstance().getNoPlayer());
            return;
        }
        ProxiedPlayer p = (ProxiedPlayer) sender;
        if (!p.hasPermission("hydroslide.reportedit")) {
            p.sendMessage(HydroSlide.getInstance().getNoPermission());
            return;
        }
        HashMap<String, String> reports = HydroSlide.reports;
        if (reports.size() <= 0) {
            p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cMomentan gibt es keine offenen Reports.");
            return;
        }
        if (args.length < 2) {
            p.sendMessage(HydroSlide.getInstance().getPrefixUse() + "reportedit <accept|delete> <Spieler>");
            return;
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("accept")) {
                if (!reports.containsKey(args[1])) {
                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cVom angegebenen Spieler gibt es keinen Report.");
                    return;
                }
                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[1]);
                if (!p.getServer().getInfo().getName().equalsIgnoreCase(target.getServer().getInfo().getName())) {
                    p.connect(target.getServer().getInfo());
                    ProxyServer.getInstance().getScheduler().schedule(HydroSlide.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            p.chat("/vanish");
                        }
                    }, 1, TimeUnit.SECONDS);
                } else {
                    p.chat("/vanish");
                }
                ProxiedPlayer targetreported = ProxyServer.getInstance().getPlayer(HydroSlide.reports.get(args[1]));
                if (targetreported != null) {
                    targetreported.sendMessage(HydroSlide.getInstance().getPrefix() + "Ein Teammitglied kümmert sich jetzt um deinen Report.");
                }
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "Du hast den Report von §e" + target.getName() + " §7wegen §6" + HydroSlide.reportreason.get(args[1]) + " §7angenommen.");
                HydroSlide.getTeamStatsRepository().addReports(p.getUniqueId(), 1);
                for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                    HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                        if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                            all.sendMessage(HydroSlide.getInstance().getPrefix() + "Ein Report wurde von " + p.getDisplayName() + " §7angenommen.");
                            all.sendMessage(HydroSlide.getInstance().getPrefix() + "Reporteter Spieler: §e" + target.getName());
                            all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + HydroSlide.reportreason.get(args[1]));
                            if (reports.size() == 0) {
                                all.sendMessage(HydroSlide.getInstance().getPrefix() + "Alle Reports wurden bearbeitet.");
                            } else {
                                all.sendMessage(HydroSlide.getInstance().getPrefix() + "Es gibt weiterhin offene Reports.");
                            }
                        }
                    });
                }
                HydroSlide.reportreason.remove(args[1]);
                HydroSlide.reportchatlog.remove(args[1]);
                reports.remove(args[1]);
            } else if (args[0].equalsIgnoreCase("delete")) {
                if (!reports.containsKey(args[1])) {
                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cVom angegebenen Spieler gibt es kein Report.");
                    return;
                }
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "Du hast den Report von §e" + args[1] + " gelöscht.");
                for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                    HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                        if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                            all.sendMessage(HydroSlide.getInstance().getPrefix() + "Ein Report wurde von " + p.getDisplayName() + " §7gelöscht!");
                            all.sendMessage(HydroSlide.getInstance().getPrefix() + "Reporteter Spieler: §e" + args[1]);
                            if (reports.size() == 0) {
                                all.sendMessage(HydroSlide.getInstance().getPrefix() + "Alle Reports wurden bearbeitet.");
                            } else {
                                all.sendMessage(HydroSlide.getInstance().getPrefix() + "Es gibt weiterhin offene Reports.");
                            }
                        }
                    });
                }
                HydroSlide.reportreason.remove(args[1]);
                HydroSlide.reportchatlog.remove(args[1]);
                reports.remove(args[1]);
            } else {
                p.sendMessage(HydroSlide.getInstance().getPrefixUse() + "reportedit <accept|delete> <Spieler>");
            }
        } else {
            p.sendMessage(HydroSlide.getInstance().getPrefixUse() + "reportedit <accept|delete> <Spieler>");
        }
    }
}

