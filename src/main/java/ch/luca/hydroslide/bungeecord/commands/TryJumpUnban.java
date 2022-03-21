package ch.luca.hydroslide.bungeecord.commands;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.HelpCommand;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import de.crafter75.perms.bungee.BungeePerms;
import lombok.AllArgsConstructor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

@AllArgsConstructor
@CommandAlias("tryjumpunban")
public class TryJumpUnban extends BaseCommand {

    private final HydroSlide hydroSlide;

    @Default
    @HelpCommand
    public void onHelp(CommandSender commandSender) {
        if (commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) commandSender;
            if (!player.hasPermission("hydroslide.tryjumpunban")) {
                player.sendMessage(HydroSlide.getInstance().getNoPermission());
                return;
            }
        }
        commandSender.sendMessage(HydroSlide.getInstance().getPrefix() + "tryjumpunban <Name>");
    }

    public void onUnban(CommandSender commandSender, @Default() String name) {
        if (commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) commandSender;
            if (!player.hasPermission("hydroslide.tryjumpunban")) {
                player.sendMessage(HydroSlide.getInstance().getNoPermission());
                return;
            }
        }
        if (name.isEmpty()) {
            commandSender.sendMessage("§cDu musst ein Name angeben!");
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
                            commandSender.sendMessage(HydroSlide.getInstance().getPrefix() + "§cEine UUID zu dem Namen wurde nicht gefunden.");
                            return;
                        }
                    }

                    Futures.addCallback(hydroSlide.getTryJumpBanRepository().removeBan(uuid), new FutureCallback<Integer>() {
                        @Override
                        public void onSuccess(Integer updated) {
                            commandSender.sendMessage(HydroSlide.getInstance().getPrefix() + "Der Spieler §e" + name + " §7wurde entbannt in TryJump.");
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

