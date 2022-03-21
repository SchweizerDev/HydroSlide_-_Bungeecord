package ch.luca.hydroslide.bungeecord.commands;


import ch.luca.hydroslide.bungeecord.HydroSlide;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class LobbyCases extends Command {

    public LobbyCases(String command) {
        super(command);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(HydroSlide.getInstance().getNoPlayer());
            return;
        }
        ProxiedPlayer p = (ProxiedPlayer) sender;
        if(!p.hasPermission("hydroslide.lobbycases")) {
            p.sendMessage(HydroSlide.getInstance().getNoPermission());
            return;
        }
        if(args.length < 2) {
            p.sendMessage(HydroSlide.getInstance().getPrefixUse() + "lobbycases <add|remove> <Spieler> <Anzahl>");
            return;
        }
        String name = args[1];
        HydroSlide.getPlayerInfoRepository().getUUID(name, uuid -> {
            if(uuid == null) {
                p.sendMessage(HydroSlide.getInstance().getPlayerNeverOnline());
                return;
            }
            int amount = 0;
            try {
                amount = Integer.parseInt(args[2]);
            } catch (NumberFormatException exception) {
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cGib eine Zahl an.");
            }
            if(args[0].equalsIgnoreCase("add")) {
                HydroSlide.getInstance().getLobbyCaseRepository().addCases(uuid, amount);
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "Du hast §e" + name + " §a" + amount + " Lobbyschatztruhen §7hinzugefügt.");
            } else if(args[0].equalsIgnoreCase("remove")) {
                HydroSlide.getInstance().getLobbyCaseRepository().removeCases(uuid, amount);
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "Du hast §e" + name + " §a" + amount + " Lobbyschatztruhen §7entfernt.");
            }
        });
    }
}
