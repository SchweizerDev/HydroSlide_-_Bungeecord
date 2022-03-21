package ch.luca.hydroslide.bungeecord.commands;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import net.md_5.bungee.api.CommandSender;
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
            } else if(activated == 0) {
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "Du hast dich aus dem Teamsystem eingeloggt.");
                HydroSlide.getLogTeamRepository().setActive(p.getUniqueId());
            }
        });
    }
}
