package tech.ikora.seleniumagent;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class FindElementInterceptor {
    public static void intercept(@Origin String method, @AllArguments Object[] args) {
        //System.out.println(getCommitId());
        System.out.println(String.format("start > %s ( %s )", method, Arrays.toString(args)));
    }

    private static String getCommitId(){
        StringBuilder stringBuilder = new StringBuilder();

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c","git rev-parse HEAD");
            processBuilder.redirectErrorStream(true);
            Process p = processBuilder.start();

            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while (true) {
                String line = r.readLine();
                if (line == null) { break; }
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("<NULL>");
        }

        return stringBuilder.toString();
    }
}
