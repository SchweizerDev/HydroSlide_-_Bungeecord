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

    public ListenableFuture<Integer> addCases(UUID uniqueId, int amount) {
        return this.mySQL.execute2("INSERT INTO lobby_cases (uuid, cases) VALUES (?,?) ON DUPLICATE KEY " +
                "UPDATE cases = GREATEST(0, cases + VALUES(cases))", uniqueId.toString(), amount);
    }

    public ListenableFuture<Integer> removeCases(UUID uniqueId, int amount) {
        return this.mySQL.execute2("INSERT INTO lobby_cases (uuid) VALUES (?) ON DUPLICATE KEY " +
                "UPDATE cases = GREATEST(0, cases - ?)", uniqueId.toString(), amount);
    }
}
