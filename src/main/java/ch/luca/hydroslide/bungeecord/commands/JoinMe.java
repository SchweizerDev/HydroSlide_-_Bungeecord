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

public class JoinMe extends Command {

    public JoinMe(String command) {
        super(command);
    }

    private static HashMap<ProxiedPlayer, Long> time = new HashMap<>();

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(HydroSlide.getInstance().getNoPlayer());
            return;
        }
        ProxiedPlayer p = (ProxiedPlayer) sender;
        if(!p.hasPermission("hydroslide.joinme")) {
            p.sendMessage(HydroSlide.getInstance().getNoPermission());
            return;
        }
        long difference = System.currentTimeMillis() - time.get(p);
        if(args.length == 0) {
            time.computeIfAbsent(p, k -> 0L);
            if(System.currentTimeMillis() - time.get(p) < 60000L) {
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDu musst noch etwas warten...");
                return;
            }
            time.put(p, 0L);
            if(!p.getServer().getInfo().getName().equalsIgnoreCase("Lobby-01")
                    && !p.getServer().getInfo().getName().equalsIgnoreCase("SilentLobby-01")
                    && !p.getServer().getInfo().getName().equalsIgnoreCase("BauServer-01")
                    && !p.getServer().getInfo().getName().equalsIgnoreCase("BauServer-02")
                    && !p.getServer().getInfo().getName().equalsIgnoreCase("Vorbauen-01")) {
                for(ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                    TextComponent joinMe = new TextComponent("§7Klicke um den §cServer §7zu betreten!");
                    joinMe.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§8*§f§oKlick§8* §7Um den Server zu §ebetreten§7.").create()));
                    joinMe.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/Lolroflcoperxxdq " + p.getName()));
                    all.sendMessage(HydroSlide.getInstance().getPrefix() + p.getDisplayName() + " §7spielt jetzt auf §c" + p.getServer().getInfo().getName());
                    all.sendMessage(new TextComponent(HydroSlide.getInstance().getPrefix()), new TextComponent(new TextComponent("§8» "), new TextComponent(joinMe)));
                }
                time.put(p, System.currentTimeMillis());
             } else {
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cAuf diesem Server kannst du keine JoinMe-Nachricht senden.");
            }
        } else {
            p.sendMessage(HydroSlide.getInstance().getPrefixUse() + "joinme");
        }
    }
}
