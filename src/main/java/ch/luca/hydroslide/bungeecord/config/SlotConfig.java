package ch.luca.hydroslide.bungeecord.config;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class SlotConfig {

    private static Integer slots;

    public static void loadSlots() {
        try {
            File file = getFile();
            if (!file.exists()) {
                file.createNewFile();
                Configuration cfg = getConfig();
                cfg.set("Slots", 150);
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(cfg, file);
            }
            slots = getConfig().getInt("Slots");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File getFile() {
        return new File(HydroSlide.getInstance().getDataFolder(),"slots.yml");
    }

    public static Configuration getConfig() {
        try {
            return ConfigurationProvider.getProvider(YamlConfiguration.class).load(getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setSlots(int amount) {
        try {
            File file = getFile();
            Configuration cfg = getConfig();
            cfg.set("Slots", amount);
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(cfg, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        slots = amount;
    }

    public static int getSlots() {
        return slots;
    }
}
