package ch.luca.hydroslide.bungeecord.commands;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;

public class TeamChat extends Command {

    public TeamChat(String command) {
        super(command);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(HydroSlide.getInstance().getNoPlayer());
            return;
        }
        ProxiedPlayer p = (ProxiedPlayer) sender;
        if (!p.hasPermission("hydroslide.teamchat")) {
            p.sendMessage(HydroSlide.getInstance().getNoPermission());
            return;
        }
        HydroSlide.getLogTeamRepository().getIsActivated(p.getUniqueId(), activated -> {
            if(activated == 1) {
                if (args.length > 0) {
                    String msg = "";
                    for (int argCo = 0; argCo <= args.length - 1; argCo++) {
                        msg = msg + args[argCo] + " ";
                    }
                    for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                        if ((all.hasPermission("hydroslide.team"))) {
                            all.sendMessage("§c§lTeamChat §8» §e" + p.getServer().getInfo().getName() + " §8┃ " + p.getDisplayName() + "  §8» §a" + msg);
                        }
                    }
                } else {
                    p.sendMessage(HydroSlide.getInstance().getPrefixUse() + "tc [Nachricht]");
                    ArrayList<ProxiedPlayer> team = new ArrayList<>();
                    for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                        if (all.hasPermission("hydroslide.team")) {
                            team.add(all);
                        }
                    }
                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "Online Teammitglieder");
                    for (ProxiedPlayer teamlist : team) {
                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "§8» " + teamlist.getDisplayName() + " §8┃ §7Aktueller Server: §e" + teamlist.getServer().getInfo().getName());
                    }
                }
            } else if(activated == 0) {
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDu bist nicht im Teamsystem eingeloggt.");
            }
        });
    }
}
