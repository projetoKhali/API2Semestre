package org.openjfx.api2semestre.database.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

public class Query {
    private QueryType type;
    private QueryTable table;
    private QueryParam<?>[] params;

    public Query (QueryType queryType, QueryTable table, QueryParam<?>[] params) {
        this.type = queryType;
        this.table = table;
        this.params = params;
    }

    private PreparedStatement build (Connection c) {
        StringBuilder sb = new StringBuilder(type.getStringValue());
        sb.append(" ").append(table.getStringValue());
        switch (type) {

            case INSERT:
                sb.append(" (");
                buildParams(sb);
                sb.append(") values (");
                for (int i = 0; i < params.length; i++) {
                    sb.append("?");
                    if (i < params.length - 1) sb.append(", ");
                }
                sb.append(") RETURNING id");
            break;

            // SELECT
            case SELECT:
                if (params.length == 0) break;
                sb.append(" WHERE ");
                buildParams(sb);
            break;

            // SELECT
            case UPDATE:
                if (params.length == 0) break;
                sb.append(" SET ");
                buildParams(sb);
                sb.append(" WHERE ").append(params[params.length - 1].build(QueryType.SELECT));
            break;
        
            case DELETE:
                if (params.length == 0) break;
                sb.append(" WHERE ");
                buildParams(sb);
            break;

        }
        try {
            PreparedStatement statement = c.prepareStatement(sb.toString());
            for (int i = 0; i < params.length; i++) {
                QueryParam<?> param = params[i];
                try {
                    param.apply(statement, i + 1);
                } catch (Exception ex) {
                    System.out.println("database.query.Query.build() -- Erro ao aplicar parametro à query!");
                    ex.printStackTrace();
                }
            }
            return statement;
        } catch (Exception ex) {
            System.out.println("database.query.Query.build() -- Erro ao criar PreparedStatement!");
            ex.printStackTrace();
        }
        return null;
    }

    private void buildParams (StringBuilder sb) {
        int length = params.length - (type == QueryType.UPDATE ? 1 : 0);
        for (int i = 0; i < length; i++) {
            QueryParam<?> param = params[i];
            sb.append(param.build(type));
            if (i < length - 1) sb.append(", ");
        }
    }

    public Optional<ResultSet> execute (Connection c) {
        Optional<ResultSet> result = Optional.empty();
        try {
            PreparedStatement statement = build(c);
            // System.out.println(statement.toString());
            switch (type) {
                case SELECT: case INSERT:
                    result = Optional.of(statement.executeQuery());
                break;
                default:
                    statement.execute();
                break;
            }
        } catch (Exception ex) {
            System.out.println("database.query.Query -- Erro ao executar Query!");
            ex.printStackTrace();
        }
        return result;
    }
}
