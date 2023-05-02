package org.openjfx.api2semestre.database.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Query {
    private String queryStr;
    private QueryParam<?>[] params;

    public Query (String queryStr, QueryParam<?>[] params) {
        this.queryStr = queryStr;
        this.params = params;
    }

    public ResultSet execute (Connection c) {
        ResultSet result = null;
        try {
            PreparedStatement statement = c.prepareStatement(queryStr);
            for (int i = 0; i < params.length; i++) {
                QueryParam<?> param = params[i];
                try {
                    param.apply(statement, i);
                } catch (Exception ex) {
                    System.out.println("database.query.Query -- Erro ao aplicar parametro à query!");
                    ex.printStackTrace();
                }
            }
            try {
                result = statement.executeQuery();
            } catch (Exception ex) {
                System.out.println("database.query.Query -- Erro ao aplicar executar Query!");
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            System.out.println("database.query.Query -- Erro ao criar PreparedStatement!");
            ex.printStackTrace();
        }
        return result;
    }
}
