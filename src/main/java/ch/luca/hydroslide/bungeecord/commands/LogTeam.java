package ch.luca.hydroslide.bungeecord.commands;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class LogTeam extends Command {

    public LogTeam(String command) {
        super(command);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(HydroSlide.getInstance().getNoPlayer());
            return;
        }
        ProxiedPlayer p = (ProxiedPlayer) sender;
        if(!p.hasPermission("hydroslide.logteam")) {
            p.sendMessage(HydroSlide.getInstance().getNoPermission());
            return;
        }
        HydroSlide.getLogTeamRepository().getIsActivated(p.getUniqueId(), activated -> {
            if(activated == 1) {
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "Du hast dich aus dem Teamsystem ausgeloggt.");
                HydroSlide.getLogTeamRepository().setInactive(p.getUniqueId());
                for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                        if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                            all.sendMessage(HydroSlide.getInstance().getPrefix() + "Das Teammitglied §e" + p.getName() + " §7hat sich aus dem TeamSystem §causgeloggt§7.");
                    }
                }
            } else if(activated == 0) {
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "Du hast dich in das Teamsystem eingeloggt.");
                HydroSlide.getLogTeamRepository().setActive(p.getUniqueId());
                for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                    if (all.hasPermission("hydroslide.team")) {
                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Das Teammitglied §e" + p.getName() + " §7hat sich in das TeamSystem §aeingeloggt§7.");
                    }
                }
            }
        });
    }
}
