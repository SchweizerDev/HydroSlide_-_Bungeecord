package ch.luca.hydroslide.bungeecord.commands;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class JumpTo extends Command {

    public JumpTo(String command) {
        super(command);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(HydroSlide.getInstance().getNoPlayer());
            return;
        }
        ProxiedPlayer p = (ProxiedPlayer) sender;
        if(!p.hasPermission("hydroslide.jumpto")) {
            p.sendMessage(HydroSlide.getInstance().getNoPermission());
            return;
        }
        if(args.length == 1) {
            if(ProxyServer.getInstance().getPlayer(args[0]) == null) {
               p.sendMessage(HydroSlide.getInstance().getPlayerNotOnline());
               return;
            }
            if(p.getName().equalsIgnoreCase(args[0])) {
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDu kannst nicht zu dir selber springen.");
                return;
            }
            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
            p.sendMessage(HydroSlide.getInstance().getPrefix() + "Verbinde zu §e" + target.getServer().getInfo().getName() + "...");
            ServerInfo serverInfo = target.getServer().getInfo();
            p.connect(serverInfo);
        } else {
            p.sendMessage(HydroSlide.getInstance().getPrefixUse() + "jumpto <Spieler>");
        }
    }
}
