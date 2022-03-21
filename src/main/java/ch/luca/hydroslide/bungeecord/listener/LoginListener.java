package ch.luca.hydroslide.bungeecord.listener;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import ch.luca.hydroslide.bungeecord.config.MaintenanceConfig;
import de.crafter75.perms.bungee.api.BungeeRankAPI;
import de.crafter75.perms.global.rank.Rank;
import de.crafter75.perms.global.rank.RankManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class LoginListener implements Listener {

    @EventHandler
    public void onLogin(LoginEvent e) {
        e.registerIntent(HydroSlide.getInstance());
        ProxyServer.getInstance().getScheduler().runAsync(HydroSlide.getInstance(), () -> {
            if(MaintenanceConfig.getMaintenance() == true) {
                Rank rank = BungeeRankAPI.getRankSync(e.getConnection().getUniqueId());
                if(!rank.isHigherEqualsLevel(RankManager.getRank("Helpmate", false))) {
                    e.setCancelled(true);
                    e.setCancelReason("§bHydroSlide §7× §eNetzwerk \n §cDer Server befindet sich momentan in Wartungsarbeiten. \n §7Versuche es später erneut. \n\n §7Discord: §ediscord.hydroslide.eu");
                    e.completeIntent(HydroSlide.getInstance());
                    return;
                }
            }
            e.completeIntent(HydroSlide.getInstance());
        });
    }
}
