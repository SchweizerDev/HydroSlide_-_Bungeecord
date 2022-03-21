package ch.luca.hydroslide.bungeecord.mysql.repository;

import ch.luca.hydroslide.bungeecord.mysql.MySQL;
import ch.luca.hydroslide.bungeecord.mysql.row.Rows;
import com.google.common.base.Function;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.UUID;

public class PrizeWheelSpinRepository {

    private final MySQL mySQL;

    public PrizeWheelSpinRepository(MySQL mySQL) {
        this.mySQL = mySQL;

        mySQL.execute2("CREATE TABLE IF NOT EXISTS prizewheel_spins (uuid VARCHAR(36), spins INT DEFAULT 0, PRIMARY KEY(uuid))");
    }

    public ListenableFuture<Integer> getSpins(UUID uniqueId) {
        return Futures.transform(this.mySQL.query2("SELECT spins FROM prizewheel_spins WHERE uuid = ?", uniqueId.toString()), (Function<Rows, Integer>) rows -> {
            if (rows != null && rows.first() != null) {
                return rows.first().getInt("spins");
            }
            return 0;
        });
    }

    public ListenableFuture<Integer> addSpins(UUID uniqueId, int amount) {
        return this.mySQL.execute2("INSERT INTO prizewheel_spins (uuid, spins) VALUES (?,?) ON DUPLICATE KEY " +
                "UPDATE spins = GREATEST(0, spins + VALUES(spins))", uniqueId.toString(), amount);
    }

    public ListenableFuture<Integer> removeSpins(UUID uniqueId, int amount) {
        return this.mySQL.execute2("INSERT INTO prizewheel_spins (uuid) VALUES (?) ON DUPLICATE KEY " +
                "UPDATE spins = GREATEST(0, spins - ?)", uniqueId.toString(), amount);
    }
}
