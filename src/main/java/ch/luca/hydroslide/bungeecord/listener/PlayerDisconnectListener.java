package ch.luca.hydroslide.bungeecord.listener;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import ch.luca.hydroslide.bungeecord.commands.Party;
import ch.luca.hydroslide.bungeecord.mysql.repository.OnlineTimeRepository;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class PlayerDisconnectListener implements Listener {

    @EventHandler
    public void onDisconnectSupport(PlayerDisconnectEvent e) {
        ProxiedPlayer p = e.getPlayer();
        //Platime
        OnlineTimeRepository.onQuit(p);
        //Report
        HydroSlide.reportreason.remove(p);
        HydroSlide.reportchatlog.remove(p);
        //Support
        if (HydroSlide.inSupport.contains(p)) {
            HydroSlide.inSupport.remove(p);
            HydroSlide.inSupport.remove(HydroSlide.chat.get(p));
            HydroSlide.chat.get(p).sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + p.getName() + " §7hat den Server verlassen. Der Support wurde geschlossen.");
            HydroSlide.chat.remove(HydroSlide.chat.get(p));
            HydroSlide.chat.remove(p);
        }
        if (HydroSlide.Supporting.contains(p)) {
            HydroSlide.Supporting.remove(p);
        }
        //Party
        if (Party.isparty.containsKey(p.getName())) {
            if (Party.party.containsKey(p.getName())) {
                ArrayList<String> party = Party.party.get(p.getName());
                for (String players : party) {
                    ProxiedPlayer pls = ProxyServer.getInstance().getPlayer(players);
                    Party.isparty.remove(pls.getName());
                    if (pls != p) {
                        pls.sendMessage(HydroSlide.getInstance().getPrefix() + "Die Party wurde aufgelöst.");
                    }
                }
                Party.party.remove(p.getName());
            } else {
                ArrayList<String> party = Party.party.get(Party.isparty.get(p.getName()));
                party.remove(p.getName());
                for (String players : party) {
                    ProxiedPlayer pls = ProxyServer.getInstance().getPlayer(players);
                    if (pls != null) {
                        pls.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDer Spieler §e" + p.getName() + " §7hat die Party §cverlassen§7.");
                    }
                }
                Party.party.put(Party.isparty.get(p.getName()), party);
                final ProxiedPlayer target2 = ProxyServer.getInstance().getPlayer(Party.isparty.get(p.getName()));
                Party.isparty.remove(p.getName());
                ProxyServer.getInstance().getScheduler().schedule(HydroSlide.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        if (target2 != null && Party.party.containsKey(target2.getName())) {
                            if (Party.party.get(target2.getName()).size() <= 0) {
                                Party.party.remove(target2.getName());
                                Party.isparty.remove(target2.getName());
                                target2.sendMessage(HydroSlide.getInstance().getPrefix() + "Deine Party wurde aufgelöst.");
                            }
                        }
                    }
                }, 150, TimeUnit.SECONDS);
            }
        }
    }
}
