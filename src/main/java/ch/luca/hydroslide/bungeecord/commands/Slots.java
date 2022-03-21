package ch.luca.hydroslide.bungeecord.commands;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import ch.luca.hydroslide.bungeecord.config.SlotConfig;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Slots extends Command {

    public Slots(String command) {
        super(command);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(HydroSlide.getInstance().getNoPlayer());
            return;
        }
        ProxiedPlayer p = (ProxiedPlayer) sender;
        if(!p.hasPermission("hydroslide.setslots")) {
            p.sendMessage(HydroSlide.getInstance().getNoPermission());
            return;
        }
        if (args.length == 0) {
            p.sendMessage(HydroSlide.getInstance().getPrefixUse() + "slots <set|info> [Anzahl]");
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("info")) {
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Server hat momentan §e" + SlotConfig.getSlots() + " §7Slots.");
            } else {
                p.sendMessage(HydroSlide.getInstance().getPrefixUse() + "slots <set|info> [Anzahl]");
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("set")) {
                int i = 0;
                try {
                    i = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cGib eine Zahl an.");
                    return;
                }
                if (i < 1) {
                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cGib eine Zahl an.");
                    return;
                }
                SlotConfig.setSlots(i);
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "Du hast die Slots auf §e" + i + " §7gestellt.");
            } else {
                p.sendMessage(HydroSlide.getInstance().getPrefixUse() + "slots <set|info> [Anzahl]");
            }
        }
    }
}
