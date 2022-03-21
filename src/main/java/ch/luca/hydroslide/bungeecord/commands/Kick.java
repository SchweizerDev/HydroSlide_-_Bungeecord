package ch.luca.hydroslide.bungeecord.commands;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Kick extends Command {

    public Kick(String command) {
        super(command);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) sender;
            if (!p.hasPermission("hydroslide.kick")) {
                p.sendMessage(HydroSlide.getInstance().getNoPermission());
                return;
            }
            if (args.length > 1) {
                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
                String msg = "";
                for (int i = 1; i < args.length; i++) {
                    msg = msg + args[i] + " ";
                }
                if (args[0].equalsIgnoreCase(p.getName())) {
                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDu kannst dich selber nicht bestrafen.");
                    return;
                }
                if (target != null) {
                    target.disconnect("§cDu wurdest von §bHydroSlide.eu §cgebannt!\n\n§7Grund: §6" + msg);
                    for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                        String finalMsg = msg;
                        HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                            if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + target.getName() + " §7wurde §cgekickt§7!");
                                all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gekickt von: §a" + p.getName());
                                all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + finalMsg);
                            }
                        });
                    }
                } else {
                    p.sendMessage(HydroSlide.getInstance().getPlayerNotOnline());
                }
            } else {
                p.sendMessage(HydroSlide.getInstance().getPrefixUse() + "kick <Spieler> <Grund>");
            }
        } else {
            if(args.length > 1) {
                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
                String msg = "";
                for(int i = 1; i < args.length; i++) {
                    msg = msg + args[i] + " ";
                }
                if(target != null) {
                    target.disconnect("Du wurdest vom §e§lCube§3§lSlide §7Netzwerk gekickt! \n§7Grund: §6" + msg);
                    for(ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                        String finalMsg = msg;
                        HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                            if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + target.getName() + " §7wurde §cgekickt§7!");
                                all.sendMessage(HydroSlide.getInstance().getPrefix() + "Gekickt von: §aSystem");
                                all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + finalMsg);
                            }
                        });
                    }
                } else {
                    sender.sendMessage(HydroSlide.getInstance().getPlayerNotOnline());
                }
            } else {
                sender.sendMessage(HydroSlide.getInstance().getPrefixUse() + "kick <Spieler> <Grund>");
            }
        }
    }
}