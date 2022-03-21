package ch.luca.hydroslide.bungeecord.commands;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class List extends Command {

    public List(String command) {
        super(command);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(HydroSlide.getInstance().getNoPlayer());
            return;
        }
        ProxiedPlayer p = (ProxiedPlayer) sender;
        if(args.length == 0) {
            p.sendMessage(HydroSlide.getInstance().getPrefix() + "Dein aktueller Server: §e" + p.getServer().getInfo().getName());
            p.sendMessage(HydroSlide.getInstance().getPrefix() + "Gesamtspieler auf dem Netzwerk: §a" + ProxyServer.getInstance().getOnlineCount());
        } else {
            p.sendMessage(HydroSlide.getInstance().getPrefixUse() + "list");
        }
    }
}
