package ch.luca.hydroslide.bungeecord;

import ch.luca.hydroslide.bungeecord.commands.*;
import ch.luca.hydroslide.bungeecord.config.MaintenanceConfig;
import ch.luca.hydroslide.bungeecord.config.MotdConfig;
import ch.luca.hydroslide.bungeecord.config.MySQLConfig;
import ch.luca.hydroslide.bungeecord.config.SlotConfig;
import ch.luca.hydroslide.bungeecord.listener.*;
import ch.luca.hydroslide.bungeecord.manager.AutoMessageManager;
import ch.luca.hydroslide.bungeecord.mysql.MySQL;
import ch.luca.hydroslide.bungeecord.mysql.MySQLPunish;
import ch.luca.hydroslide.bungeecord.mysql.repository.*;
import co.aikar.commands.BungeeCommandManager;
import de.crafter75.perms.global.mojang.NameToUUIDResolver;
import lombok.Getter;
import net.cubespace.Yamler.Config.InvalidConfigurationException;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

@Getter
public class HydroSlide extends Plugin {

    @Getter
    private static HydroSlide instance;
    private static AutoMessageManager autoMessageManager;
    private static MySQL mySQL;
    private NameToUUIDResolver nameToUUIDResolver;
    private ProfileRepository profileRepository;

    public static ArrayList<ProxiedPlayer> inSupport = new ArrayList<>();
    public static ArrayList<ProxiedPlayer> needHelp = new ArrayList<>();
    public static ArrayList<ProxiedPlayer> Supporting = new ArrayList<>();
    public static HashMap<ProxiedPlayer, ProxiedPlayer> chat = new HashMap();
    public static HashMap<String, String> reports = new HashMap<>();
    public static HashMap<String, String> reportreason = new HashMap<>();
    public static HashMap<String, String> reportchatlog = new HashMap<>();

    PluginManager pluginManager = ProxyServer.getInstance().getPluginManager();

    @Getter
    private static BanMuteHistoryRepository banMuteHistoryRepository;
    @Getter
    private static PlayerInfoRepository playerInfoRepository;
    @Getter
    private static CoinsRepository coinsRepository;
    @Getter
    private static LogTeamRepository logTeamRepository;
    @Getter
    private static TeamStatsRepository teamStatsRepository;
    private TryJumpBanRepository tryJumpBanRepository;
    private LobbyCaseRepository lobbyCaseRepository;
    private PrizeWheelSpinRepository prizeWheelSpinRepository;

    private final String prefix = "§bHydroSlide §8» §7";
    private final String noPermission = "§bHydroSlide §8» §cFür diesen Befehl fehlt dir die Berechtigung.";
    private final String noPlayer = "§bHydroSlide §8» §cBenutze diesen Befehl bitte im Spiel.";
    private final String playerNotOnline = "§bHydroSlide §8» §cDieser Spieler ist nicht online.";
    private final String playerNeverOnline = "§bHydroSlide §8» §cDieser Spieler wurde in der Datenbank nicht gefunden.";
    private final String prefixUse = "§bHydroSlide §8» §cBenutze: §e/";
    private final String autoMessage = "§cHydroSlide §8» ";

