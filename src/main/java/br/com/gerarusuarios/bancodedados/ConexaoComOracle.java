package br.com.gerarusuarios.bancodedados;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoComOracle {
    public static void createSchema(String schema, String pass) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:oracle:thin:@10.0.20.80:1521/xe", "system", "VemSer@2000!");

        con.createStatement().execute("create user " + schema + " identified by " + pass);
        con.createStatement().execute("GRANT CONNECT TO " + schema);
        con.createStatement().execute("GRANT CONNECT, RESOURCE TO " + schema);
        con.createStatement().execute("GRANT CREATE SESSION TO " + schema);
        con.createStatement().execute("grant create view, create procedure, create sequence to " + schema);
        con.createStatement().execute("GRANT UNLIMITED TABLESPACE TO " + schema);
        con.createStatement().execute("GRANT CREATE MATERIALIZED VIEW TO " + schema);
        con.createStatement().execute("GRANT CREATE TABLE TO " + schema);

        con.close();
    }
}
