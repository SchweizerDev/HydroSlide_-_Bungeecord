package ch.luca.hydroslide.bungeecord.commands;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import ch.luca.hydroslide.bungeecord.mysql.repository.MuteRepository;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;

public class MuteList extends Command {

    public MuteList(String command) {
        super(command);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(HydroSlide.getInstance().getNoPlayer());
            return;
        }
        ProxiedPlayer p = (ProxiedPlayer) sender;
        if (!p.hasPermission("hydroslide.mutelist")) {
            p.sendMessage(HydroSlide.getInstance().getNoPermission());
            return;
        }
        if (args.length == 0) {
            final ArrayList<String> bans = new ArrayList<>();
            p.sendMessage(HydroSlide.getInstance().getPrefix() + "Wird geladen...");
            ProxyServer.getInstance().getScheduler().runAsync(HydroSlide.getInstance(), () -> {
                for (String b : MuteRepository.getMutedPlayers()) {
                    bans.add(HydroSlide.getInstance().getPrefix() + "§e" + b + "§8(§6" + MuteRepository.getReasonWithName(b) + "§8)");
                }
                if (bans.size() > 0) {
                    for (String b : bans) {
                        p.sendMessage(b);
                    }
                } else {
                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDie Muteliste ist leer.");
                }
            });
        } else {
            p.sendMessage(HydroSlide.getInstance().getPlayerNeverOnline() + "mutelist");
        }
    }
}
