package ch.luca.hydroslide.bungeecord.mysql.repository;

import ch.luca.hydroslide.bungeecord.mysql.MySQL;
import ch.luca.hydroslide.bungeecord.mysql.row.Row;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class BanMuteHistoryRepository {

    private final MySQL mySQL;

    public BanMuteHistoryRepository(MySQL mySQL) {
        this.mySQL = mySQL;

        mySQL.executeAsync("CREATE TABLE IF NOT EXISTS History (id INT AUTO_INCREMENT, type VARCHAR(30), " +
                "uuid VARCHAR(36), name VARCHAR(40), reason VARCHAR(100), punisher_name VARCHAR(40), date DATETIME DEFAULT NOW(), PRIMARY KEY(id))");
    }

    public void addHistory(HistoryType type, UUID uuid, String name, String reason, String punisherName) {
        this.mySQL.executeAsync("INSERT INTO History (type, uuid, name, reason, punisher_name) VALUES(?,?,?,?,?)",
                type.name().toUpperCase(), uuid.toString(), name, reason, punisherName);
    }

    public void getHistory(HistoryType type, UUID uuid, Consumer<List<HistoryEntry>> history) {
        this.mySQL.queryAsync("SELECT * FROM History WHERE type = ? AND uuid = ?", rows -> {
            List<HistoryEntry> entries = new ArrayList<>();
            if (rows != null) {
                for (Row row : rows.all()) {
                    entries.add(HistoryEntry.fromRow(row));
                }
            }
            history.accept(entries);
        }, type.name().toUpperCase(), uuid.toString());
    }

    @Getter
    @AllArgsConstructor
    public static class HistoryEntry {

        private final HistoryType type;

        private final UUID uuid;

        private final String name;

        private final String reason;

        private final String punisherName;

        private final LocalDateTime date;

        public static HistoryEntry fromRow(Row row) {
            return new HistoryEntry(HistoryType.valueOf(row.getString("type")), UUID.fromString(row.getString("uuid")),
                    row.getString("name"), row.getString("reason"), row.getString("punisher_name"), LocalDateTime.parse(String.valueOf(row.get("date", Object.class)), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
    }

    public enum HistoryType {
        BAN,
        MUTE;
    }
}
