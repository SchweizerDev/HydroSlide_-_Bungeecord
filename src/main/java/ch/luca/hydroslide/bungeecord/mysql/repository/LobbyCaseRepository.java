package ch.luca.hydroslide.bungeecord.mysql.repository;

import ch.luca.hydroslide.bungeecord.mysql.MySQL;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.UUID;

public class LobbyCaseRepository {

    private MySQL mySQL;

    public LobbyCaseRepository(MySQL mySQL) {
        this.mySQL = mySQL;

        mySQL.execute2("CREATE TABLE IF NOT EXISTS lobby_cases (uuid VARCHAR(36), cases INT, PRIMARY KEY(uuid))");
    }

    public ListenableFuture<Integer> getCases(UUID uniqueId) {
        return Futures.transform(this.mySQL.query2("SELECT cases FROM lobby_cases WHERE uuid = ?", uniqueId.toString()), rows -> {
            if (rows != null && rows.first() != null) {
                return rows.first().getInt("cases");
            }
            return 0;
        });
    }

    public ListenableFuture<Integer> addCase(UUID uniqueId) {
        return this.addCases(uniqueId, 1);
    }

    public ListenableFuture<Integer> addCases(UUID uniqueId, int amount) {
        return Futures.transformAsync(this.getCases(uniqueId), cases -> {
            cases += amount;
            return this.mySQL.execute2("REPLACE INTO lobby_cases VALUES (?,?)", uniqueId.toString(), cases);
        });
    }

    public ListenableFuture<Integer> removeCase(UUID uniqueId) {
        return this.removeCases(uniqueId, 1);
    }

    public ListenableFuture<Integer> removeCases(UUID uniqueId, int amount) {
        return Futures.transformAsync(this.getCases(uniqueId), cases -> {
            cases -= amount;
            if (cases < 0) {
                cases = 0;
            }
            if (cases == 0) {
                return this.mySQL.execute2("DELETE FROM lobby_cases WHERE uuid = ?", uniqueId.toString());
            }
            return this.mySQL.execute2("REPLACE INTO lobby_cases VALUES (?,?)", uniqueId.toString(), cases);
        });
    }
}