package org.openjfx.api2semestre.database.query;

import java.sql.PreparedStatement;
import java.sql.Timestamp;

public class QueryParam <T> {
    protected TableProperty property;
    protected T value;

    public QueryParam(TableProperty property, T value) {
        this.property = property;
        this.value = value;
    }

    public TableProperty getProperty() { return property; }
    public T getvalue() { return value; }

    protected String build (QueryType type) {
        switch (type) {
            case INSERT: return property.getStringValue();
            default: return property.getStringValueWhere();
        }
    }

    protected void apply (PreparedStatement statement, int index) throws Exception {
        if (value instanceof Integer) {
            statement.setInt(index, (int)value);
        } else if (value instanceof Boolean) {
            statement.setBoolean(index, (boolean)value);
        } else if (value instanceof String) {
            statement.setString(index, (String)value);
        } else if (value instanceof Timestamp) {
            statement.setTimestamp(index, (Timestamp)value);
        } else {
            throw new Exception("Unimplemented QueryParam.apply for T of type '" + value.getClass() + "'");
        }
    }
}
