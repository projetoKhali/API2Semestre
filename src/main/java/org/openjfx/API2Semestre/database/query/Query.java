package org.openjfx.api2semestre.database.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

public class Query {
    private QueryType type;
    private QueryTable table;
    // private String queryStr;
    private QueryParam<?>[] params;

    public Query (QueryType queryType, QueryTable table, QueryParam<?>[] params) {
        this.type = queryType;
        this.table = table;
        // this.queryStr = queryStr;
        this.params = params;
    }

    public Optional<ResultSet> execute (Connection c) {
        Optional<ResultSet> result = Optional.empty();
        StringBuilder sb = new StringBuilder(type.getStringValue());
        sb.append(" ").append(table.getStringValue());
        try {
            PreparedStatement statement = c.prepareStatement(sb.toString());
            for (int i = 0; i < params.length; i++) {
                QueryParam<?> param = params[i];
                try {
                    param.apply(statement, i + 1);
                } catch (Exception ex) {
                    System.out.println("database.query.Query -- Erro ao aplicar parametro à query!");
                    ex.printStackTrace();
                }
            }
            try {
                switch (type) {
                    case SELECT:
                        result = Optional.of(statement.executeQuery());
                    break;
                    default:
                        statement.execute();
                    break;
                }
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
