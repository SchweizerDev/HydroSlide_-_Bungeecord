package ch.luca.hydroslide.bungeecord.commands;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class TeamStats extends Command {

    public TeamStats(String command) {
        super(command);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(HydroSlide.getInstance().getNoPlayer());
            return;
        }
        ProxiedPlayer p = (ProxiedPlayer) sender;
        if (!p.hasPermission("hydroslide.teamstats")) {
            p.sendMessage(HydroSlide.getInstance().getNoPermission());
            return;
        }
        if (args.length == 1) {
            String name = args[0];
            HydroSlide.getPlayerInfoRepository().getUUID(name, uuid -> {
                if (uuid == null) {
                    p.sendMessage(HydroSlide.getInstance().getPlayerNeverOnline());
                    return;
                }
                HydroSlide.getTeamStatsRepository().getSupports(uuid, supports -> {
                    HydroSlide.getTeamStatsRepository().getReports(uuid, reports -> {
                        HydroSlide.getTeamStatsRepository().getKicks(uuid, kicks -> {
                            HydroSlide.getTeamStatsRepository().getMutes(uuid, mutes -> {
                                HydroSlide.getTeamStatsRepository().getBans(uuid, bans -> {
                                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "Teammitglied: §e" + name);
                                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "§e" + supports + " §7Supports erledigt");
                                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "§e" + reports + " §7Reports angenommen");
                                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "§e" + kicks + " §7Kicks ausgeführt");
                                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "§e" + mutes + " §7Mutes verteilt");
                                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "§e" + bans + " §7Bans erteilt");
                                });
                            });
                        });
                    });
                });
            });
        } else {
            p.sendMessage(HydroSlide.getInstance().getPrefixUse() + "teamstats <Spieler>");
        }
    }
}