    @Override
    public void onEnable() {
        instance = this;
        if(!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        mySQL = new MySQL("localhost", "bungeecord", "root", "password", 3306);
        banMuteHistoryRepository = new BanMuteHistoryRepository(mySQL);
        playerInfoRepository = new PlayerInfoRepository(mySQL);
        coinsRepository = new CoinsRepository(mySQL);
        logTeamRepository = new LogTeamRepository(mySQL);
        teamStatsRepository = new TeamStatsRepository(mySQL);
        autoMessageManager.startAutoMessages();
        SlotConfig.loadSlots();
        MotdConfig.loadMotd();
        MaintenanceConfig.loadMaintenance();
        MySQLPunish.loadFile();
        MySQLPunish.connect();
        MySQLPunish.createTables();
        MySQLConfig config;
        try {
            if (!this.getDataFolder().exists()) {
                this.getDataFolder().mkdir();
            }
            config = new MySQLConfig(new File(this.getDataFolder(), "extraCommandsMySQL.yml"));
            config.init();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
            return;
        }
        this.profileRepository = new ProfileRepository(new MySQL(config.getProfileHost(), config.getProfileDatabase(), config.getProfileUser(), config.getProfilePassword(), config.getProfilePort()));
        this.nameToUUIDResolver = new NameToUUIDResolver();

        this.tryJumpBanRepository = new TryJumpBanRepository(new MySQL(config.getTryJumpHost(), config.getTryJumpDatabase(), config.getTryJumpUser(), config.getTryJumpPassword(), config.getTryJumpPort()));
        MySQL lobbyMySQL = new MySQL(config.getLobbyHost(), config.getLobbyDatabase(), config.getLobbyUser(),
                config.getLobbyPassword(), config.getLobbyPort());
        this.lobbyCaseRepository = new LobbyCaseRepository(lobbyMySQL);
        this.prizeWheelSpinRepository = new PrizeWheelSpinRepository(lobbyMySQL);

        pluginManager.registerCommand(HydroSlide.getInstance(), new AccountInfo("accountinfo"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new AccountInfo("acinfo"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new AdminCoins("admincoins"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new AdminInfo("admininfo"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new Ban("ban"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new BanList("banlist"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new BauServer("bauserver"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new BauServer("bs"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new Broadcast("bc"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new Broadcast("broadcast"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new ChatClear("cc"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new ChatClear("chatclear"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new ChatMute("chatmute"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new Coins("coins"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new Coins("money"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new Coins("geld"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new Discord("dc"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new Discord("discord"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new GlobalChatClear("gcc"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new GlobalChatClear("globalchatclear"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new Help("help"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new Help("hilfe"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new History("history"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new Hub("hub"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new Hub("l"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new Hub("lobby"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new JoinMe("joinme"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new JumpTo("jumpto"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new Kick("kick"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new List("list"));
        BungeeCommandManager.getCurrentCommandManager().registerCommand(new LobbyCases(this));
        pluginManager.registerCommand(HydroSlide.getInstance(), new LogTeam("logteam"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new Lolroflcoperxxdq("lolroflcoperxxdq"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new Maintenance("maintenance"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new Motd("motd"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new Mute("mute"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new MuteList("mutelist"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new NetworkStop("networkstop"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new OnlineTime("onlinetime"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new OnlineTime("playtime"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new OnlineTime("spielzeit"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new Party("party"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new PartyChat("partychat"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new PartyChat("pc"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new Ping("ping"));
        BungeeCommandManager.getCurrentCommandManager().registerCommand(new PrizeWheelSpins(this));
        pluginManager.registerCommand(HydroSlide.getInstance(), new Report("report"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new ReportEdit("reportedit"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new Reports("reports"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new Reset("reset"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new ServerInfo("serverinfo"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new Slots("slots"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new Support("support"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new TeamChat("tc"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new TeamChat("teamchat"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new TeamStats("teamstats"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new TestServer("testserver"));
        BungeeCommandManager.getCurrentCommandManager().registerCommand(new TryJumpUnban(this));
        pluginManager.registerCommand(HydroSlide.getInstance(), new Unban("unban"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new Unmute("unmute"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new Userinfo("userinfo"));
        pluginManager.registerCommand(HydroSlide.getInstance(), new Userinfo("ui"));

        pluginManager.registerListener(HydroSlide.getInstance(), new ChatListener());
        pluginManager.registerListener(HydroSlide.getInstance(), new LoginListener());
        pluginManager.registerListener(HydroSlide.getInstance(), new PlayerDisconnectListener());
        pluginManager.registerListener(HydroSlide.getInstance(), new PostLoginListener());
        pluginManager.registerListener(HydroSlide.getInstance(), new ProxyPingListener());
        pluginManager.registerListener(HydroSlide.getInstance(), new ServerConnectedListener());
        pluginManager.registerListener(HydroSlide.getInstance(), new ServerConnectListener());
        pluginManager.registerListener(HydroSlide.getInstance(), new ServerDisconnectListener());
        pluginManager.registerListener(HydroSlide.getInstance(), new ServerKickListener());
        pluginManager.registerListener(HydroSlide.getInstance(), new ServerSwitchListener());
        pluginManager.registerListener(HydroSlide.getInstance(), new TabCompleteListener());

    }

    @Override
    public void onDisable() {
        mySQL.disconnect();
        MySQLPunish.close();
    }
}
