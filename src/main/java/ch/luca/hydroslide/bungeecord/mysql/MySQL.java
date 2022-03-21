package ch.luca.hydroslide.bungeecord.mysql;

import ch.luca.hydroslide.bungeecord.mysql.row.Row;
import ch.luca.hydroslide.bungeecord.mysql.row.Rows;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class MySQL {

    private Connection connection;

    @Getter
    private final ExecutorService pool = Executors.newCachedThreadPool();

    @Getter
    private ListeningExecutorService pool2 = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());

    public MySQL(String host, String database, String username, String password, int port) {
        System.out.println("§cMySQL: Connecting to database " + database + "...");
        if (this.connect(host, database, username, password, port)) {
            System.out.println("§cMySQL: Successfully connected to database " + database + "!");
            return;
        }
        System.out.println("§cMySQL: Can't connect to " + database + "! Shutting down...");
        ProxyServer.getInstance().stop();
    }

    private boolean connect(String host, String database, String username, String password, int port) {
        try {
            Class.forName("com.mysql.jdbc.Driver");

            this.connection = DriverManager.getConnection("jdbc:mysql://" + host + ":"
                    + port + "/" + database + "?user="
                    + username + "&password=" + password
                    + "&autoReconnect=true");
        } catch (Exception e) {
            System.out.println("§cMySQL: Connection error! (DB: " + database + ")");
            e.printStackTrace();
        }
        return this.isConnected();
    }

    public boolean disconnect() {
        if (!this.isConnected()) {
            return true;
        }

        try {
            this.connection.close();
            return true;
        } catch (SQLException e) {
            System.out.println("§cMySQL: Can't close connection!");
            e.printStackTrace();
            return false;
        }
    }

    public boolean isConnected() {
        try {
            return this.connection != null && !this.connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int execute(String statement, Object... objects) {
        if (!this.isConnected()) {
            return -1;
        }

        try {
            PreparedStatement preparedStatement = this.getPreparedStatement(statement, objects);
            int result = preparedStatement.executeUpdate();
            preparedStatement.close();
            return result;
        } catch (SQLException e) {
            System.out.println("§cMySQL: Error while executing statement!");
            e.printStackTrace();
            return 0;
        }
    }

    public void executeAsync(String statement, Consumer<Integer> result, Object... objects) {
        this.pool.execute(() -> result.accept(this.execute(statement, objects)));
    }

    public void executeAsync(String statement, Object... objects) {
        this.pool.execute(() -> this.execute(statement, objects));
    }

    public Rows query(String statement, Object... objects) {
        // Check if connected
        if (!this.isConnected()) {
            return null;
        }

        try {
            // Get Prepared Statement
            PreparedStatement preparedStatement = this.getPreparedStatement(statement, objects);

            // Execute statement and get ResultSet
            ResultSet resultSet = preparedStatement.executeQuery();

            // Cache ResultSet into Rows
            Rows rows = this.getRows(resultSet);

            // Close statement
            preparedStatement.close();
            resultSet.close();

            return rows;
        } catch (SQLException e) {
            System.out.println("§cMySQL: Error while query statement!");
            e.printStackTrace();
            return new Rows();
        }
    }

    public void queryAsync(String statement, Consumer<Rows> result, Object... objects) {
        this.pool.execute(() -> result.accept(this.query(statement, objects)));
    }

    public ListenableFuture<Integer> execute2(String statement, Object... objects) {
        if (!this.isConnected()) {
            return Futures.immediateFailedFuture(new SQLException("No MySQL connection!"));
        }

        return this.pool2.submit(() -> {
            try {
                PreparedStatement preparedStatement = this.getPreparedStatement(statement, objects);
                int result = preparedStatement.executeUpdate();
                preparedStatement.close();
                return result;
            } catch (SQLException e) {
                System.out.println("[BungeeCommandPlugin] MySQL: Error while executing statement!");
                e.printStackTrace();
                return 0;
            }
        });
    }

    public ListenableFuture<Rows> query2(String statement, Object... objects) {
        if (!this.isConnected()) {
            return Futures.immediateFailedFuture(new SQLException("No MySQL connection!"));
        }

        return this.pool2.submit(() -> {
            try {
                PreparedStatement preparedStatement = this.getPreparedStatement(statement, objects);
                ResultSet resultSet = preparedStatement.executeQuery();
                Rows rows = this.getRows(resultSet);

                preparedStatement.close();
                resultSet.close();

                return rows;
            } catch (SQLException e) {
                System.out.println("[BungeeCommandPlugin] MySQL: Error while query statement!");
                e.printStackTrace();
                return new Rows();
            }
        });
    }

    private PreparedStatement getPreparedStatement(String statement, Object... objects) throws SQLException {
        PreparedStatement preparedStatement = this.connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);

        for (int i = 0; i < objects.length; i++) {
            preparedStatement.setObject((i + 1), objects[i]);
        }
        return preparedStatement;
    }

    private Rows getRows(ResultSet resultSet) throws SQLException {
        List<Row> rowList = new ArrayList<>();

        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        Map<String, Object> row;
        while (resultSet.next()) {
            row = new HashMap<>(columnCount);

            for (int i = 1; i <= columnCount; i++) {
                row.put(metaData.getColumnName(i).toLowerCase(), resultSet.getObject(i));
            }
            rowList.add(new Row(row));
        }
        return new Rows(rowList);
    }
}

