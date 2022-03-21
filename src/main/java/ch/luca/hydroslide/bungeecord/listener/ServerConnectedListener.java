package ch.luca.hydroslide.bungeecord.listener;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import ch.luca.hydroslide.bungeecord.commands.Party;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerConnectedListener implements Listener {

    @EventHandler
    public void onChangeServerwithParty(ServerConnectedEvent e) {
        ProxiedPlayer p = e.getPlayer();
        if (Party.party.containsKey(p.getName())
                && !e.getServer().getInfo().getName().equalsIgnoreCase("Lobby-01")
                && !e.getServer().getInfo().getName().equalsIgnoreCase("SilentLobby-01")
                && !e.getServer().getInfo().getName().equalsIgnoreCase("BauServer-01")
                && !e.getServer().getInfo().getName().equalsIgnoreCase("BauServer-02")
                && !e.getServer().getInfo().getName().equalsIgnoreCase("Vorbauen-01")) {
            ArrayList<String> party = Party.party.get(p.getName());
            Iterator<String> iterator = party.iterator();
            AtomicInteger taskId = new AtomicInteger();
            taskId.set(ProxyServer.getInstance().getScheduler().schedule(HydroSlide.getInstance(), () -> {
                if(iterator.hasNext()) {
                    ProxiedPlayer pls = ProxyServer.getInstance().getPlayer(iterator.next());
                    pls.sendMessage(HydroSlide.getInstance().getPrefix() + "Die Party betritt den Server §e" + e.getServer().getInfo().getName() + "§7...");
                    if (pls != p) {
                        pls.connect(e.getServer().getInfo());
                    }
                } else {
                    ProxyServer.getInstance().getScheduler().cancel(taskId.get());
                }
            }, 0L, 250L, TimeUnit.MILLISECONDS).getId());
        }
    }
}