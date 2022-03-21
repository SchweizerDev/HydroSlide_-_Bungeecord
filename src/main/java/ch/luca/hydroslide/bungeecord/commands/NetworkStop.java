package ch.luca.hydroslide.bungeecord.commands;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.concurrent.TimeUnit;

public class NetworkStop extends Command {

    public NetworkStop(String command) {
        super(command);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(HydroSlide.getInstance().getNoPlayer());
            return;
        }
        ProxiedPlayer p = (ProxiedPlayer) sender;
        if(!p.hasPermission("hydroslide.networkstop")) {
            p.sendMessage(HydroSlide.getInstance().getNoPermission());
            return;
        }
        for(ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
            all.disconnect("§cDas Netzwerk wird neugestartet. \n §7§oDieser Vorgang dauert circa 20 Sekunden.");
        }
        ProxyServer.getInstance().getScheduler().schedule(HydroSlide.getInstance(), new Runnable() {
            @Override
            public void run() {
                ProxyServer.getInstance().stop();
            }
        }, 1, TimeUnit.SECONDS);
    }
}
