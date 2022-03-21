package ch.luca.hydroslide.bungeecord.commands;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Broadcast extends Command {

    public Broadcast(String command) {
        super(command);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(HydroSlide.getInstance().getNoPlayer());
            return;
        }
        ProxiedPlayer p = (ProxiedPlayer) sender;
        if(!p.hasPermission("hydroslide.broadcast")) {
            p.sendMessage(HydroSlide.getInstance().getNoPermission());
            return;
        }
        if(args.length >= 1) {
            String msg = "";
            for(int argCo = 0; argCo <= args.length - 1; argCo++) {
                msg = msg + args[argCo] + " ";
            }
            for(ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                all.sendMessage(HydroSlide.getInstance().getPrefix() + "§7" + msg.replace("&", "§"));
            }
        } else {
            p.sendMessage(HydroSlide.getInstance().getPrefixUse() + "broadcast <Nachricht>");
        }
    }
}
