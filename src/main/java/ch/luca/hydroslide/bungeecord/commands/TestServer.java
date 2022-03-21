package ch.luca.hydroslide.bungeecord.commands;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class TestServer extends Command {

    public TestServer(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(HydroSlide.getInstance().getNoPlayer());
            return;
        }
        ProxiedPlayer p = (ProxiedPlayer)sender;
        if(!p.hasPermission("hydroslide.testserver")) {
            p.sendMessage(HydroSlide.getInstance().getNoPermission());
            return;
        }
        if(args.length == 0) {
                connectTestServer(p);
        } else {
            p.sendMessage(HydroSlide.getInstance().getPrefixUse() + "testserver");
        }
    }

    private static void connectTestServer(ProxiedPlayer p) {
            if (p.getServer().getInfo().getName().startsWith("TestServer-01")) {
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "Du befindest dich bereits auf dem TestServer.");
            } else {
                p.connect(ProxyServer.getInstance().getServerInfo("TestServer-01"));
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "Willkommen auf dem TestServer-01.");
            }
    }
}
