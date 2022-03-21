package ch.luca.hydroslide.bungeecord.commands;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class TryJumpUnban extends Command {

    public TryJumpUnban(String command) {
        super(command);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(HydroSlide.getInstance().getNoPlayer());
            return;
        }
        ProxiedPlayer p = (ProxiedPlayer) sender;
        if(!p.hasPermission("hydroslide.tryjumpunban")) {
            p.sendMessage(HydroSlide.getInstance().getNoPermission());
            return;
        }
        if (args.length == 1) {
            String name = args[0];
            HydroSlide.getPlayerInfoRepository().getUUID(name, uuid -> {
                if(uuid == null) {
                    p.sendMessage(HydroSlide.getInstance().getPlayerNeverOnline());
                    return;
                }
                HydroSlide.getInstance().getTryJumpBanRepository().removeBan(uuid);
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "Du hast den Tryjumpban von §e" + name + " §7entfernt.");
            });
        } else {
            p.sendMessage(HydroSlide.getInstance().getPrefixUse() + "tryjumpunban <Spieler>");
        }
    }
}

