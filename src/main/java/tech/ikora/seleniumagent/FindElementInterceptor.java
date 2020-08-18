package tech.ikora.seleniumagent;

import net.bytebuddy.asm.Advice;

import org.openqa.selenium.remote.RemoteWebDriver;
import tech.ikora.seleniumagent.helpers.SourcePageFetcher;

import java.util.Arrays;

public class FindElementInterceptor {
    @Advice.OnMethodEnter
    public static void log(@Advice.This RemoteWebDriver driver, @Advice.Origin String method, @Advice.AllArguments Object[] args) {
        System.out.printf("%s ( %s )%n", method, Arrays.toString(args));
        System.out.println(driver.getCurrentUrl());

        final String html = SourcePageFetcher.getCurrentDom(driver);
        System.out.println(html);

        StringBuilder stringBuilder = new StringBuilder();
        for(StackTraceElement st: Thread.currentThread().getStackTrace()){
            final String name = String.format("%s:%s", st.getClassName(), st.getMethodName());

            if(name.startsWith("org.codehaus.plexus.")
            || name.startsWith("org.apache.maven.")
            || name.startsWith("org.testng.")
            || name.startsWith("org.junit.")
            || name.startsWith("jdk.internal.reflect.")
            || name.startsWith("java.lang.Thread")
            || name.startsWith("java.lang.reflect.Method")){
                continue;
            }

            stringBuilder.append(name);
            stringBuilder.append(";");
        }
        String stackTrace = stringBuilder.toString();
        System.out.println(stackTrace);
    }
}
