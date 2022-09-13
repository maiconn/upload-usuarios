package br.com.gerarusuarios.shell;

import java.io.IOException;
import java.util.concurrent.Executors;

public class ExecutarSh {
    public static void executarCriacaoUserJenkins(String user, String senha, String email) throws IOException, InterruptedException {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("sudo",
                "sh",
                "/usr/local/bin/jenkins_user/create-user.sh",
                user,
                senha,
                email);
        Process process = builder.start();
        StreamGobbler streamGobbler =
                new StreamGobbler(process.getInputStream(), process.getErrorStream(), System.out::println);
        Executors.newSingleThreadExecutor().submit(streamGobbler);
//        int exitCode = process.waitFor();
    }
}
