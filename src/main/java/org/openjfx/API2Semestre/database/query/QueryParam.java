package org.openjfx.api2semestre.database.query;

import java.sql.PreparedStatement;

public class QueryParam <T> {
    protected T value;

    public QueryParam(T value) { this.value = value; }

    public T getValue() { return value; }

    protected void apply (PreparedStatement statement, int index) throws Exception {
        throw new Exception("Unimplemented QueryParam.apply for T = '" + this.value.getClass() + "'");
    }
    
}
