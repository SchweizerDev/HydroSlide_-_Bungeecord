package ch.luca.hydroslide.bungeecord.commands;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class AdminInfo extends Command {

    public AdminInfo(String command) {
        super(command);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(HydroSlide.getInstance().getNoPlayer());
            return;
        }
        ProxiedPlayer p = (ProxiedPlayer) sender;
        if (!p.hasPermission("hydroslide.admininfo")) {
            p.sendMessage(HydroSlide.getInstance().getNoPermission());
            return;
        }
        if (args.length == 1) {
            ProxyServer.getInstance().getScheduler().runAsync(HydroSlide.getInstance(), () -> {
                String name = args[0];
                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
                HydroSlide.getPlayerInfoRepository().getUUID(name, uuid -> {
                    if (uuid == null) {
                        p.sendMessage(HydroSlide.getInstance().getPlayerNeverOnline());
                        return;
                    }
                    HydroSlide.getPlayerInfoRepository().getIP(uuid, ip -> {
                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "Name: §e" + args[0]);
                        if (ProxyServer.getInstance().getPlayer(name) != null) {
                            p.sendMessage(HydroSlide.getInstance().getPrefix() + "Status: §aOnline §7(§e" + ProxyServer.getInstance().getPlayer(target.getName()).getServer().getInfo().getName() + "§7)");
                        } else {
                            p.sendMessage(HydroSlide.getInstance().getPrefix() + "Status: §cOffline");
                        }
                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "IP-Adresse: §c" + ip);
                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "UUID: §c" + uuid.toString());
                    });
                });
            });
        } else {
            p.sendMessage(HydroSlide.getInstance().getPrefixUse() + "admininfo <Spieler>");
        }
    }
}

