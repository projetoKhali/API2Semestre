package Classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AppointmentApproval {

    //Função de aprovação/reprovação de um apontamento de hora-extra ou sobreaviso
    //Os status são: 0 - Pendente; 1 - Aprovado; 2 - Reprovado
    public static void updateTable(Connection conexao, boolean aprovado, int apt_id) throws SQLException {
        
        String apv = "UPDATE apontamento SET aprovado = ? WHERE apt_id = ?";

        try(PreparedStatement statement = conexao.prepareStatement(apv)) {
            //Substitui os parâmetros "?" para os valores desejados
            statement.setInt(1, aprovado ? 1 : 2);
            statement.setInt(2, apt_id);
            
            //Executa o update
            statement.executeUpdate();

        } catch (Exception ex) {
            //Exibe erros ao executar a query
            System.out.println("Erro ao executar a query: " + ex.getMessage());
        }
    }
}
