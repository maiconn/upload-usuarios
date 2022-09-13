package br.com.gerarusuarios;

import br.com.gerarusuarios.email.Email;
import org.apache.commons.lang3.RandomStringUtils;

import javax.mail.MessagingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException, SQLException, MessagingException, URISyntaxException {
        String workspace = args[0];
        String arquivo = args[1];
        File file = new File(workspace + "/" + arquivo);
        Scanner scan = new Scanner(new FileInputStream(file));
        while (scan.hasNext()) {
            String email = scan.nextLine();
            String nomeUsuario = email.split("@")[0].replace(".", "_");
            String senha = gerarSenha();
            System.out.printf("%s - %s - %s\n", email, nomeUsuario, senha);
            Email.enviarEmail(email, nomeUsuario, senha);
            //ConexaoComOracle.createSchema(nomeUsuario, senha);
            //ExecutarSh.executarCriacaoUserJenkins(nomeUsuario, senha, email);
        }
        scan.close();
    }

    private static String gerarSenha() {
        int length = 12;
        boolean useLetters = true;
        boolean useNumbers = false;
        return RandomStringUtils.random(length, useLetters, useNumbers);
    }

}