package tech.ikora.seleniumagent;

import net.bytebuddy.asm.Advice;

import java.util.Arrays;

public class FindElementInterceptor {
    @Advice.OnMethodExit
    public static void log(@Advice.Origin String method, @Advice.AllArguments Object[] args) {
        System.out.printf("%s ( %s )%n", method, Arrays.toString(args));
    }
}
