package ch.luca.hydroslide.bungeecord.commands;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Hub extends Command {

    public Hub(String command) {
        super(command);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(HydroSlide.getInstance().getNoPlayer());
            return;
        }
        ProxiedPlayer p = (ProxiedPlayer) sender;
        if(!p.getServer().getInfo().getName().equalsIgnoreCase("Lobby-01") || p.getServer().getInfo().getName().equalsIgnoreCase("SilentLobby-01")) {
            p.connect(ProxyServer.getInstance().getServerInfo("Lobby-01"));
        } else {
            p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDu befindest dich bereits in der Lobby.");
        }
    }
}
