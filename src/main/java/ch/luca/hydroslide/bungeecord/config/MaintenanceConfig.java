package ch.luca.hydroslide.bungeecord.config;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class MaintenanceConfig {

    private static boolean maintenance;

    public static void loadMaintenance() {
        try {
            File file = getFile();
            if (!file.exists()) {
                file.createNewFile();
                Configuration cfg = getConfig();
                cfg.set("maintenance", true);
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(cfg, file);
            }
            maintenance = getConfig().getBoolean("maintenance");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File getFile() {
        return new File(HydroSlide.getInstance().getDataFolder(),"maintenance.yml");
    }

    public static Configuration getConfig() {
        try {
            return ConfigurationProvider.getProvider(YamlConfiguration.class).load(getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setMaintenance(boolean bool) {
        try {
            File file = getFile();
            Configuration cfg = getConfig();
            cfg.set("maintenance", bool);
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(cfg, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        maintenance = bool;
    }

    public static boolean getMaintenance() {
        return maintenance;
    }
}
