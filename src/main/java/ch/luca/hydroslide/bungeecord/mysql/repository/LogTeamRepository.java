package ch.luca.hydroslide.bungeecord.mysql.repository;

import ch.luca.hydroslide.bungeecord.mysql.MySQL;
import ch.luca.hydroslide.bungeecord.mysql.row.Rows;

import java.util.UUID;
import java.util.function.Consumer;

public class LogTeamRepository {

    private final MySQL mySQL;

    public LogTeamRepository(MySQL mySQL) {
        this.mySQL = mySQL;
        mySQL.executeAsync("CREATE TABLE IF NOT EXISTS Logteam (name VARCHAR(40), uuid VARCHAR(36), activated INT DEFAULT 0, PRIMARY KEY(uuid))");
    }

    public void updateLogTeam(UUID uuid, String name) {
        this.mySQL.executeAsync("INSERT INTO Logteam (name, uuid) VALUES (?,?) ON DUPLICATE KEY UPDATE name = VALUES(name)", name, uuid.toString());
    }

    public void getIsActivated(UUID uuid, Consumer<Integer> activated) {
        this.mySQL.queryAsync("SELECT activated FROM Logteam WHERE uuid = ?", rows -> {
            if (rows != null && rows.first() != null) {
                activated.accept(rows.first().getInt("activated"));
                return;
            }
            activated.accept(0);
        }, uuid.toString());
    }

    public int getIsActivated( UUID uniqueId ) {
        Rows rows = this.mySQL.query( "SELECT activated FROM Logteam WHERE uuid = ?", uniqueId.toString() );
        if ( rows != null && rows.first() != null ) {
            return rows.first().getInt( "activated" );
        }
        return -1;
    }

    public void setActive(UUID uuid) {
        this.mySQL.executeAsync("UPDATE Logteam SET activated = activated + 1 WHERE uuid = ?", uuid.toString());
    }

    public void setInactive(UUID uuid) {
        this.mySQL.executeAsync("UPDATE Logteam SET activated = activated - 1 WHERE uuid = ?", uuid.toString());
    }
}
