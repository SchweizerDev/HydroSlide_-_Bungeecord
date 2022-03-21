package ch.luca.hydroslide.bungeecord.mysql.repository;

import ch.luca.hydroslide.bungeecord.mysql.MySQL;
import ch.luca.hydroslide.bungeecord.mysql.row.Rows;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PlayerInfoRepository {

    private final MySQL mySQL;

    public PlayerInfoRepository(MySQL mySQL) {
        this.mySQL = mySQL;

        mySQL.executeAsync("CREATE TABLE IF NOT EXISTS Playerinfo (uuid VARCHAR(36), name VARCHAR(40), " +
                "ip_address VARCHAR(30), ban_points INT DEFAULT 0, mute_points INT DEFAULT 0, first_join DATETIME DEFAULT NOW(), PRIMARY KEY(uuid))");
    }

    public void updatePlayerInfo(UUID uuid, String name, String ip) {
        this.mySQL.executeAsync("INSERT INTO Playerinfo (uuid, name, ip_address) VALUES (?,?,?) ON DUPLICATE KEY " +
                "UPDATE name = VALUES(name), ip_address = VALUES(ip_address)", uuid.toString(), name, ip);
    }

    public void getUUID(String name, Consumer<UUID> uuid) {
        this.mySQL.queryAsync("SELECT uuid FROM Playerinfo WHERE BINARY name = ?", rows -> {
            if (rows != null && rows.first() != null) {
                uuid.accept(UUID.fromString(rows.first().getString("uuid")));
                return;
            }
            uuid.accept(null);
        }, name);
    }

    public void getName(UUID uuid, Consumer<String> name) {
        this.mySQL.queryAsync("SELECT name FROM Playerinfo WHERE uuid = ?", rows -> {
            if (rows != null && rows.first() != null) {
                name.accept(rows.first().getString("name"));
                return;
            }
            name.accept(null);
        }, uuid.toString());
    }

    public void getIP(UUID uuid, Consumer<String> ip) {
        this.mySQL.queryAsync("SELECT ip_address FROM Playerinfo WHERE uuid = ?", rows -> {
            if (rows != null && rows.first() != null) {
                ip.accept(rows.first().getString("ip_address"));
                return;
            }
            ip.accept(null);
        }, uuid.toString());
    }

    public void getFirstJoin(UUID uuid, Consumer<LocalDateTime> date) {
        this.mySQL.queryAsync("SELECT first_join FROM Playerinfo WHERE uuid = ?", rows -> {
            if (rows != null && rows.first() != null) {
                date.accept(LocalDateTime.parse(String.valueOf(rows.first().get("first_join", Object.class)), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                return;
            }
            date.accept(null);
        }, uuid.toString());
    }

    public void getNamesWithIP(String ip, Consumer<List<String>> names) {
        this.mySQL.queryAsync("SELECT name FROM Playerinfo WHERE ip_address = ?", rows -> {
            if (rows != null) {
                names.accept(rows.all().stream().filter(Objects::nonNull).map(r -> r.getString("name")).collect(Collectors.toList()));
                return;
            }
            names.accept(Collections.emptyList());
        }, ip);
    }

    public void getBanPoints(UUID uuid, Consumer<Integer> banPoints) {
        this.mySQL.queryAsync("SELECT ban_points FROM Playerinfo WHERE uuid = ?", rows -> {
            if (rows != null && rows.first() != null) {
                banPoints.accept(rows.first().getInt("ban_points"));
                return;
            }
            banPoints.accept(0);
        }, uuid.toString());
    }

    public void getMutePoints(UUID uuid, Consumer<Integer> mutePoints) {
        this.mySQL.queryAsync("SELECT mute_points FROM Playerinfo WHERE uuid = ?", rows -> {
            if (rows != null && rows.first() != null) {
                mutePoints.accept(rows.first().getInt("mute_points"));
                return;
            }
            mutePoints.accept(0);
        }, uuid.toString());
    }

    public long getLastOnline(UUID uuid) {
        Rows rows = this.mySQL.query("SELECT LastOnline FROM Friends WHERE UUID = ?", uuid.toString());
        if (rows != null && rows.first() != null) {
            return rows.first().getLong("LastOnline");
        }
        return -1;
    }

    public void addBanPoints(UUID uuid, int amount) {
        this.mySQL.executeAsync("UPDATE Playerinfo SET ban_points = ban_points + ? WHERE uuid = ?", amount, uuid.toString());
    }

    public void addMutePoints(UUID uuid, int amount) {
        this.mySQL.executeAsync("UPDATE Playerinfo SET mute_points = mute_points + ? WHERE uuid = ?", amount, uuid.toString());
    }

    public void resetBanPoints(UUID uuid) {
        this.mySQL.executeAsync("UPDATE Playerinfo SET ban_points = 0 WHERE uuid = ?", uuid.toString());
    }

    public void resetMutePoints(UUID uuid) {
        this.mySQL.executeAsync("UPDATE Playerinfo SET ban_points = 0 WHERE uuid = ?", uuid.toString());
    }

    public void removeBanPoints(UUID uuid, int amount) {
        this.mySQL.executeAsync("UPDATE Playerinfo SET ban_points = GREATEST(0, ban_points - ?) WHERE uuid = ?", amount, uuid.toString());
    }

    public void removeMutePoints(UUID uuid, int amount) {
        this.mySQL.executeAsync("UPDATE Playerinfo SET mute_points = GREATEST(0, mute_points - ?) WHERE uuid = ?", amount, uuid.toString());
    }
}
