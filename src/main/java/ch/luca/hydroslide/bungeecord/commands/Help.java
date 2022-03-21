package ch.luca.hydroslide.bungeecord.commands;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Help extends Command {

    public Help(String command) {
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
            p.sendMessage(HydroSlide.getInstance().getPrefix() + "§e/discord §8- §7Zeigt dir die Adresse des Discords");
            p.sendMessage(HydroSlide.getInstance().getPrefix() + "§e/coins §8- §7Zeigt dir dein Kontostand");
            p.sendMessage(HydroSlide.getInstance().getPrefix() + "§e/ping §8- §7Zeigt dir dein aktuellen Ping");
            p.sendMessage(HydroSlide.getInstance().getPrefix() + "§e/party §8- §7Spiele mit deinen Freunden in einer Party");
            p.sendMessage(HydroSlide.getInstance().getPrefix() + "§e/hub §8- §7Kehre zur Lobby zurück");
            p.sendMessage(HydroSlide.getInstance().getPrefix() + "§e/list §8- §7Zeigt dir deinen aktuellen Server + Spieler auf dem Netzwerk");
            p.sendMessage(HydroSlide.getInstance().getPrefix() + "");
            p.sendMessage(HydroSlide.getInstance().getPrefix() + "Bei weiteren Fragen besuche unsere Webseite oder benutze den Support.");
            p.sendMessage(HydroSlide.getInstance().getPrefix() + "Webseite: §ewww.hydroslide.eu §7| Discord: §ediscord.hydroslide.eu");
        } else {
            p.sendMessage(HydroSlide.getInstance().getPrefixUse() + "help");
        }
    }
}
