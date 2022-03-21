package ch.luca.hydroslide.bungeecord.mysql.repository;

import ch.luca.hydroslide.bungeecord.mysql.MySQL;

import java.util.UUID;
import java.util.function.Consumer;

public class TeamStatsRepository {

    private final MySQL mySQL;

    public TeamStatsRepository(MySQL mySQL) {
        this.mySQL = mySQL;
        mySQL.executeAsync("CREATE TABLE IF NOT EXISTS Teamstats (name VARCHAR(40), uuid VARCHAR(36), reports INT DEFAULT 0, mutes INT DEFAULT 0, bans INT DEFAULT 0, kicks INT DEFAULT 0, supports INT DEFAULT 0, PRIMARY KEY(uuid))");
    }

    public void updateTeamStats(UUID uuid, String name) {
        this.mySQL.executeAsync("INSERT INTO Teamstats (name, uuid) VALUES (?,?) ON DUPLICATE KEY " +
                "UPDATE name = VALUES(name)", name, uuid.toString());
    }

    public void getReports(UUID uuid, Consumer<Integer> reports) {
        this.mySQL.queryAsync("SELECT reports FROM Teamstats WHERE uuid = ?", rows -> {
            if (rows != null && rows.first() != null) {
                reports.accept(rows.first().getInt("reports"));
                return;
            }
            reports.accept(null);
        }, uuid.toString());
    }

    public void getMutes(UUID uuid, Consumer<Integer> mutes) {
        this.mySQL.queryAsync("SELECT mutes FROM Teamstats WHERE uuid = ?", rows -> {
            if (rows != null && rows.first() != null) {
                mutes.accept(rows.first().getInt("mutes"));
                return;
            }
            mutes.accept(null);
        }, uuid.toString());
    }

    public void getBans(UUID uuid, Consumer<Integer> bans) {
        this.mySQL.queryAsync("SELECT bans FROM Teamstats WHERE uuid = ?", rows -> {
            if (rows != null && rows.first() != null) {
                bans.accept(rows.first().getInt("bans"));
                return;
            }
            bans.accept(null);
        }, uuid.toString());
    }

    public void getKicks(UUID uuid, Consumer<Integer> kicks) {
        this.mySQL.queryAsync("SELECT kicks FROM Teamstats WHERE uuid = ?", rows -> {
            if (rows != null && rows.first() != null) {
                kicks.accept(rows.first().getInt("kicks"));
                return;
            }
            kicks.accept(null);
        }, uuid.toString());
    }

    public void getSupports(UUID uuid, Consumer<Integer> supports) {
        this.mySQL.queryAsync("SELECT supports FROM Teamstats WHERE uuid = ?", rows -> {
            if (rows != null && rows.first() != null) {
                supports.accept(rows.first().getInt("supports"));
                return;
            }
            supports.accept(null);
        }, uuid.toString());
    }



    public void addReports(UUID uuid, int reports) {
        this.mySQL.executeAsync("UPDATE Teamstats SET reports = reports + ? WHERE uuid = ?", reports, uuid.toString());
    }

    public void resetReports(UUID uuid) {
        this.mySQL.executeAsync("UPDATE Teamstats SET reports = 0 WHERE uuid = ?", uuid.toString());
    }

    public void addMutes(UUID uuid, int mutes) {
        this.mySQL.executeAsync("UPDATE Teamstats SET mutes = mutes + ? WHERE uuid = ?", mutes, uuid.toString());
    }

    public void resetMutes(UUID uuid) {
        this.mySQL.executeAsync("UPDATE Teamstats SET mutes = 0 WHERE uuid = ?", uuid.toString());
    }

    public void addBans(UUID uuid, int bans) {
        this.mySQL.executeAsync("UPDATE Teamstats SET bans = bans + ? WHERE uuid = ?", bans, uuid.toString());
    }

    public void resetBans(UUID uuid) {
        this.mySQL.executeAsync("UPDATE Teamstats SET bans = 0 WHERE uuid = ?", uuid.toString());
    }

    public void addKicks(UUID uuid, int kicks) {
        this.mySQL.executeAsync("UPDATE Teamstats SET kicks = kicks + ? WHERE uuid = ?", kicks, uuid.toString());
    }

    public void resetKicks(UUID uuid) {
        this.mySQL.executeAsync("UPDATE Teamstats SET kicks = 0 WHERE uuid = ?", uuid.toString());
    }

    public void addSupports(UUID uuid, int supports) {
        this.mySQL.executeAsync("UPDATE Teamstats SET supports = supports + ? WHERE uuid = ?", supports, uuid.toString());
    }

    public void resetSupports(UUID uuid) {
        this.mySQL.executeAsync("UPDATE Teamstats SET supports = 0 WHERE uuid = ?", uuid.toString());
    }
}

