package ch.luca.hydroslide.bungeecord.commands;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import ch.luca.hydroslide.bungeecord.mysql.repository.OnlineTimeRepository;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class OnlineTime extends Command {

    public OnlineTime(String command) {
        super(command);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(HydroSlide.getInstance().getNoPlayer());
            return;
        }
        ProxiedPlayer p = (ProxiedPlayer) sender;
        ProxyServer.getInstance().getScheduler().runAsync(HydroSlide.getInstance(), () -> {
            p.sendMessage(HydroSlide.getInstance().getPrefix() + "Deine Spielzeit beträgt: §e" + OnlineTimeRepository.getFormattedPlayTime(p.getUniqueId()));
        });
    }
}