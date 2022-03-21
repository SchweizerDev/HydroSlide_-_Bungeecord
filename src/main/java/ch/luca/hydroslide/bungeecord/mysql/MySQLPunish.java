package ch.luca.hydroslide.bungeecord.mysql;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.sql.*;

public class MySQLPunish {

    public static String username;
    public static String password;
    public static String database;
    public static String host;
    public static String port;
    public static Connection con;

    public static void connect() {
        if (!MySQLPunish.isConnected()) {
            try {
                con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database+"?autoReconnect=true", username, password);
                ProxyServer.getInstance().getConsole().sendMessage("§cMySQL: Successfully connected to database " + database + "!");
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close() {
        if (MySQLPunish.isConnected()) {
            try {
                con.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isConnected() {
        if (con != null) {
            return true;
        }
        return false;
    }

    public static void update(String query) {
        PreparedStatement ps = null;
        try {
            ps = MySQLPunish.con.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static ResultSet getResult(String query) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = MySQLPunish.con.prepareStatement(query);
            rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void createTables() {
        if (MySQLPunish.isConnected()) {
            try {
                con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS Bannedplayers(name varchar(100), uuid varchar(100), end varchar(100), reason varchar(100), banner varchar(100), PRIMARY KEY(uuid))");
                con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS Mutedplayers(name varchar(100), uuid varchar(100), end varchar(100), reason varchar(100), muter varchar(100), PRIMARY KEY(uuid))");
                con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS Onlinetime(uuid varchar(64), time BIGINT, PRIMARY KEY(uuid))");
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void loadFile() {
        try {
            File file = new File(HydroSlide.getInstance().getDataFolder(), "mysql.yml");
            boolean created = true;
            if (!file.exists()) {
                file.createNewFile();
                created = false;
            }
            Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            if (!created) {
                config.set("mysql.Host", "localhost");
                config.set("mysql.Port", "3306");
                config.set("mysql.Database", "bungeecord");
                config.set("mysql.Username", "root");
                config.set("mysql.Password", "wqfj9X3v9cPMUrfS");
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
            }
            host = config.getString("mysql.Host");
            port = config.getString("mysql.Port");
            database = config.getString("mysql.Database");
            username = config.getString("mysql.Username");
            password = config.getString("mysql.Password");
        }
        catch (Exception localException) {}
    }
}