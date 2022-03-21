package ch.luca.hydroslide.bungeecord.mysql.repository;

import ch.luca.hydroslide.bungeecord.HydroSlide;
import ch.luca.hydroslide.bungeecord.mysql.MySQLPunish;
import net.md_5.bungee.api.ProxyServer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MuteRepository {

    public static void mute(String uuid, String name, String reason, long seconds, String muter) {
        long end = 0;
        if (seconds == -1) {
            end = -1;
        } else {
            long current = System.currentTimeMillis();
            long millis = seconds * 1000;
            end = current + millis;
        }
        MySQLPunish.update("INSERT INTO Mutedplayers (name, uuid, end, reason, muter) VALUES ('" + name + "','" + uuid + "','" + end + "','" + reason + "','" + muter + "')");
        if (ProxyServer.getInstance().getPlayer(name) != null) {
            ProxyServer.getInstance().getPlayer(name).sendMessage(HydroSlide.getInstance().getPrefix() + "§cDu wurdest aus dem Chat ausgeschlossen.");
            ProxyServer.getInstance().getPlayer(name).sendMessage(HydroSlide.getInstance().getPrefix() + "Grund: §6" + getReason(uuid));
        }
    }

    public static void unmute(String uuid) {
        MySQLPunish.update("DELETE FROM Mutedplayers WHERE uuid='" + uuid + "'");
    }

    public static boolean getIsMuted(String uuid) {
        ResultSet rs = MySQLPunish.getResult("SELECT end FROM Mutedplayers WHERE uuid='" + uuid + "'");
        try {
            return rs.next();
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                rs.close();
            } catch ( SQLException e ) {
                e.printStackTrace();
            }
        }
    }

    public static String getReason(String uuid) {
        ResultSet rs = MySQLPunish.getResult("SELECT * FROM Mutedplayers WHERE uuid='" + uuid + "'");
        try {
            if (rs.next()) {
                return rs.getString("reason");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch ( SQLException e ) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String getReasonWithName(String PlayerName) {
        ResultSet rs = MySQLPunish.getResult("SELECT * FROM Mutedplayers WHERE name='" + PlayerName + "'");
        try {
            if (rs.next()) {
                return rs.getString("reason");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch ( SQLException e ) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String getMuter(String uuid) {
        ResultSet rs = MySQLPunish.getResult("SELECT * FROM Mutedplayers WHERE uuid='" + uuid + "'");
        try {
            if (rs.next()) {
                return rs.getString("muter");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch ( SQLException e ) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static Long getEnd(String uuid) {
        ResultSet rs = MySQLPunish.getResult("SELECT * FROM Mutedplayers WHERE uuid='" + uuid + "'");
        try {
            if (rs.next()) {
                return rs.getLong("end");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch ( SQLException e ) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static List<String> getMutedPlayersFromPlayer(String name) {
        ArrayList<String> list = new ArrayList<String>();
        ResultSet rs = MySQLPunish.getResult("SELECT * FROM Mutedplayers WHERE muter='" + name + "'");
        try {
            while (rs.next()) {
                list.add(rs.getString("muter"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch ( SQLException e ) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public static List<String> getMutedPlayers() {
        ArrayList<String> list = new ArrayList<String>();
        ResultSet rs = MySQLPunish.getResult("SELECT * FROM Mutedplayers");
        try {
            while (rs.next()) {
                list.add(rs.getString("name"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch ( SQLException e ) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public static List<String> getMutedPlayersNamesWithSameIP(String ip) {
        ArrayList<String> list = new ArrayList<String>();
        ResultSet rs = MySQLPunish.getResult("SELECT Playerinfo.name AS name FROM Playerinfo INNER JOIN Mutedplayers USING(uuid) WHERE Playerinfo.ip_address = '" + ip + "'");
        try {
            while (rs.next()) {
                list.add(rs.getString("name"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch ( SQLException e ) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public static String getReamainingTime(String uuid) {
        long current = System.currentTimeMillis();
        long end = MuteRepository.getEnd(uuid);
        if (end == -1) {
            return "§c§lPERMANENT";
        }
        long millis = end - current;
        long seconds = 0;
        long minutes = 0;
        long hours = 0;
        long days = 0;
        long weeks = 0;
        while (millis > 1000) {
            millis -= 1000;
            ++seconds;
        }
        while (seconds > 60) {
            seconds -= 60;
            ++minutes;
        }
        while (minutes > 60) {
            minutes -= 60;
            ++hours;
        }
        while (hours > 24) {
            hours -= 24;
            ++days;
        }
        while (days > 7) {
            days -= 7;
            ++weeks;
        }
        return "§e" + weeks + " §7Woche(n) §e" + days + " §7Tag(e) §e" + hours + " §7Stunde(n) §e" + minutes + " §7Minute(n) §e" + seconds + " §7Sekunde(n) ";
    }
}
