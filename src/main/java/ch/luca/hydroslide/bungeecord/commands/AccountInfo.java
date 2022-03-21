package ch.luca.hydroslide.bungeecord.commands;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class AccountInfo extends Command {

    public AccountInfo(String command) {
        super(command);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(HydroSlide.getInstance().getNoPlayer());
            return;
        }
        ProxiedPlayer p = (ProxiedPlayer) sender;
        if (!p.hasPermission("hydroslide.accountinfo")) {
            sender.sendMessage(HydroSlide.getInstance().getNoPermission());
            return;
        }
        if (args.length == 1) {
            String name = args[0];
            HydroSlide.getPlayerInfoRepository().getUUID(name, uuid -> {
                if (uuid == null) {
                    p.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDieser Spieler war noch nie auf dem Server.");
                    return;
                }
                HydroSlide.getPlayerInfoRepository().getIP(uuid, ip -> {
                    if (ip == null) {
                        p.sendMessage(HydroSlide.getInstance().getPlayerNotOnline());
                        return;
                    }
                    HydroSlide.getPlayerInfoRepository().getNamesWithIP(ip, names -> {
                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "Registrierte Accounts auf die selbe §cIP-Adresse §7des angegebenen Spielers:");
                        for (String nameFromIp : names) {
                            p.sendMessage(HydroSlide.getInstance().getPrefix() + "§8- §e" + nameFromIp);
                        }
                    });
                });
            });
        } else {
            p.sendMessage(HydroSlide.getInstance().getPrefixUse() + "accountinfo <Spieler>");
        }
    }
}
