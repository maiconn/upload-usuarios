package br.com.gerarusuarios.shell;

import java.io.IOException;
import java.util.concurrent.Executors;

public class ExecutarSh {
    public static void executarCriacaoUserJenkins(String user, String senha, String email) throws IOException, InterruptedException {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("sudo",
                "java",
                "-jar",
                "/usr/local/bin/jenkins_user/jenkins-cli.jar",
                "-s",
                "http://localhost:8080/",
                "-auth",
                "admin:11f9d8012168e0c6b670fba5400153c4e0",
                "groovy",
                "=",
                "<",
                "/usr/local/bin/jenkins_user/user-creation.groovy",
                user,
                senha,
                email);
        Process process = builder.start();
        StreamGobbler streamGobbler =
                new StreamGobbler(process.getInputStream(), process.getErrorStream(), System.out::println);
        Executors.newSingleThreadExecutor().submit(streamGobbler);
        int exitCode = process.waitFor();
    }
}
