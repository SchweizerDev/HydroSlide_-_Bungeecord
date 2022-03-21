package ch.luca.hydroslide.bungeecord.commands;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import ch.luca.hydroslide.bungeecord.mysql.repository.MuteRepository;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Unmute extends Command {

    public Unmute(String command) {
        super(command);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) sender;
            if(!p.hasPermission("hydroslide.unmute")) {
                p.sendMessage(HydroSlide.getInstance().getNoPermission());
                return;
            }
            if(args.length == 1) {
                String name = args[0];
                HydroSlide.getPlayerInfoRepository().getUUID(name, uuid -> {
                    if(MuteRepository.getIsMuted(uuid.toString())) {
                        MuteRepository.unmute(uuid.toString());
                        HydroSlide.getPlayerInfoRepository().removeMutePoints(uuid, 20);
                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "Du hast §e" + name + " §7entmutet.");
                        for(ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                            HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                    all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §centmutet§7.");
                                    all.sendMessage(HydroSlide.getInstance().getPrefix() + "Entmutet von: §a" + p.getName());
                                    all.sendMessage(HydroSlide.getInstance().getPrefix() + "Entfernte Mutepunkte: §320 Punkte");
                                }
                            });
                        }
                    } else {
                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDieser Spieler ist nicht gemutet.");
                    }
                });
            } else {
                p.sendMessage(HydroSlide.getInstance().getPrefixUse() + "unmute <Spieler>");
            }
        } else {
            if(args.length == 1) {
                String name = args[0];
                HydroSlide.getPlayerInfoRepository().getUUID(name, uuid -> {
                    if(MuteRepository.getIsMuted(uuid.toString())) {
                        MuteRepository.unmute(uuid.toString());
                        HydroSlide.getPlayerInfoRepository().removeMutePoints(uuid, 20);
                        sender.sendMessage(HydroSlide.getInstance().getPrefix() + "Du hast §e" + name + " §7entmutet.");
                        for(ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                            HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                    all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §centmutet§7.");
                                    all.sendMessage(HydroSlide.getInstance().getPrefix() + "Entmutet von: §aSystem");
                                    all.sendMessage(HydroSlide.getInstance().getPrefix() + "Entfernte Mutepunkte: §320 Punkte");
                                }
                            });
                        }
                    } else {
                        sender.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDieser Spieler ist nicht gemutet.");
                    }
                });
            } else {
                sender.sendMessage(HydroSlide.getInstance().getPrefixUse() + "unmute <Spieler>");
            }
        }
    }
}
