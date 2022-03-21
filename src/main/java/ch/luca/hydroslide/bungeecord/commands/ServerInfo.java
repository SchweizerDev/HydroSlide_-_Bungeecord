package ch.luca.hydroslide.bungeecord.commands;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ServerInfo extends Command {

    public ServerInfo(String command) {
        super(command);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(HydroSlide.getInstance().getNoPlayer());
            return;
        }
        ProxiedPlayer p = (ProxiedPlayer) sender;
        if (!p.hasPermission("hydroslide.serverinfo")) {
            p.sendMessage(HydroSlide.getInstance().getNoPermission());
            return;
        }
        if (args.length == 0) {
            Runtime run = Runtime.getRuntime();
            int memory = (int) (run.totalMemory() - run.freeMemory());
            int cores = run.availableProcessors();
            p.sendMessage(HydroSlide.getInstance().getPrefix() + "Spieler online: §e" + ProxyServer.getInstance().getPlayers().size());
            p.sendMessage(HydroSlide.getInstance().getPrefix() + "System: §e" + System.getProperty("os.name"));
            p.sendMessage(HydroSlide.getInstance().getPrefix() + "Total RAM: §e" + run.totalMemory() + " MB");
            p.sendMessage(HydroSlide.getInstance().getPrefix() + "Benutzter RAM: §e" + memory + " MB");
            p.sendMessage(HydroSlide.getInstance().getPrefix() + "Kerne: §e" + cores);
            p.sendMessage(HydroSlide.getInstance().getPrefix() + "Version: §e" + System.getProperty("os.version"));
            p.sendMessage(HydroSlide.getInstance().getPrefix() + "Java Version: §e" + System.getProperty("java.version"));
        } else {
            p.sendMessage(HydroSlide.getInstance().getPrefixUse() + "serverinfo");
        }
    }
}