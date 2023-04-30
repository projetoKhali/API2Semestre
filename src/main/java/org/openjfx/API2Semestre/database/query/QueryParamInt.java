package org.openjfx.api2semestre.database.query;

import java.sql.PreparedStatement;

public class QueryParamInt extends QueryParam<Integer> {
    public QueryParamInt(Integer value) {
        super(value);
    }

    @Override
    public void apply(PreparedStatement statement, int index) throws Exception {
        statement.setInt(index, value);
    }
    
}
