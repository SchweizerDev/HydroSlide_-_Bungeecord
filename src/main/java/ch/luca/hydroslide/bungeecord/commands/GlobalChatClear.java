package ch.luca.hydroslide.bungeecord.commands;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class GlobalChatClear extends Command {

    public GlobalChatClear(String command) {
        super(command);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(HydroSlide.getInstance().getNoPlayer());
            return;
        }
        ProxiedPlayer p = (ProxiedPlayer) sender;
        if(!p.hasPermission("hydroslide.globalchatclear")) {
            p.sendMessage(HydroSlide.getInstance().getNoPermission());
            return;
        }
        for(ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
            if(!all.hasPermission("hydroslide.team")) {
                for(int i = 0; i < 100; i++) {
                    all.sendMessage(" ");
                }
            }
            if(!all.hasPermission("hydroslide.team")) {
                if(all.getServer().getInfo().getName().equalsIgnoreCase(p.getServer().getInfo().getName())) {
                    all.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDer globale Serverchat wurde geelert.");
                }
            } else {
                if(all.getServer().getInfo().getName().equalsIgnoreCase(p.getServer().getInfo().getName())) {
                    all.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDer globale Serverchat wurde geelert.");
                }
            }
        }
    }
}
