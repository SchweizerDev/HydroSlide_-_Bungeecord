package ch.luca.hydroslide.bungeecord.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Lolroflcoperxxdq extends Command {

    public Lolroflcoperxxdq(String command) {
        super(command);
    }

    @Override
    public void execute(CommandSender getSender, String[] getArgs) {
        if (getArgs.length == 1) {
            ProxiedPlayer p = (ProxiedPlayer) getSender;
            ProxiedPlayer pp = ProxyServer.getInstance().getPlayer(getArgs[0]);
            p.connect(pp.getServer().getInfo());
        }
    }
}
