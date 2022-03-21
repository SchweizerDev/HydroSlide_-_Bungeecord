package ch.luca.hydroslide.bungeecord.commands;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class BauServer extends Command {

    public BauServer(String command) {
        super(command);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(HydroSlide.getInstance().getNoPlayer());
            return;
        }
        ProxiedPlayer p = (ProxiedPlayer) sender;
        if(!p.hasPermission("hydroslide.bauserver")) {
            p.sendMessage(HydroSlide.getInstance().getNoPermission());
            return;
        }
        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("1")) {
                connectBauServer1(p);
            } else if(args[0].equalsIgnoreCase("2")) {
                connectBauServer2(p);
            }
        } else {
            p.sendMessage(HydroSlide.getInstance().getPrefixUse() + "bauserver <1|2>");
            p.sendMessage(HydroSlide.getInstance().getPrefix() + "Information: §c1 §71.8 Bauserver | §c2 §71.17.1 Bauserver");
        }
    }

    private void connectBauServer1(ProxiedPlayer p) {
        if(p.getServer().getInfo().getName().startsWith("BauServer-01")) {
            p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDu befindest dich bereits auf dem Bauserver-01.");
        } else {
            p.connect(ProxyServer.getInstance().getServerInfo("BauServer-01"));
            p.sendMessage(HydroSlide.getInstance().getPrefix() + "Willkommen auf dem BauServer-01.");
        }
    }

    private void connectBauServer2(ProxiedPlayer p) {
        if(p.getServer().getInfo().getName().startsWith("BauServer-02")) {
            p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDu befindest dich bereits auf dem Bauserver-02.");
        } else {
            p.connect(ProxyServer.getInstance().getServerInfo("BauServer-02"));
            p.sendMessage(HydroSlide.getInstance().getPrefix() + "Willkommen auf dem BauServer-02.");
        }
    }
}
