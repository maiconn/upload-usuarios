package br.com.gerarusuarios.bancodedados;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoComOracle {
    public static void createSchema(String schema, String pass) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:oracle:thin:@10.0.20.80:1521/xe", System.getProperty("db.user"), System.getProperty("db.pass"));

        con.createStatement().executeUpdate("create user " + schema + " identified by " + pass);
        con.createStatement().executeUpdate("GRANT CONNECT TO " + schema);
        con.createStatement().executeUpdate("GRANT CONNECT, RESOURCE TO " + schema);
        con.createStatement().executeUpdate("GRANT CREATE SESSION TO " + schema);
        con.createStatement().executeUpdate("grant create view, create procedure, create sequence to " + schema);
        con.createStatement().executeUpdate("GRANT UNLIMITED TABLESPACE TO " + schema);
        con.createStatement().executeUpdate("GRANT CREATE MATERIALIZED VIEW TO " + schema);
        con.createStatement().executeUpdate("GRANT CREATE TABLE TO " + schema);

        con.close();
    }
}
