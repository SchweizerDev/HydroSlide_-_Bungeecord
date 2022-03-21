package ch.luca.hydroslide.bungeecord.config;

import lombok.Getter;
import net.cubespace.Yamler.Config.Path;
import net.cubespace.Yamler.Config.YamlConfig;

import java.io.File;

@Getter
public class MySQLConfig extends YamlConfig {

    @Path("profile.host")
    private String profileHost = "localhost";

    @Path("profile.database")
    private String profileDatabase = "database";

    @Path("profile.user")
    private String profileUser = "user";

    @Path("profile.password")
    private String profilePassword = "pm";

    @Path("profile.port")
    private int profilePort = 3306;

    @Path("tryjump.host")
    private String tryJumpHost = "localhost";

    @Path("tryjump.database")
    private String tryJumpDatabase = "database";

    @Path("tryjump.user")
    private String tryJumpUser = "user";

    @Path("tryjump.password")
    private String tryJumpPassword = "pm";

    @Path("tryjump.port")
    private int tryJumpPort = 3306;

    @Path("lobby-cases.host")
    private String lobbyHost = "localhost";

    @Path("lobby-cases.database")
    private String lobbyDatabase = "database";

    @Path("lobby-cases.user")
    private String lobbyUser = "user";

    @Path("lobby-cases.password")
    private String lobbyPassword = "pm";

    @Path("lobby-cases.port")
    private int lobbyPort = 3306;

    public MySQLConfig(File file) {
        this.CONFIG_FILE = file;
    }
}