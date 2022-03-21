package ch.luca.hydroslide.bungeecord.commands;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import ch.luca.hydroslide.bungeecord.mysql.repository.BanMuteHistoryRepository;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class History extends Command {

    public History(String command) {
        super(command);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(HydroSlide.getInstance().getNoPlayer());
            return;
        }
        ProxiedPlayer p = (ProxiedPlayer) sender;
        if(!p.hasPermission("hydroslide.history")) {
            p.sendMessage(HydroSlide.getInstance().getNoPermission());
            return;
        }
        if(args.length == 2) {
            String name = args[0];
            HydroSlide.getPlayerInfoRepository().getUUID(name, uuid -> {
                if(uuid == null) {
                    p.sendMessage(HydroSlide.getInstance().getPlayerNeverOnline());
                    return;
                }
                if(args[1].equalsIgnoreCase("bans")) {
                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "Die Bans von §e" + name + " §7werden geladen...");
                    HydroSlide.getBanMuteHistoryRepository().getHistory(BanMuteHistoryRepository.HistoryType.BAN, uuid, historyEntries -> {
                        if(historyEntries.size() == 0) {
                            p.sendMessage(HydroSlide.getInstance().getPrefix() + "Der angegebene Spieler hat keine Bans.");
                            return;
                        }
                        int id = 1;
                        for(BanMuteHistoryRepository.HistoryEntry entry : historyEntries) {
                            p.sendMessage(HydroSlide.getInstance().getPrefix() + "Ban §c" + id + "§7:");
                            p.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + entry.getReason());
                            p.sendMessage(HydroSlide.getInstance().getPrefix() + "Gebannt von: §a" + entry.getPunisherName());
                            id++;
                        }
                    });
                } else if(args[1].equalsIgnoreCase("mutes")) {
                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "Die Mutes von §e" + name + " §7werden geladen...");
                    HydroSlide.getBanMuteHistoryRepository().getHistory(BanMuteHistoryRepository.HistoryType.MUTE, uuid, historyEntries -> {
                        if(historyEntries.size() == 0) {
                            p.sendMessage(HydroSlide.getInstance().getPrefix() + "Der angegebene Spieler hat keine Mutes.");
                            return;
                        }
                        int id = 1;
                        for(BanMuteHistoryRepository.HistoryEntry entry : historyEntries) {
                            p.sendMessage(HydroSlide.getInstance().getPrefix() + "Mute §c" + id + "§7:");
                            p.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + entry.getReason());
                            p.sendMessage(HydroSlide.getInstance().getPrefix() + "Gemutet von: §a" + entry.getPunisherName());
                            id++;
                        }
                    });
                } else {
                    p.sendMessage(HydroSlide.getInstance().getPrefixUse() + "history <Spieler> <bans|mutes>");
                }
            });
        } else {
            p.sendMessage(HydroSlide.getInstance().getPrefixUse() + "history <Spieler> <bans|mutes>");
        }
    }
}
