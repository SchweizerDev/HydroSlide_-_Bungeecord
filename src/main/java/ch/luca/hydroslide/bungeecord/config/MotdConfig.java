package ch.luca.hydroslide.bungeecord.config;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class MotdConfig {

    private static String motdNormal1;
    private static String motdNormal2;
    private static String motdMaintenance1;
    private static String motdMaintenance2;

    public static void loadMotd() {
        try {
            File file = getFile();
            if(!file.exists()) {
                file.createNewFile();
                Configuration configuration = getConfig();
                configuration.set("motdNormal1", "");
                configuration.set("motdNormal2", "");
                configuration.set("motdMaintenance1", "");
                configuration.set("motdMaintenance2", "");
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, file);
            }
            motdNormal1 = getConfig().getString("motdNormal1");
            motdNormal2 = getConfig().getString("motdNormal2");
            motdMaintenance1 = getConfig().getString("motdMaintenance1");
            motdMaintenance2 = getConfig().getString("motdMaintenance2");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File getFile() {
        return new File(HydroSlide.getInstance().getDataFolder(), "motd.yml");
    }

    public static Configuration getConfig() {
        try {
            return ConfigurationProvider.getProvider(YamlConfiguration.class).load(getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setMotdNormal1(String normalMotd1) {
        try {
            File file = getFile();
            Configuration configuration = getConfig();
            configuration.set("motdNormal1", normalMotd1);
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, file);
        } catch(IOException e) {
            e.printStackTrace();
        }
        motdNormal1 = normalMotd1;
    }

    public static void setMotdNormal2(String normalMotd2) {
        try {
            File file = getFile();
            Configuration configuration = getConfig();
            configuration.set("motdNormal2", normalMotd2);
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, file);
        } catch(IOException e) {
            e.printStackTrace();
        }
        motdNormal2 = normalMotd2;
    }

    public static void setMotdMaintenance1(String maintenanceMotd1) {
        try {
            File file = getFile();
            Configuration configuration = getConfig();
            configuration.set("motdMaintenance1", maintenanceMotd1);
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, file);
        } catch(IOException e) {
            e.printStackTrace();
        }
        motdMaintenance1 = maintenanceMotd1;
    }

    public static void setMotdMaintenance2(String maintenanceMotd2) {
        try {
            File file = getFile();
            Configuration configuration = getConfig();
            configuration.set("motdMaintenance2", maintenanceMotd2);
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, file);
        } catch(IOException e) {
            e.printStackTrace();
        }
        motdMaintenance2 = maintenanceMotd2;
    }


    public static String getMotdNormal1() {
        return motdNormal1;
    }
    public static String getMotdNormal2() {
        return motdNormal2;
    }
    public static String getMotdMaintenance1() {
        return motdMaintenance1;
    }
    public static String getMotdMaintenance2() {
        return motdMaintenance2;
    }

}
