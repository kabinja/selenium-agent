package tech.ikora.seleniumagent;

import net.bytebuddy.asm.Advice;

import org.openqa.selenium.remote.RemoteWebDriver;
import tech.ikora.seleniumagent.helpers.AgentHelper;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

public class FindElementInterceptor {
    @Advice.OnMethodEnter
    public static void log(@Advice.This RemoteWebDriver driver, @Advice.AllArguments Object[] args) {
        try (Socket socket = new Socket("localhost", 8085)){
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            final String dom = (String)driver.executeScript(AgentHelper.getJsCode());

            AgentHelper.sendMessage(out, 'u', driver.getCurrentUrl());
            AgentHelper.sendMessage(out, 'a', Arrays.toString(args));
            AgentHelper.sendMessage(out, 'd', dom);
            AgentHelper.sendMessage(out, 's', AgentHelper.getStackTrace());

        } catch (IOException e) {
            System.out.println("Something went terribly wrong: " + e.getMessage());
        }
    }
}
