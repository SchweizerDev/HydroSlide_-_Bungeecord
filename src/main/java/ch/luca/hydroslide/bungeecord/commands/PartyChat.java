package ch.luca.hydroslide.bungeecord.commands;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;

public class PartyChat extends Command {

    public PartyChat(String command) {
        super(command);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(HydroSlide.getInstance().getNoPlayer());
            return;
        }
        ProxiedPlayer p = (ProxiedPlayer) sender;
        if (!Party.isparty.containsKey(p.getName())) {
            p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDu bist in keiner Party.");
            return;
        }
        if (args.length >= 1) {
            String msg = "";
            for (int argCo = 0; argCo <= args.length - 1; argCo++) {
                msg = msg + args[argCo] + " ";
            }
            ArrayList<String> party = Party.party.get(Party.isparty.get(p.getName()));
            for (String player : party) {
                ProxiedPlayer pl = ProxyServer.getInstance().getPlayer(player);
                pl.sendMessage(HydroSlide.getInstance().getPrefix() + "§7[§5PartyChat§7] §e" + p.getName() + " §8» §7" + msg);
            }
        } else {
            p.sendMessage(HydroSlide.getInstance().getPrefixUse() + "pc <Nachricht>");
        }
    }
}
