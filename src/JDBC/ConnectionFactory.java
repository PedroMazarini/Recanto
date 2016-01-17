/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Uma fábrica de conexões.
 *
 * @author David Buzatto
 */
public class ConnectionFactory {

    /*
     * Este bloco estático será executado assim que esta classe for carregada,
     * sendo assim, será executado apenas uma vez.
     */
    static {
        try {
            /*
             * Carrega a classe com.mysql.jdbc.Driver, que é a implementação
             * do driver JDBC para o MySQL.
             */
            Class.forName( "com.mysql.jdbc.Driver" );

            // caso a classe não seja encontrada
        } catch ( ClassNotFoundException exc ) {

            /*
             * Como log usaremos o stacktrace das excessões.
             */
            exc.printStackTrace();

        }
    }

    /**
     * O método getConnection retorna uma conexão com a base de dados
     * cadastro_clientes.
     *
     * @return Uma conexão com o banco de dados testes_padroes.
     * @throws SQLException Caso ocorra algum problema durante a conexão.
     */
    public static Connection getConnection() throws SQLException {

        // o método getConnection de DriverManagaer recebe como parâmetro
        // a URL da base de dados, o usuário usado para conectar na base
        // e a senha deste usuário.
        return DriverManager.getConnection(
                "jdbc:mysql://localhost/db_recanto",
                "root",
                "1234" );

    }

}
