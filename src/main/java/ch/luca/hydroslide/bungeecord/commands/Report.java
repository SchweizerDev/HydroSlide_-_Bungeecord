package ch.luca.hydroslide.bungeecord.commands;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.HashMap;

public class Report extends Command {

    public Report(String command) {
        super(command);
    }


    public void execute(CommandSender sender, final String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(HydroSlide.getInstance().getNoPlayer());
            return;
        }
        ProxiedPlayer p = (ProxiedPlayer) sender;
        if(args.length == 2) {
            if (args[0].equalsIgnoreCase(p.getName())) {
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDu kannst dich nicht selbst reporten.");
                return;
            }
            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
            if (target == null) {
                p.sendMessage(HydroSlide.getInstance().getPlayerNotOnline());
                return;
            }
            if (target.hasPermission("hydroslide.team")) {
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDieser Spieler ist ein Teammitglied. Melde dich beim Support falls der Spieler verdächtig ist.");
                return;
            }
            HashMap<String, String> reports = HydroSlide.reports;
            if (reports.containsKey(target.getName())) {
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDer Spieler wurde bereits reportet.");
                return;
            }
            String msg = "";
            if (args[1].equalsIgnoreCase("Hacking") || args[1].equalsIgnoreCase("Cheating") || args[1].equalsIgnoreCase("Killaura") || args[1].equalsIgnoreCase("Fly") || args[1].equalsIgnoreCase("AntiKnockback")) {
                msg = "Hacking";
            } else if (args[1].equalsIgnoreCase("Beleidigung") || args[1].equalsIgnoreCase("Chat")) {
                msg = "Chatverhalten";
            } else if (args[1].equalsIgnoreCase("Teaming") || args[1].equalsIgnoreCase("Team")) {
                msg = "Teaming";
            } else if (args[1].equalsIgnoreCase("Buguser") || args[1].equalsIgnoreCase("Bugusing") || args[1].equalsIgnoreCase("Buguse")) {
                msg = "Bugusing";
            } else if (args[1].equalsIgnoreCase("Werbung")) {
                msg = "Werbung";
            } else if (args[1].equalsIgnoreCase("Statsboosting")) {
                msg = "Statsboosting";
            } else {
                p.sendMessage(HydroSlide.getInstance().getPrefixUse() + "report <Spieler> <Grund>");
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "Verfügbare Gründe: §eHacking §8| §eChatverhalten §8| §eTeaming §8| §eBugusing §8| §eWerbung §8| §eStatsboosting");
                return;
            }
            reports.put(target.getName(), p.getName());
            HydroSlide.reportreason.put(target.getName(), msg);
            p.sendMessage(HydroSlide.getInstance().getPrefix() + "Vielen Dank für dein Report! Ein Teammitglied wurde benachrichtigt...");
            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                String finalMsg = msg;
                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                        TextComponent annehmen = new TextComponent("§7[§aReport übernehmen§7]");
                        annehmen.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Verbinde dich auf den Server").create()));
                        annehmen.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/reportedit accept " + target.getName()));
                        TextComponent info = new TextComponent("§7[§eSpielerinfo§7]");
                        info.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Infos über den Spieler").create()));
                        info.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/userinfo " + target.getName()));
                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + target.getName() + " §7wurde §creportet§7!");
                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Reportet von: §a" + p.getName());
                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + finalMsg);
                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Server: §c" + target.getServer().getInfo().getName());
                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Ping: §b" + target.getPing());
                        all.sendMessage(new TextComponent(HydroSlide.getInstance().getPrefix() + "Funktionen: "), new TextComponent(annehmen), new TextComponent(" §7| "), new TextComponent(info));
                    }
                });
            }
        } else {
            p.sendMessage(HydroSlide.getInstance().getPrefixUse() + "report <Spieler> <Grund>");
            p.sendMessage(HydroSlide.getInstance().getPrefix() + "Verfügbare Gründe: §eHacking §8| §eChatverhalten §8| §eTeaming §8| §eBugusing §8| §eWerbung §8| §eStatsboosting");
        }
    }
}