package ch.luca.hydroslide.bungeecord.commands;

import java.util.HashMap;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Reports extends Command {

    public Reports(String command) {
        super(command);
    }

    @SuppressWarnings("deprecation")
    public void execute(CommandSender sender, final String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(HydroSlide.getInstance().getNoPlayer());
            return;
        }
        ProxiedPlayer p = (ProxiedPlayer) sender;
        if (!p.hasPermission("hydroslide.reports")) {
            p.sendMessage(HydroSlide.getInstance().getNoPermission());
            return;
        }
        HashMap<String, String> reports = HydroSlide.reports;
        if (reports.size() <= 0) {
            p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cMomentan gibt es keine offenen Reports.");
            return;
        }
        for (String r : reports.keySet()) {
            TextComponent annehmen = new TextComponent("§7[§aReport übernehmen§7]");
            annehmen.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Verbinde dich auf den Server").create()));
            annehmen.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/reportedit accept " + r));
            TextComponent info = new TextComponent("§7[§eSpielerinfo§7]");
            info.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Infos über den Spieler").create()));
            info.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/userinfo " + r));
            TextComponent delete = new TextComponent("§7[§eReport löschen§7]");
            delete.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Lösche den Report").create()));
            delete.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/reportedit delete " + r));
            p.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + r + " §7wurde §creportet§7!");
            p.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + HydroSlide.reportreason.get(r));
            p.sendMessage(new TextComponent(HydroSlide.getInstance().getPrefix() + "§7Funktionen: "), new TextComponent(annehmen), new TextComponent(" §7| "), new TextComponent(info), new TextComponent(" §7| "),new TextComponent(delete));
        }
    }
}
