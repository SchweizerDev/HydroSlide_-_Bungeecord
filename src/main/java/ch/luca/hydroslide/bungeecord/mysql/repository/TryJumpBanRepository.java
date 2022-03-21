package ch.luca.hydroslide.bungeecord.mysql.repository;

import ch.luca.hydroslide.bungeecord.mysql.MySQL;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.UUID;

public class TryJumpBanRepository {

    private final MySQL mySQL;

    public TryJumpBanRepository(MySQL mySQL) {
        this.mySQL = mySQL;

        mySQL.execute("CREATE TABLE IF NOT EXISTS tryjump_bans (uuid VARCHAR(36), end BIGINT, PRIMARY KEY(uuid))");
    }

    public ListenableFuture<Long> getBanTime(UUID uniqueId) {
        return Futures.transform(this.mySQL.query2("SELECT end FROM tryjump_bans WHERE uuid = ?", uniqueId.toString()), rows -> {
            if (rows != null && rows.first() != null) {
                return rows.first().getLong("end");
            }
            return -1L;
        });
    }

    public ListenableFuture<Integer> insertBan(UUID uniqueId, long time) {
        return this.mySQL.execute2("INSERT INTO tryjump_bans (uuid, end) VALUES(?, ?) ON DUPLICATE KEY UPDATE end = VALUES(end)", uniqueId.toString(), time);
    }

    public ListenableFuture<Integer> removeBan(UUID uniqueId) {
        return this.mySQL.execute2("DELETE FROM tryjump_bans WHERE uuid = ?", uniqueId.toString());
    }
}
