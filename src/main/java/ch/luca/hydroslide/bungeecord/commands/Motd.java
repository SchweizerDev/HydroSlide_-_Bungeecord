package ch.luca.hydroslide.bungeecord.commands;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import ch.luca.hydroslide.bungeecord.config.MotdConfig;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Motd extends Command {

    public Motd(String command) {
        super(command);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(HydroSlide.getInstance().getNoPlayer());
            return;
        }
        ProxiedPlayer p = (ProxiedPlayer) sender;
        if (!p.hasPermission("hydroslide.motd")) {
            p.sendMessage(HydroSlide.getInstance().getNoPermission());
            return;
        }
        if (!(args.length < 4)) {
            if (args[0].equalsIgnoreCase("set")) {
                if (args[1].equalsIgnoreCase("normal")) {
                    if (args[2].equalsIgnoreCase("1")) {
                        String m1 = "";
                        for (int i = 3; i < args.length; i++) {
                            m1 = m1 + args[i] + " ";
                        }
                        MotdConfig.setMotdNormal1(m1);
                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "Du hast die erste Linie der Normalen Motd gesetzt.");
                    } else if (args[2].equalsIgnoreCase("2")) {
                        String m2 = "";
                        for (int i = 3; i < args.length; i++) {
                            m2 = m2 + args[i] + " ";
                        }
                        MotdConfig.setMotdNormal2(m2);
                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "Du hast die zweite Linie der Normalen Motd gesetzt.");
                    }
                } else if (args[1].equalsIgnoreCase("wartung")) {
                    if (args[2].equalsIgnoreCase("1")) {
                        String m1 = "";
                        for (int i = 3; i < args.length; i++) {
                            m1 = m1 + args[i] + " ";
                        }
                        MotdConfig.setMotdMaintenance1(m1);
                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "Du hast die erste Linie der Wartungs Motd gesetzt.");
                    } else if (args[2].equalsIgnoreCase("2")) {
                        String m2 = "";
                        for (int i = 3; i < args.length; i++) {
                            m2 = m2 + args[i] + " ";
                        }
                        MotdConfig.setMotdMaintenance2(m2);
                        p.sendMessage(HydroSlide.getInstance().getPrefix() + "Du hast die zweite Linie der Wartungs Motd gesetzt.");
                    }
                }
            } else {
                p.sendMessage(HydroSlide.getInstance().getPrefixUse() + "motd set <reload|normal|wartung> [1|2] [Text]");
            }
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                MotdConfig.loadMotd();
                p.sendMessage(HydroSlide.getInstance().getPrefix() + "Die Motd wurde neu geladen.");
            }
        } else {
            p.sendMessage(HydroSlide.getInstance().getPrefixUse() + "motd set <reload|normal|wartung> [1|2] [Text]");
        }
    }
}
