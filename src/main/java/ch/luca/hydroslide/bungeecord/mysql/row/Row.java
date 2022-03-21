package ch.luca.hydroslide.bungeecord.mysql.row;

import lombok.Getter;

import java.util.Map;

public class Row {

    @Getter
    private Map<String, Object> values;

    public Row(Map<String, Object> results) {
        this.values = results;
    }

    public String getString(String columnName) {
        return this.get(columnName, String.class, "");
    }

    public int getInt(String columnName) {
        String sValue = String.valueOf(this.getObject(columnName));
        if(sValue.contains(".")) {
            return (int) Double.parseDouble(sValue);
        }
        return Integer.parseInt(sValue);
    }

    public double getDouble(String columnName) {
        String sValue = String.valueOf(this.getObject(columnName));
        return Double.parseDouble(sValue);
    }

    public long getLong(String columnName) {
        String sValue = String.valueOf(this.getObject(columnName));
        if(sValue.contains(".")) {
            return (long) Double.parseDouble(sValue);
        }
        return Long.parseLong(sValue);
    }

    public boolean getBoolean(String columnName) {
        String sValue = String.valueOf(this.getObject(columnName));
        return Boolean.parseBoolean(sValue);
    }

    private <T> T get(String columnName, Class<T> clazz, T defaultValue) {
        columnName = columnName.toLowerCase();

        if (!this.values.containsKey(columnName)) {
            return null;
        }

        Object object = this.values.get(columnName);
        if (object == null) {
            return defaultValue;
        }

        if (clazz.isInstance(object)) {
            return clazz.cast(object);
        }
        return null;
    }

    private Object getObject(String columnName) {
        columnName = columnName.toLowerCase();

        if (!this.values.containsKey(columnName)) {
            return null;
        }

        return this.values.get(columnName);
    }

    public <T> T get( String columnName, Class<T> clazz ) {
        // Make name lowercase
        columnName = columnName.toLowerCase();

        // Check if column name exists
        if ( !this.values.containsKey( columnName ) ) {
            return null;
        }
        // Get object from column name
        Object object = this.values.get( columnName );

        if(object == null) {
            return null;
        }

        // Check if object is instance of class
        if ( clazz.isInstance( object ) ) {
            // Cast object
            return clazz.cast( object );
        }
        return null;
    }
}
