package br.com.gerarusuarios;

import br.com.gerarusuarios.bancodedados.ConexaoComOracle;
import br.com.gerarusuarios.email.Email;
import br.com.gerarusuarios.shell.ExecutarSh;
import org.apache.commons.lang3.RandomStringUtils;

import javax.mail.MessagingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    /**
     * String[] args = $OPCAO $EMAIL $WORKSPACE "ARQUIVO"
     */
    public static void main(String[] args) throws IOException, InterruptedException, SQLException, MessagingException, URISyntaxException {
        // cria usuario banco
        String opcao = args[0];
        System.out.println(opcao);
        if ("--createDB".equals(opcao)) {
            String email = args[1];
            String[] credenciais = getCredentials(email);
            String nomeUsuario = credenciais[0];
            String senha = credenciais[1];
            System.out.printf("%s - %s - %s\n", email, nomeUsuario, senha);
            ConexaoComOracle.createSchema(nomeUsuario, senha);
            Email.enviarEmail(email, nomeUsuario, senha, false, true);

            // cria usuario jenkins
        } else if ("--createJenkins".equals(opcao)) {
            String email = args[1];
            String[] credenciais = getCredentials(email);
            String nomeUsuario = credenciais[0];
            String senha = credenciais[1];
            System.out.printf("%s - %s - %s\n", email, nomeUsuario, senha);
            ExecutarSh.executarCriacaoUserJenkins(nomeUsuario, senha, args[1]);
            Email.enviarEmail(email, nomeUsuario, senha, true, false);

            // cria usuarios em lote
        } else if ("--createFile".equals(opcao)) {
            String workspace = args[2];
            String arquivo = args[3];
            File file = new File(workspace + "/" + arquivo);
            Scanner scan = new Scanner(new FileInputStream(file));
            while (scan.hasNext()) {
                String email = scan.nextLine();
                if (email.trim().equals("")) {
                    break;
                }
                String[] credenciais = getCredentials(email);
                String nomeUsuario = credenciais[0];
                String senha = credenciais[1];
                System.out.printf("%s - %s - %s\n", email, nomeUsuario, senha);
//                ConexaoComOracle.createSchema(nomeUsuario, senha);
//                ExecutarSh.executarCriacaoUserJenkins(nomeUsuario, senha, email);
                Email.enviarEmail(email, nomeUsuario, senha, true, true);
            }
            scan.close();
        }
    }

    private static String gerarSenha() {
        int length = 12;
        boolean useLetters = true;
        boolean useNumbers = false;
        return RandomStringUtils.random(length, useLetters, useNumbers);
    }

    private static String[] getCredentials(String email) {
        return new String[]{email.split("@")[0].replace(".", "_"),
                gerarSenha()
        };
    }

}