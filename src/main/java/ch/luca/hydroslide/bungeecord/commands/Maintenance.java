package ch.luca.hydroslide.bungeecord.commands;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import ch.luca.hydroslide.bungeecord.config.MaintenanceConfig;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Maintenance extends Command {

    public Maintenance(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) sender;
            if (!p.hasPermission("hydroslide.maintenance")) {
                p.sendMessage(HydroSlide.getInstance().getNoPermission());
                return;
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("on")) {
                    MaintenanceConfig.setMaintenance(true);
                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Wartungsmodus wurde aktiviert.");
                    for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                        if (!all.hasPermission("hydroslide.maintenance")) {
                            all.disconnect("§bHydroSlide §7× §eNetzwerk \n §cDer Server befindet sich momentan in Wartungsarbeiten. \n §7Versuche es später erneut. \n\n §7Discord: §ediscord.hydroslide.eu");
                        }
                    }
                } else if (args[0].equalsIgnoreCase("off")) {
                    MaintenanceConfig.setMaintenance(false);
                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Wartungsmodus wurde deaktiviert.");
                } else {
                    p.sendMessage(HydroSlide.getInstance().getPrefixUse() + "maintenance <on|off>");
                }
            } else {
                p.sendMessage(HydroSlide.getInstance().getPrefixUse() + "maintenance <on|off>");
            }
        } else {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("on")) {
                    MaintenanceConfig.setMaintenance(true);
                    sender.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Wartungsmodus wurde aktiviert.");
                    for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                        if (!all.hasPermission("hydroslide.maintenance")) {
                            all.disconnect("§bHydroSlide §7× §eNetzwerk \n §cDer Server befindet sich momentan in Wartungsarbeiten. \n §7Versuche es später erneut. \n\n §7Discord: §ediscord.hydroslide.eu");
                        }
                    }
                } else if (args[0].equalsIgnoreCase("off")) {
                    MaintenanceConfig.setMaintenance(false);
                    sender.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Wartungsmodus wurde deaktiviert.");
                } else {
                    sender.sendMessage(HydroSlide.getInstance().getPrefixUse() + "maintenance <on|off>");
                }
            } else {
                sender.sendMessage(HydroSlide.getInstance().getPrefixUse() + "maintenance <on|off>");
            }
        }
    }
}
