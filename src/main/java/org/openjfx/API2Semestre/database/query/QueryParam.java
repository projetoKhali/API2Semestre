package org.openjfx.api2semestre.database.query;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

public class QueryParam <T> {
    protected TableProperty property;
    protected LinkedList<T> values;

    public QueryParam(TableProperty property, T value) {
        this.property = property;
        this.values = new LinkedList<T>(List.of(value));
    }

    public QueryParam<T> or (T item) {
        values.add(item);
        return this;
    }

    public TableProperty getProperty() { return property; }
    public LinkedList<T> getValues() { return values; }

    protected String build (QueryType type) {
        switch (type) {
            case INSERT: return property.getStringValue();
            default: 
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < values.size(); i++) {
                    sb.append(property.getStringValueWhere());
                    if (i < values.size() - 1) sb.append(" OR ");
                }
                return sb.toString();
        }
    }

    protected void apply (PreparedStatement statement, int index) throws Exception {
        for (T value : values) {
            if (value instanceof Integer) {
                statement.setInt(index++, (int)value);
            } else if (value instanceof Boolean) {
                statement.setBoolean(index++, (boolean)value);
            } else if (value instanceof String) {
                statement.setString(index++, (String)value);
            } else if (value instanceof Timestamp) {
                statement.setTimestamp(index++, (Timestamp)value);
            } else {
                throw new Exception("Unimplemented QueryParam.apply for T of type '" + value.getClass() + "'");
            }
        }
    }
}
