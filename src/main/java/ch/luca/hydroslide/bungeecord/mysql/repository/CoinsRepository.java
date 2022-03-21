package ch.luca.hydroslide.bungeecord.mysql.repository;

import ch.luca.hydroslide.bungeecord.mysql.MySQL;

import java.util.UUID;
import java.util.function.Consumer;

public class CoinsRepository {

    private final MySQL mySQL;

    public CoinsRepository(MySQL mySQL) {
        this.mySQL = mySQL;
        mySQL.executeAsync("CREATE TABLE IF NOT EXISTS Coins (name VARCHAR(40), uuid VARCHAR(36), amount INT DEFAULT 1000, PRIMARY KEY(uuid))");
    }

    public void updateCoins(UUID uuid, String name) {
        this.mySQL.executeAsync("INSERT INTO Coins (name, uuid) VALUES (?,?) ON DUPLICATE KEY UPDATE name = VALUES(name)", name, uuid.toString());
    }

    public void getCoins(UUID uuid, Consumer<Integer> amount) {
        this.mySQL.queryAsync("SELECT amount FROM Coins WHERE uuid = ?", rows -> {
            if (rows != null && rows.first() != null) {
                amount.accept(rows.first().getInt("amount"));
                return;
            }
            amount.accept(null);
        }, uuid.toString());
    }

    public void addCoins(UUID uuid, int amount) {
        this.mySQL.executeAsync("UPDATE Coins SET amount = amount + ? WHERE uuid = ?", amount, uuid.toString());
    }

    public void removeCoins(UUID uuid, int amount) {
        this.mySQL.executeAsync("UPDATE Coins SET amount = GREATEST(0, amount - ?) WHERE uuid = ?", amount, uuid.toString());
    }

    public void resetCoins(UUID uuid) {
        this.mySQL.executeAsync("UPDATE Coins SET amount = 0 WHERE uuid = ?", uuid.toString());
    }

}
