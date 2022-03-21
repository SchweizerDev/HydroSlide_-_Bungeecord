package ch.luca.hydroslide.bungeecord.commands;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import ch.luca.hydroslide.bungeecord.mysql.repository.BanRepository;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Unban extends Command {

    public Unban(String command) {
        super(command);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) sender;
            if(!p.hasPermission("hydroslide.unban")) {
                p.sendMessage(HydroSlide.getInstance().getNoPermission());
                return;
            }
            if(args.length == 1) {
                String name = args[0];
                HydroSlide.getPlayerInfoRepository().getUUID(name, uuid -> {
                    if(BanRepository.getIsBanned(uuid.toString())) {
                        BanRepository.unban(uuid.toString());
                        HydroSlide.getPlayerInfoRepository().removeBanPoints(uuid, 50);
                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "Du hast §e" + name + " §7entbannt.");
                        for(ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                            HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                    all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §centbannt§7.");
                                    all.sendMessage(HydroSlide.getInstance().getPrefix() + "Entbannt von: §a" + p.getName());
                                    all.sendMessage(HydroSlide.getInstance().getPrefix() + "Entfernte Banpunkte: §350 Punkte");
                                }
                            });
                        }
                    } else {
                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDieser Spieler ist nicht gebannt.");
                    }
                });
            } else {
                p.sendMessage(HydroSlide.getInstance().getPrefixUse() + "unban <Spieler>");
            }
        } else {
            if(args.length == 1) {
                String name = args[0];
                HydroSlide.getPlayerInfoRepository().getUUID(name, uuid -> {
                    if(BanRepository.getIsBanned(uuid.toString())) {
                        BanRepository.unban(uuid.toString());
                        HydroSlide.getPlayerInfoRepository().removeBanPoints(uuid, 50);
                        sender.sendMessage(HydroSlide.getInstance().getPrefix() + "Du hast §e" + name + " §7entbannt.");
                        for(ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                            HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                    all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde §centbannt§7.");
                                    all.sendMessage(HydroSlide.getInstance().getPrefix() + "Entbannt von: §aSystem");
                                    all.sendMessage(HydroSlide.getInstance().getPrefix() + "Entfernte Banpunkte: §350 Punkte");
                                }
                            });
                        }
                    } else {
                        sender.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDieser Spieler ist nicht gebannt.");
                    }
                });
            } else {
                sender.sendMessage(HydroSlide.getInstance().getPrefixUse() + "unban <Spieler>");
            }
        }
    }
}
