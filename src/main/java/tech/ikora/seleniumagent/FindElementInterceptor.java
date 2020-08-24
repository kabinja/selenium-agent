package tech.ikora.seleniumagent;

import net.bytebuddy.asm.Advice;

import tech.ikora.seleniumagent.helpers.AgentHelper;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Arrays;

public class FindElementInterceptor {
    @Advice.OnMethodExit(onThrowable = RuntimeException.class)
    public static void exit(@Advice.This Object driver, @Advice.AllArguments Object[] args, @Advice.Thrown Throwable throwable) {
        try (Socket socket = new Socket("localhost", 8085)){
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            AgentHelper.initializeFrame(out);
            AgentHelper.sendMessage(out, 'u', AgentHelper.getCurrentUrl(driver));
            AgentHelper.sendMessage(out, 'a', Arrays.toString(args));
            AgentHelper.sendMessage(out, 'd', AgentHelper.getDom(driver));
            AgentHelper.sendMessage(out, 's', AgentHelper.getStackTrace());
            AgentHelper.sendMessage(out, 'w', AgentHelper.getWindowWidth(driver));
            AgentHelper.sendMessage(out, 'h', AgentHelper.getWindowHeight(driver));
            AgentHelper.sendMessage(out, 'f', AgentHelper.getFailure(throwable));

        } catch (Throwable e) {
            System.out.println("Something went terribly wrong: " + e.getMessage());
        }
    }
}
