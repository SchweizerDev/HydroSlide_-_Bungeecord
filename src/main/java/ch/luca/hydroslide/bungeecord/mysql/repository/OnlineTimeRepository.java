package ch.luca.hydroslide.bungeecord.mysql.repository;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import ch.luca.hydroslide.bungeecord.mysql.MySQLPunish;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OnlineTimeRepository {

    private static Map<UUID, Long> joinTime = new HashMap<>();

    public static void onJoin(ProxiedPlayer player) {
        joinTime.put(player.getUniqueId(), System.currentTimeMillis());
    }

    public static void onQuit(ProxiedPlayer player) {
        if (!joinTime.containsKey(player.getUniqueId())) {
            return;
        }
        long playTimeToAdd = System.currentTimeMillis() - joinTime.remove(player.getUniqueId());
        ProxyServer.getInstance().getScheduler().runAsync(HydroSlide.getInstance(), () -> {
            MySQLPunish.update("INSERT INTO Onlinetime (uuid, time) VALUES ('" + player.getUniqueId().toString() + "'," + playTimeToAdd + ") ON DUPLICATE KEY UPDATE time=time+VALUES(time)");
        });
    }

    public static long getPlayTime(UUID uniqueId) {
        ResultSet resultSet = MySQLPunish.getResult("SELECT time FROM Onlinetime WHERE uuid = '" + uniqueId.toString() + "'");
        long playTime = 0;
        try {
            if (resultSet != null && resultSet.next()) {
                playTime = resultSet.getLong("time");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (joinTime.containsKey(uniqueId)) {
            long playTimeToAdd = System.currentTimeMillis() - joinTime.get(uniqueId);
            playTime += playTimeToAdd;
        }
        return playTime;
    }

    public static String getFormattedPlayTime(UUID uniqueId) {
        return getFormattedPlayTime(getPlayTime(uniqueId));
    }

    private static String getFormattedPlayTime(long playTime) {
        StringBuilder stringBuilder = new StringBuilder();
        int sec = (int) (playTime / 1000);
        int days = sec / 86400;
        int hours = (sec % 86400) / 3600;
        int minutes = (sec % 3600) / 60;
        int seconds = (sec % 60);
        if (days != 0) {
            if (days < 10) {
                if (days == 1) {
                    stringBuilder.append("0").append(days).append(" §7Tag, §e");
                } else {
                    stringBuilder.append("0").append(days).append(" §7Tage, §e");
                }
            } else {
                stringBuilder.append(days).append(" §7Tage, §e");
            }
        }
        if (hours != 0) {
            if (hours < 10) {
                if (hours == 1) {
                    stringBuilder.append("0").append(hours).append(" §7Stunde, §e");
                } else {
                    stringBuilder.append("0").append(hours).append(" §7Stunden, §e");
                }
            } else {
                stringBuilder.append(hours).append(" §7Stunden, §e");
            }
        }
        if (minutes != 0) {
            if (minutes < 10) {
                if (minutes == 1) {
                    stringBuilder.append("0").append(minutes).append(" §7Minute, §e");
                } else {
                    stringBuilder.append("0").append(minutes).append(" §7Minuten, §e");
                }
            } else {
                stringBuilder.append(minutes).append(" §7Minuten, §e");
            }
        }
        if (seconds != 0) {
            if (seconds < 10) {
                if (seconds == 1) {
                    stringBuilder.append("0").append(seconds).append(" §7Sekunde");
                } else {
                    stringBuilder.append("0").append(seconds).append(" §7Sekunden");
                }
            } else {
                stringBuilder.append(seconds).append(" §7Sekunden");
            }
        }
        return stringBuilder.toString();
    }
}