package ch.luca.hydroslide.bungeecord.commands;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class AdminCoins extends Command {

    public AdminCoins(String command) {
        super(command);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) sender;
            if (!p.hasPermission("hydroslide.admincoins")) {
                p.sendMessage(HydroSlide.getInstance().getNoPermission());
                return;
            }
            if (args.length < 2) {
                p.sendMessage(HydroSlide.getInstance().getPrefixUse() + "admincoins <add|remove|get> <Spieler> [Anzahl]");
                return;
            }
            String name = args[1];
            HydroSlide.getPlayerInfoRepository().getUUID(name, uuid -> {
                if (uuid == null) {
                    p.sendMessage(HydroSlide.getInstance().getPlayerNeverOnline());
                    return;
                }
                int coins = 0;
                if (!args[0].equalsIgnoreCase("get")) {
                    if (args.length < 3) {
                        p.sendMessage(HydroSlide.getInstance().getPrefixUse() + "admincoins <add|remove|get> <Spieler> [Anzahl]");
                        return;
                    }
                    try {
                        coins = Integer.parseInt(args[2]);
                    } catch (NumberFormatException exception) {
                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cGib eine Zahl an.");
                        return;
                    }
                }
                if (args[0].equalsIgnoreCase("add")) {
                    HydroSlide.getCoinsRepository().addCoins(uuid, coins);
                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "Du hast §e" + args[1] + " §6" + coins + " Coins §7hinzugefügt.");
                } else if (args[0].equalsIgnoreCase("remove")) {
                    HydroSlide.getCoinsRepository().removeCoins(uuid, coins);
                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "Du hast §e" + args[1] + " §6" + coins + " Coins §7entfernt.");
                } else if (args[0].equalsIgnoreCase("get")) {
                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "§e" + args[1] + " §7hat §6" + coins + " Coins§7.");
                } else {
                    p.sendMessage(HydroSlide.getInstance().getPrefixUse() + "admincoins <add|remove|get> <Spieler> [Anzahl]");
                }
            });
        } else {
            if (args.length < 2) {
                sender.sendMessage(HydroSlide.getInstance().getPrefixUse() + "admincoins <add|remove|get> <Spieler> [Anzahl]");
                return;
            }
            String name = args[1];
            HydroSlide.getPlayerInfoRepository().getUUID(name, uuid -> {
                if (uuid == null) {
                    sender.sendMessage(HydroSlide.getInstance().getPlayerNeverOnline());
                    return;
                }
                int coins = 0;
                if (!args[0].equalsIgnoreCase("get")) {
                    if (args.length < 3) {
                        sender.sendMessage(HydroSlide.getInstance().getPrefixUse() + "admincoins <add|remove|get> <Spieler> [Anzahl]");
                        return;
                    }
                    try {
                        coins = Integer.parseInt(args[2]);
                    } catch (NumberFormatException exception) {
                        sender.sendMessage(HydroSlide.getInstance().getPrefix() + "§cGib eine Zahl an.");
                        return;
                    }
                }
                if (args[0].equalsIgnoreCase("add")) {
                    HydroSlide.getCoinsRepository().addCoins(uuid, coins);
                    sender.sendMessage(HydroSlide.getInstance().getPrefix() + "Du hast §e" + args[1] + " §6" + coins + " Coins §7hinzugefügt.");
                } else if (args[0].equalsIgnoreCase("remove")) {
                    HydroSlide.getCoinsRepository().removeCoins(uuid, coins);
                    sender.sendMessage(HydroSlide.getInstance().getPrefix() + "Du hast §e" + args[1] + " §6" + coins + " Coins §7entfernt.");
                } else if (args[0].equalsIgnoreCase("get")) {
                    sender.sendMessage(HydroSlide.getInstance().getPrefix() + "§e" + args[1] + " §7hat §6" + coins + " Coins§7.");
                } else {
                    sender.sendMessage(HydroSlide.getInstance().getPrefixUse() + "admincoins <add|remove|get> <Spieler> [Anzahl]");
                }
            });
        }
    }
}
