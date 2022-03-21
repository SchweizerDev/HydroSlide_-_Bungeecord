package ch.luca.hydroslide.bungeecord.listener;

import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerKickListener implements Listener {

    @EventHandler
    public void onKick(ServerKickEvent e) {
        if(e.getPlayer().getServer().getInfo().getName() != "Lobby-01") {
            if(!e.getKickReason().startsWith(".0")) {
                e.setCancelled(true);
            }
        } else {
            e.setCancelled(true);
        }
    }
}
