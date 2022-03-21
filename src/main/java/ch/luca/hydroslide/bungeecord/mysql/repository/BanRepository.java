package ch.luca.hydroslide.bungeecord.mysql.repository;

import ch.luca.hydroslide.bungeecord.mysql.MySQLPunish;
import net.md_5.bungee.api.ProxyServer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BanRepository {

    public static void ban(String uuid, String name, String reason, long seconds, String banner) {
        long end = 0;
        if (seconds == -1) {
            end = -1;
        } else {
            long current = System.currentTimeMillis();
            long millis = seconds * 1000;
            end = current + millis;
        }
        MySQLPunish.update("INSERT INTO Bannedplayers (name, uuid, end, reason, banner) VALUES ('" + name + "','" + uuid + "','" + end + "','" + reason + "','" + banner + "')");
        if (ProxyServer.getInstance().getPlayer(name) != null) {
            ProxyServer.getInstance().getPlayer(name).disconnect( "§cDu wurdest von §bHydroSlide.eu §cgebannt!\n\n§7Grund: §6" + getReason(uuid) +
                    " \n\n§7Du kannst einen Entbannungsantrag auf §ewww.hydroslide.eu §7schreiben.");
        }
    }

    public static void unban(String uuid) {
        MySQLPunish.update("DELETE FROM Bannedplayers WHERE uuid='" + uuid + "'");
    }

    public static boolean getIsBanned(String uuid) {
        ResultSet rs = MySQLPunish.getResult("SELECT end FROM Bannedplayers WHERE uuid='" + uuid + "'");
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
        ResultSet rs = MySQLPunish.getResult("SELECT * FROM Bannedplayers WHERE uuid='" + uuid + "'");
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
        ResultSet rs = MySQLPunish.getResult("SELECT * FROM Bannedplayers WHERE name='" + PlayerName + "'");
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

    public static String getBanner(String uuid) {
        ResultSet rs = MySQLPunish.getResult("SELECT * FROM Bannedplayers WHERE uuid='" + uuid + "'");
        try {
            if (rs.next()) {
                return rs.getString("banner");
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
        ResultSet rs = MySQLPunish.getResult("SELECT * FROM Bannedplayers WHERE uuid='" + uuid + "'");
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

    public static List<String> getBannedPlayers() {
        ArrayList<String> list = new ArrayList<String>();
        ResultSet rs = MySQLPunish.getResult("SELECT * FROM Bannedplayers");
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

    public static List<String> getBannedPlayersNamesWithSameIP(String ip) {
        ArrayList<String> list = new ArrayList<String>();
        ResultSet rs = MySQLPunish.getResult("SELECT Playerinfo.name AS name FROM Playerinfo INNER JOIN Bannedplayers USING(uuid) WHERE Playerinfo.ip_address = '" + ip + "'");
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
        long end = BanRepository.getEnd(uuid);
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
