package ch.luca.hydroslide.bungeecord.commands;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;

public class ChatMute extends Command {

    public ChatMute(String command) {
        super(command);
    }

    public static boolean muteGlobal = false;
    public static ArrayList<String> muteLocal = new ArrayList<>();

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(HydroSlide.getInstance().getNoPlayer());
            return;
        }
        ProxiedPlayer p = (ProxiedPlayer) sender;
        if(!p.hasPermission("hydroslide.chatmute")) {
            p.sendMessage(HydroSlide.getInstance().getNoPermission());
            return;
        }
        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("lokal")) {
                if(!muteLocal.contains(p.getServer().getInfo().getName())) {
                    muteLocal.add(p.getServer().getInfo().getName());
                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Chat für den Server §e" + p.getServer().getInfo().getName() + " §7wurde §cdeaktiviert§7.");
                } else {
                    muteLocal.remove(p.getServer().getInfo().getName());
                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Chat für den Server §e" + p.getServer().getInfo().getName() + " §7wurde §aaktiviert§7.");
                }
            } else if(args[0].equalsIgnoreCase("global")) {
                if(muteGlobal) {
                    muteGlobal = false;
                    for(ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Globale Chat wurde §aaktiviert§7.");
                    }
                } else {
                    muteGlobal = true;
                    for(ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Globale Chat wurde §cdeaktiviert§7.");
                    }
                }
            }
        }
    }
}
