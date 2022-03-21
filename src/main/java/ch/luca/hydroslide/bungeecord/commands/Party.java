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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Party extends Command {

    public Party(String command) {
        super(command);
    }

    public static HashMap<String, ArrayList<String>> party = new HashMap<>();
    public static HashMap<String, String> isparty = new HashMap<>();
    public static HashMap<String, ArrayList<String>> partyinvite = new HashMap<>();

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(HydroSlide.getInstance().getNoPlayer());
            return;
        }
        final ProxiedPlayer p = (ProxiedPlayer)sender;
        if(args.length == 2) {
            if(args[0].equalsIgnoreCase("invite")) {
                if(p.getName().equalsIgnoreCase(args[1])) {
                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDu kannst dich nicht selber in eine Party einladen.");
                    return;
                }
                if(isparty.containsKey(p.getName()) && !isparty.get(p.getName()).equalsIgnoreCase(p.getName())) {
                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDu bist bereits in einer Party.");
                    return;
                }
                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[1]);
                if(target == null) {
                    p.sendMessage(HydroSlide.getInstance().getPlayerNotOnline());
                    return;
                }
                if(isparty.containsKey(target.getName())) {
                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDieser Spieler ist bereits in einer Party.");
                    return;
                }
                if(!partyinvite.containsKey(target.getName())) {
                    partyinvite.put(target.getName(), new ArrayList<String>());
                }
                ArrayList<String> anfragen = partyinvite.get(target.getName());
                if(anfragen.contains(p.getName())) {
                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDu hast diesen Spieler bereits eingeladen.");
                    return;
                }
                if(!party.containsKey(p.getName())) {
                    ArrayList<String> newparty = new ArrayList<String>();
                    party.put(p.getName(), newparty);
                    isparty.put(p.getName(), p.getName());
                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "Du hast eine Party erstellt.");
                }
                ArrayList<String> party2 = party.get(p.getName());
                if(!party2.contains(p.getName())) {
                    party2.add(p.getName());
                }
                party.put(p.getName(), party2);
                anfragen.add(p.getName());
                partyinvite.put(target.getName(), anfragen);
                target.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + p.getName() + " §7hat dich in eine §5Party §7eingeladen.");
                TextComponent accept = new TextComponent("§7[§aAnnehmen§7]");
                accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party accept " + p.getName()));
                TextComponent deny = new TextComponent("§7[§cAblehnen§7]");
                deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party deny " + p.getName()));
                target.sendMessage(new TextComponent(HydroSlide.getInstance().getPrefix() + "Funktionen: "), new TextComponent(accept), new TextComponent(" §7| "), new TextComponent(deny));
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "Du hast §e" + target.getName() + " §7in die Party eingeladen.");
                ProxyServer.getInstance().getScheduler().schedule(HydroSlide.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        if(p != null) {
                            for(ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                if(partyinvite.containsKey(all.getName())) {
                                    ArrayList<String> anfragen = partyinvite.get(all.getName());
                                    anfragen.remove(p.getName());
                                    partyinvite.put(all.getName(), anfragen);
                                }
                            }
                            if(party.containsKey(p.getName()) && party.get(p.getName()).size() <= 1) {
                                party.remove(p.getName());
                                isparty.remove(p.getName());
                                p.sendMessage(HydroSlide.getInstance().getPrefix() + "Die Party wurde aufgelöst.");
                            }
                        }
                    }
                }, 150, TimeUnit.SECONDS);
            } else if(args[0].equalsIgnoreCase("kick") || args[0].equalsIgnoreCase("remove")) {
                if (isparty.containsKey(p.getName()) && isparty.get(p.getName()).equalsIgnoreCase(p.getName()) && party.containsKey(p.getName())) {
                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[1]);
                    if (target == null) {
                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDer Spieler §e" + args[1] + " §cist nicht in deiner Party.");
                        return;
                    }
                    ArrayList<String> party2 = party.get(p.getName());
                    if (party2.contains(target.getName())) {
                        party2.remove(target.getName());
                        target.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDu wurdest aus der Party gekickt.");
                        for (String players : party2) {
                            ProxiedPlayer pls = ProxyServer.getInstance().getPlayer(players);
                            if (pls != null) {
                                pls.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + target.getName() + " §7wurde aus der Party gekickt.");
                            }
                        }
                        party.put(p.getName(), party2);
                        final ProxiedPlayer target2 = ProxyServer.getInstance().getPlayer(isparty.get(target.getName()));
                        ProxyServer.getInstance().getScheduler().schedule(HydroSlide.getInstance(), new Runnable() {
                            @Override
                            public void run() {
                                if (target2 != null && party.containsKey(target2.getName())) {
                                    if (party.get(target2.getName()).size() <= 1) {
                                        party.remove(target2.getName());
                                        isparty.remove(target2.getName());
                                        target2.sendMessage(HydroSlide.getInstance().getPrefix() + "Die Party wurde aufgelöst.");
                                    }
                                }
                            }
                        }, 150, TimeUnit.SECONDS);
                        isparty.remove(target.getName());
                    } else {
                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDer Spieler §e" + args[1] + " §cist nicht in deiner Party.");
                    }
                } else {
                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDu bist nicht der Party Inhaber.");
                }
            } else if(args[0].equalsIgnoreCase("accept")) {
                if (partyinvite.containsKey(p.getName())) {
                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[1]);
                    if (target == null) {
                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDu wurdest von §e" + args[1] + " §cin keine Party eingeladen.");
                        return;
                    }
                    if (isparty.containsKey(p.getName())) {
                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDu bist bereits in einer Party.");
                        return;
                    }
                    ArrayList<String> reqlist = partyinvite.get(p.getName());
                    if (reqlist.size() > 0) {
                        if (reqlist.contains(target.getName())) {
                            reqlist.remove(target.getName());
                            partyinvite.put(p.getName(), reqlist);
                            if (!party.containsKey(target.getName())) {
                                ArrayList<String> newparty = new ArrayList<>();
                                newparty.add(p.getName());
                                party.put(target.getName(), newparty);
                                isparty.put(p.getName(), p.getName());
                            }
                            ArrayList<String> party2 = party.get(target.getName());
                            for (String players : party2) {
                                ProxiedPlayer pls = ProxyServer.getInstance().getPlayer(players);
                                pls.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + p.getName() + " §7hat die Partyeinladung angenommen.");
                            }
                            party2.add(p.getName());
                            party.put(target.getName(), party2);
                            isparty.put(p.getName(), target.getName());
                            p.sendMessage(HydroSlide.getInstance().getPrefix() + "Du bist der Party von §e" + target.getName() + " §7beigetreten.");
                        } else {
                            p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDu wurdest von §e" + target.getName() + " §cin keine Party eingeladen.");
                        }
                    } else {
                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDu wurdest von §e" + target.getName() + " §cin keine Party eingeladen.");
                    }
                } else {
                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDu wurdest von §e" + args[1] + " §cin keine Party eingeladen.");
                }
            } else if(args[0].equalsIgnoreCase("deny")) {
                if (partyinvite.containsKey(p.getName())) {
                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[1]);
                    if (target == null) {
                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDu wurdest von §e" + args[1] + " §cin keine Party eingeladen.");
                        return;
                    }
                    if (isparty.containsKey(p.getName())) {
                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDu bist bereits in einer Party.");
                        return;
                    }
                    ArrayList<String> reqlist = partyinvite.get(p.getName());

                    if (reqlist.size() > 0) {
                        if (reqlist.contains(target.getName())) {
                            reqlist.remove(target.getName());
                            partyinvite.put(p.getName(), reqlist);
                            target.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + p.getName() + " §7hat deine Party Anfrage abgelehnt.");
                            p.sendMessage(HydroSlide.getInstance().getPrefix() + "Du hast die Party Anfrage von §e" + target.getName() + " §7abgelehnt.");
                        } else {
                            p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDu wurdest von §e" + target.getName() + " §cin keine Party eingeladen.");
                        }
                    } else {
                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDu wurdest von §e" + target.getName() + " §cin keine Party eingeladen.");
                    }
                } else {
                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDu wurdest von §e" + args[1] + " §cin keine Party eingeladen.");
                }
            } else {
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "/party help");
            }
        } else if(args.length == 1) {
            if(args[0].equalsIgnoreCase("help")) {
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "§e/party invite <Spieler> §8- §7Lädt jemand in die Party ein");
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "§e/party accept <Spieler> §8- §7Nimmt die Anfrage an");
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "§e/party deny <Spieler> §8- §7Lehnt die Anfrage ab");
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "§e/party leave §8- §7Verlässt die aktuelle Party");
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "§e/party kick <Spieler> §8- §7Kickt jemanden aus der Party");
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "§e/party list §8- §7Zeigt alle Mitglieder der Party");
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "§e/pc <Nachricht> §8- §7Schreibe im Partychat");
            } else if(args[0].equalsIgnoreCase("leave")) {
                if(!isparty.containsKey(p.getName())) {
                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDu bist in keiner Party");
                    return;
                }
                if(party.containsKey(p.getName()) && isparty.get(p.getName()).equalsIgnoreCase(p.getName())) {
                    ArrayList<String> party2 = party.get(p.getName());
                    for(String player : party2) {
                        ProxiedPlayer pl = ProxyServer.getInstance().getPlayer(player);
                        isparty.remove(pl.getName());
                        if(pl != p) {
                            pl.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDie Party wurde aufgelöst.");
                        } else {
                            pl.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDie Party wurde aufgelöst.");
                        }
                    }
                    party.remove(p.getName());
                } else {
                    ArrayList<String> party2 = party.get(isparty.get(p.getName()));
                    party2.remove(p.getName());
                    for(String player : party2) {
                        ProxiedPlayer pl = ProxyServer.getInstance().getPlayer(player);
                        if(pl != null) {
                            pl.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + p.getName() + " §7hat die Party verlassen.");
                        }
                    }
                    party.put(isparty.get(p.getName()), party2);
                    final ProxiedPlayer target2 = ProxyServer.getInstance().getPlayer(isparty.get(p.getName()));
                    isparty.remove(p.getName());
                    ProxyServer.getInstance().getScheduler().schedule(HydroSlide.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            if(target2 != null && party.containsKey(target2.getName())) {
                                if(party.get(target2.getName()).size() <= 1) {
                                    party.remove(target2.getName());
                                    isparty.remove(target2.getName());
                                    target2.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDie Party wurde aufgelöst.");
                                }
                            }
                        }
                    }, 150, TimeUnit.SECONDS);
                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "Du hast die Party verlassen.");
                }
            } else if(args[0].equalsIgnoreCase("list")) {
                if(!isparty.containsKey(p.getName())) {
                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDu bist in keiner Party.");
                    return;
                }
                String owner = isparty.get(p.getName());
                ArrayList<String> party2 = party.get(owner);
                String liste = "";
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "Party Besitzer: §c" + owner);
                for(String player : party2) {
                    if(!player.equalsIgnoreCase(owner)) {
                        liste = liste + "§7, §e" + player;
                    }
                }
                if(liste.equalsIgnoreCase("")) {
                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cKeine Mitglieder");
                } else {
                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "Party Mitglieder: §e" + liste.substring(4));
                }
            } else {
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "§e/party invite <Spieler> §8- §7Lädt jemand in die Party ein");
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "§e/party accept <Spieler> §8- §7Nimmt die Anfrage an");
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "§e/party deny <Spieler> §8- §7Lehnt die Anfrage ab");
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "§e/party leave §8- §7Verlässt die aktuelle Party");
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "§e/party kick <Spieler> §8- §7Kickt jemanden aus der Party");
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "§e/party list §8- §7Zeigt alle Mitglieder der Party");
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "§e/pc <Nachricht> §8- §7Schreibe im Partychat");
            }
        } else {
            p.sendMessage(HydroSlide.getInstance().getPrefix() + "§e/party invite <Spieler> §8- §7Lädt jemand in die Party ein");
            p.sendMessage(HydroSlide.getInstance().getPrefix() + "§e/party accept <Spieler> §8- §7Nimmt die Anfrage an");
            p.sendMessage(HydroSlide.getInstance().getPrefix() + "§e/party deny <Spieler> §8- §7Lehnt die Anfrage ab");
            p.sendMessage(HydroSlide.getInstance().getPrefix() + "§e/party leave §8- §7Verlässt die aktuelle Party");
            p.sendMessage(HydroSlide.getInstance().getPrefix() + "§e/party kick <Spieler> §8- §7Kickt jemanden aus der Party");
            p.sendMessage(HydroSlide.getInstance().getPrefix() + "§e/party list §8- §7Zeigt alle Mitglieder der Party");
            p.sendMessage(HydroSlide.getInstance().getPrefix() + "§e/pc <Nachricht> §8- §7Schreibe im Partychat");
        }
    }
}
