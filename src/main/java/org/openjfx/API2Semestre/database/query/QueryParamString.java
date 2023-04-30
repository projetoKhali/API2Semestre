package org.openjfx.api2semestre.database.query;

import java.sql.PreparedStatement;

public class QueryParamString extends QueryParam<String> {
    public QueryParamString(String value) {
        super(value);
    }

    @Override
    public void apply(PreparedStatement statement, int index) throws Exception {
        statement.setString(index, value);
    }
    
}
