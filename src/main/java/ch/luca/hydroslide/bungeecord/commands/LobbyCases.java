package ch.luca.hydroslide.bungeecord.commands;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Subcommand;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import lombok.AllArgsConstructor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

@AllArgsConstructor
@CommandAlias("lobbycases")
public class LobbyCases extends BaseCommand {

    private final HydroSlide hydroSlide;

    @Default
    @HelpCommand
    public void onHelp(CommandSender commandSender) {
        if (commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) commandSender;
            if (!player.hasPermission("hydroslide.lobbycases")) {
                player.sendMessage(HydroSlide.getInstance().getNoPermission());
                return;
            }
        }
        commandSender.sendMessage(HydroSlide.getInstance().getPrefixUse() + "lobbycases <add|remove> <Name> <Anzahl>");
    }

    @Subcommand("add")
    public void onAddCases(CommandSender commandSender, @Default() String name, @Default("1") String amount) {
        if (commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) commandSender;
            if (!player.hasPermission("hydroslide.lobbycases")) {
                player.sendMessage(HydroSlide.getInstance().getNoPermission());
                return;
            }
        }
        if (name.isEmpty()) {
            commandSender.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDu musst ein Namen angeben.");
            return;
        }
        int caseAmount;
        try {
            caseAmount = Integer.parseInt(amount);
        } catch (NumberFormatException e) {
            commandSender.sendMessage(HydroSlide.getInstance().getPrefix() + "§cGib eine Zahl an.");
            return;
        }
        ProxyServer.getInstance().getScheduler().runAsync(this.hydroSlide, () -> {
            Futures.addCallback(this.hydroSlide.getProfileRepository().getUniqueId(name), new FutureCallback<UUID>() {
                @Override
                public void onSuccess(UUID uuid) {
                    if (uuid == null) {
                        try {
                            uuid = hydroSlide.getNameToUUIDResolver().resolve(name);
                        } catch (Exception e) {
                            commandSender.sendMessage(HydroSlide.getInstance().getPrefix() + "§cEine UUID zum Namen wurde nicht gefunden.");
                            return;
                        }
                    }
                    Futures.addCallback(hydroSlide.getLobbyCaseRepository().addCases(uuid, caseAmount), new FutureCallback<Integer>() {
                        @Override
                        public void onSuccess(Integer integer) {
                            commandSender.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7hat §6" + caseAmount + " §6Lobbycases §7erhalten.");
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            throwable.printStackTrace();
                            commandSender.sendMessage(HydroSlide.getInstance().getPrefix() + "§cEs ist ein Fehler aufgetreten.");
                        }
                    });
                }

                @Override
                public void onFailure(Throwable throwable) {
                    throwable.printStackTrace();
                    commandSender.sendMessage(HydroSlide.getInstance().getPrefix() + "§cEs ist ein Fehler aufgetreten.");
                }
            });
        });
    }

    @Subcommand("remove")
    public void onRemoveCases(CommandSender commandSender, @Default() String name, @Default("1") String amount) {
        if (commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) commandSender;
            if (!player.hasPermission("hydroslide.lobbycases")) {
                player.sendMessage(HydroSlide.getInstance().getNoPermission());
                return;
            }
        }
        if (name.isEmpty()) {
            commandSender.sendMessage(HydroSlide.getInstance().getPrefix() + "§cDu musst ein Namen angeben.");
            return;
        }
        int caseAmount;
        try {
            caseAmount = Integer.parseInt(amount);
        } catch (NumberFormatException e) {
            commandSender.sendMessage(HydroSlide.getInstance().getPrefix() + "§cGib eine Zahl an.");
            return;
        }
        ProxyServer.getInstance().getScheduler().runAsync(this.hydroSlide, () -> {
            Futures.addCallback(this.hydroSlide.getProfileRepository().getUniqueId(name), new FutureCallback<UUID>() {
                @Override
                public void onSuccess(UUID uuid) {
                    if (uuid == null) {
                        try {
                            uuid = hydroSlide.getNameToUUIDResolver().resolve(name);
                        } catch (Exception e) {
                            commandSender.sendMessage(HydroSlide.getInstance().getPrefix() + "§cEine UUID zum Namen wurde nicht gefunden.");
                            return;
                        }
                    }
                    Futures.addCallback(hydroSlide.getLobbyCaseRepository().removeCases(uuid, caseAmount), new FutureCallback<Integer>() {
                        @Override
                        public void onSuccess(Integer integer) {
                            commandSender.sendMessage(HydroSlide.getInstance().getPrefix() + "Dem Spieler §e" + name + " §7wurden §6" + caseAmount + " §6Lobbycases §7entfernt.");
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            throwable.printStackTrace();
                            commandSender.sendMessage(HydroSlide.getInstance().getPrefix() + "§cEs ist ein Fehler aufgetreten.");
                        }
                    });
                }

                @Override
                public void onFailure(Throwable throwable) {
                    throwable.printStackTrace();
                    commandSender.sendMessage(HydroSlide.getInstance().getPrefix() + "§cEs ist ein Fehler aufgetreten.");
                }
            });
        });
    }
}
