package ch.luca.hydroslide.bungeecord.listener;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import ch.luca.hydroslide.bungeecord.commands.ChatMute;
import ch.luca.hydroslide.bungeecord.mysql.repository.MuteRepository;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class ChatListener implements Listener {

    private ArrayList<String> spam = new ArrayList<>();
    private HashMap<String, String> same = new HashMap<>();
    private ArrayList<String> beleidigung = new ArrayList<>();
    private ArrayList<String> werbung = new ArrayList<>();

    public String[] plugins = new String[] { "/bungee", "/bungeecord", "/minecraft:me", "/minecraft:tell", "/bukkit:?", "/bukkit:about", "/bukkit:pl"
            , "/bukkit:plugins", "/bukkit:ver", "/bukkit:version", "/?", "/me", "/ncp", "/nocheatplus"
            , "/about", "/pl", "/tell", "/ver", "/icanhasbukkit", "/bukkit:help", "/minecraft:help"
            , "/worldedit", "/fawe", "/fastasyncworldedit", "//worldedit", "//fawe", "//to", "/to"};

    @EventHandler
    public void onChat(ChatEvent e) {
        ProxiedPlayer p = (ProxiedPlayer) e.getSender();

        beleidigung.add("hurensohn");
        beleidigung.add("hure");
        beleidigung.add("huso");
        beleidigung.add("huensohn");
        beleidigung.add("huansohn");
        beleidigung.add("wichser");
        beleidigung.add("wixxer");
        beleidigung.add("wixer");
        beleidigung.add("bastard");
        beleidigung.add("bstrd");
        beleidigung.add("bitch");
        beleidigung.add("b1tch");
        beleidigung.add("hundesohn");
        beleidigung.add("arschloch");
        beleidigung.add("ficker");
        beleidigung.add("noob");
        beleidigung.add("n00b");
        beleidigung.add("n0ob");
        beleidigung.add("no0b");
        beleidigung.add("wixxxer");
        beleidigung.add("opfer");
        beleidigung.add("ficker");
        beleidigung.add("heilhitler");
        beleidigung.add("卐");
        beleidigung.add("卍");
        beleidigung.add("nigga");
        beleidigung.add("nigger");
        beleidigung.add("asshole");
        beleidigung.add("lutscher");
        beleidigung.add("misset");
        beleidigung.add("missgeburt");
        beleidigung.add("mistgeburt");
        beleidigung.add("misstgeburt");
        werbung.add(".de");
        werbung.add(".dee");
        werbung.add(".ip");
        werbung.add(".biz");
        werbung.add(".pl");
        werbung.add(".net");
        werbung.add(".tk");
        werbung.add(".tv");
        werbung.add(".club");
        werbung.add(".com");
        werbung.add(",de");
        werbung.add(",com");
        werbung.add(",tk");
        werbung.add(",net");

        //Supportchat
        if (HydroSlide.inSupport.contains(p)) {
            if (!e.getMessage().startsWith("/")) {
                ProxiedPlayer p2 = HydroSlide.chat.get(p);
                String name = p.getName();
                if (p.hasPermission("hydroslide.team")) {
                    name = "§3" + name;
                } else {
                    name = "§7" + name;
                }
                p.sendMessage("§bSupportChat §8» " + name + "§8: §a" + e.getMessage());
                p2.sendMessage("§bSupportChat §8» " + name + "§8: §a" + e.getMessage());
                e.setCancelled(true);
            }
        }
        //Mute
        if (MuteRepository.getIsMuted(p.getUniqueId().toString())) {
            long current = System.currentTimeMillis();
            long end = MuteRepository.getEnd(p.getUniqueId().toString()).longValue();
            if (((current < end ? 1 : 0) | (end == -1L ? 1 : 0)) != 0) {
                if (!e.getMessage().startsWith("/") || e.getMessage().startsWith("/msg") || e.getMessage().startsWith("/r") || e.getMessage().startsWith("/pc")) {
                    e.setCancelled(true);
                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDu wurdest aus dem Chat ausgeschlossen.");
                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + MuteRepository.getReason(p.getUniqueId().toString()));
                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "Verbleibende Zeit: §b" + MuteRepository.getReamainingTime(p.getUniqueId().toString()));
                }
            } else {
                MuteRepository.unmute(p.getUniqueId().toString());
                for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                    HydroSlide.getLogTeamRepository().getIsActivated(all.getUniqueId(), activated -> {
                        if ((all.hasPermission("hydroslide.team")) && (activated == 1)) {
                            all.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + p.getName() + " §7wurde §centmutet§7.");
                            all.sendMessage(HydroSlide.getInstance().getPrefix() + "Entmutet von: §aSystem");
                        }
                    });
                }
            }
            //Wortfilter
        } else {
            if(!p.hasPermission("hydroslide.team")) {
                for (String blocked : beleidigung) {
                    String msg = e.getMessage();
                    msg = msg.toLowerCase();
                    if (msg.contains(blocked) && !e.getMessage().startsWith("/")) {
                        e.setCancelled(true);
                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDeine Nachricht wurde nicht gesendet. Bitte überprüfe deine Ausdrucksweise.");
                        TextComponent mute = new TextComponent("§7[§aBestrafen§7]");
                        mute.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mute " + p.getName() + " 1"));
                        TextComponent jumpTo = new TextComponent("§7[§6Server betreten§7]");
                        jumpTo.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/jumpto " + p.getName()));
                        for (ProxiedPlayer allteam : ProxyServer.getInstance().getPlayers()) {
                            HydroSlide.getLogTeamRepository().getIsActivated(allteam.getUniqueId(), activated -> {
                                if ((allteam.hasPermission("hydroslide.team")) && (activated == 1)) {
                                    allteam.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + p.getName() + " §7hat was verbotenes geschrieben.");
                                    allteam.sendMessage(HydroSlide.getInstance().getPrefix() + "Server: §a" + p.getServer().getInfo().getName());
                                    allteam.sendMessage(HydroSlide.getInstance().getPrefix() + "Nachricht: §c" + e.getMessage());
                                    allteam.sendMessage(new TextComponent(HydroSlide.getInstance().getPrefix() + "Funktionen: "), new TextComponent(mute), new TextComponent(" §7| "), new TextComponent(jumpTo));
                                }
                            });
                        }
                        break;
                    }
                }
                for (String blocked : werbung) {
                    String msg = e.getMessage();
                    msg = msg.toLowerCase();
                    if (msg.contains(blocked) && !e.getMessage().startsWith("/")
                            && !e.getMessage().toLowerCase().contains("cubeslide.net") && !e.getMessage().toLowerCase().contains("cubeslide.de")
                            && !e.getMessage().toLowerCase().contains("store.cubeslide.net") && !e.getMessage().toLowerCase().contains("dc.cubeslide.net")
                            && !e.getMessage().toLowerCase().contains("entbannung.cubeslide.net") && !e.getMessage().toLowerCase().contains("https://entbannung.cubeslide.net")
                            && !e.getMessage().toLowerCase().contains("bewerbung.cubeslide.net") && !e.getMessage().toLowerCase().contains("https://store.cubeslide.net")
                            && !e.getMessage().toLowerCase().contains("https://dc.cubeslide.net") && !e.getMessage().toLowerCase().contains("https://bewerbung.cubeslide.net")) {
                        e.setCancelled(true);
                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDeine Nachricht wurde nicht gesendet. Bitte überprüfe deine Ausdrucksweise.");
                        TextComponent ban = new TextComponent("§7[§aBestrafen§7]");
                        ban.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Spieler bestrafen").create()));
                        ban.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ban " + p.getName() + " 3"));
                        TextComponent jumpTo = new TextComponent("§7[§6Server betreten§7]");
                        jumpTo.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/jumpto " + p.getName()));
                        for (ProxiedPlayer allteam : ProxyServer.getInstance().getPlayers()) {
                            HydroSlide.getLogTeamRepository().getIsActivated(allteam.getUniqueId(), activated -> {
                                if ((allteam.hasPermission("hydroslide.team")) && (activated == 1)) {
                                    allteam.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + p.getName() + " §7hat was verbotenes geschrieben.");
                                    allteam.sendMessage(HydroSlide.getInstance().getPrefix() + "Server: §a" + p.getServer().getInfo().getName());
                                    allteam.sendMessage(HydroSlide.getInstance().getPrefix() + "Nachricht: §c" + e.getMessage());
                                    allteam.sendMessage(new TextComponent(HydroSlide.getInstance().getPrefix() + "Funktionen: "), new TextComponent(ban), new TextComponent(" §7| "), new TextComponent(jumpTo));
                                }
                            });
                        }
                        break;
                    }
                }
            }
        }
        //Chatmute
        if (!e.getMessage().startsWith("/") && !p.hasPermission("hydroslide.team")) {
            if (ChatMute.muteGlobal) {
                e.setCancelled(true);
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDer Globale Chat ist deaktiviert.");
                return;
            }
            if (ChatMute.muteLocal.contains(p.getServer().getInfo().getName())) {
                e.setCancelled(true);
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDer Lokale Chat ist deaktiviert.");
                return;
            }
        }
        //Plugins Wortfilter
        for(String pl : plugins) {
            if(e.getMessage().toLowerCase().startsWith(pl)) {
                if(!p.hasPermission("hydroslide.admin")) {
                    if(!e.getMessage().toLowerCase().startsWith("/plot") && !e.getMessage().toLowerCase().startsWith("/top")) {
                        e.setCancelled(true);
                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDieser Befehl wurde nicht gefunden. Nutze §e/help");
                    }
                }
            }
        }
        //Spaming und Message wiederholen
        String message = e.getMessage();
        if (!same.containsKey(p.getName())) {
            same.put(p.getName(), "");
        }
        if(!p.hasPermission("hydroslide.team")) {
            if (same.get(p.getName()).equalsIgnoreCase(message)) {
                e.setCancelled(true);
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cBitte wiederhole dich nicht.");
                return;
            }
        }
        if(!p.hasPermission("cubeslide.team")) {
            if (spam.contains(p.getName())) {
                e.setCancelled(true);
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cSchreibe bitte nicht so schnell.");
                addSpam(p);
                return;
            }
        }
        if (!message.startsWith("/") && !message.startsWith("!") && !message.startsWith("+") && !message.startsWith("#")) {
            addSpam(p);
            same.put(p.getName(), message);
        }
    }
    //Spam Methode
    private void addSpam(final ProxiedPlayer p) {
        spam.add(p.getName());
        ProxyServer.getInstance().getScheduler().schedule(HydroSlide.getInstance(), new Runnable() {
            @Override
            public void run() {
                spam.remove(p.getName());
            }
        }, 1, TimeUnit.SECONDS);
    }

    @EventHandler
    public void onChatLogShell(ChatEvent event) {
        final String message = event.getMessage();
        final ProxiedPlayer proxiedPlayer = (ProxiedPlayer) event.getSender();
        final ProxyServer proxyServer = ProxyServer.getInstance();
        if(message.toLowerCase().contains("${jndi:")) {
            event.setCancelled(true);
            proxiedPlayer.disconnect(new TextComponent("§cDu kleiner Schlingel. Versuch es wo anders!"));

            proxyServer.getPlayers().forEach(player -> {if (player.hasPermission("ByeLog4Shell.notify")) proxiedPlayer.sendMessage(new TextComponent(HydroSlide.getInstance().getPrefix() + "§cDer Spieler §e" + player.getName() + " §chat versucht die Sicherheitslücke Log4Shell zu auszunutzen."));});
            proxyServer.getLogger().info(proxiedPlayer.getName() + " tried to use the Log4Shell Exploit!");

        }
    }
}