package ch.luca.hydroslide.bungeecord.commands;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Support extends Command {

    public Support(String command) {
        super(command);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer p = (ProxiedPlayer) sender;
        if (args.length == 0) {
            if (!HydroSlide.needHelp.contains(p)) {
                if (p.hasPermission("hydroslide.team")) {
                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cAls Teammitglied kannst du keine Supportanfrage stellen.");
                    return;
                }
                if (HydroSlide.inSupport.contains(p)) {
                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDu bist bereits in einem Supportgespräch.");
                    return;
                }
                for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                    HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                        if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                            TextComponent accept = new TextComponent("§7[§aAnnehmen§7]");
                            accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/support accept " + p.getName()));
                            all.sendMessage(HydroSlide.getInstance().getPrefix() + "Eine Supportanfrage wurde erstellt.");
                            all.sendMessage(HydroSlide.getInstance().getPrefix() + "Spieler: §e" + p.getName());
                            all.sendMessage(new TextComponent(HydroSlide.getInstance().getPrefix()), new TextComponent(accept));
                        }
                    });
                }
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "Deine Supportanfrage wurde erstellt. Du wirst in kürze supportet.");
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "Falls kein Crewmitglied online ist, kannst du auch unseren Ticketsupport nutzen auf Discord.");
                HydroSlide.needHelp.add(p);
            } else {
                HydroSlide.needHelp.remove(p);
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "Du hast deine Supportanfrage zurückgezogen.");
            }
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("end") || args[0].equalsIgnoreCase("abbrechen") || args[0].equalsIgnoreCase("schliessen")) {
                if (HydroSlide.inSupport.contains(p)) {
                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "Du hast den Supportchat beendet.");
                    HydroSlide.chat.get(p).sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + p.getName() + " §7hat den Supportchat §cgeschlossen§7.");
                    HydroSlide.inSupport.remove(p);
                    HydroSlide.inSupport.remove(HydroSlide.chat.get(p));
                    HydroSlide.chat.remove(p);
                    HydroSlide.chat.remove(HydroSlide.chat.get(p));
                } else {
                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDu bist in keinem Supportchat.");
                }
            }
        } else if (args.length == 2) {
            if (!p.hasPermission("hydroslide.team")) {
                p.sendMessage(HydroSlide.getInstance().getNoPermission());
                return;
            }
            if (args[0].equalsIgnoreCase("accept")) {
                if (ProxyServer.getInstance().getPlayer(args[1]) != null) {
                    ProxiedPlayer p2 = ProxyServer.getInstance().getPlayer(args[1]);
                    if (HydroSlide.needHelp.contains(p2)) {
                        if (!HydroSlide.inSupport.contains(p)) {
                            HydroSlide.needHelp.remove(p2);
                            HydroSlide.inSupport.add(p);
                            HydroSlide.inSupport.add(p2);
                            HydroSlide.chat.put(p, p2);
                            HydroSlide.chat.put(p2, p);
                            p.sendMessage("§bSupportChat §8» §7Du hast die Supportanfrage von §e" + p2.getName() + " §7angenommen.");
                            p.sendMessage("§bSupportChat §8» §7Um das Gespräch zu verlassen mache §e/support end");
                            p2.sendMessage("§bSupportChat §8» §7Deine Supportanfrage wurde von §e" + p.getName() + " §7angenommen.");
                            p2.sendMessage("§bSupportChat §8» §7Um das Gespräch zu verlassen mache §e/support end");
                            HydroSlide.getTeamStatsRepository().addSupports(p.getUniqueId(), 1);
                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                                    if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Eine Supportanfrage wurde von " + p.getDisplayName() + " §7angenommen.");
                                        all.sendMessage(HydroSlide.getInstance().getPrefix() + "Anfrage von: " + p2.getName());
                                    }
                                });
                            }
                        } else {
                            p.sendMessage(HydroSlide.getInstance().getPrefix() + "Du bist bereits in einem Supportgespräch.");
                            p.sendMessage(HydroSlide.getInstance().getPrefix() + "Um dies zu verlassen nutze §e/support end");
                        }
                    } else {
                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDieser Spieler hat keine Anfrage gesendet.");
                    }
                } else {
                    p.sendMessage(HydroSlide.getInstance().getPlayerNotOnline());
                }
            }
        }
    }
}