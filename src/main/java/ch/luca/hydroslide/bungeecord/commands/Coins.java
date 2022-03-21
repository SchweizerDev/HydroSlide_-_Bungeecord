package ch.luca.hydroslide.bungeecord.commands;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import ch.luca.hydroslide.bungeecord.manager.CoinsManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Coins extends Command {

    public Coins(String command) {
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
            HydroSlide.getCoinsRepository().getCoins(p.getUniqueId(), coins -> p.sendMessage(HydroSlide.getInstance().getPrefix() + "Deine aktuellen Coins betragen §6" + CoinsManager.asString(Integer.parseInt(coins)) + " Coins§7."));
        } else {
            String name = args[0];
            HydroSlide.getPlayerInfoRepository().getUUID(name, uuid -> {
                if (uuid == null) {
                    p.sendMessage(HydroSlide.getInstance().getPlayerNeverOnline());
                    return;
                }
                HydroSlide.getCoinsRepository().getCoins(uuid, coins -> p.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7hat §6" + CoinsManager.asString(Integer.parseInt(coins))  + " Coins§7."));
            });
        }
    }
}
