package tech.ikora.seleniumagent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import org.openqa.selenium.By;

import java.lang.instrument.Instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.*;

public class SeleniumReporter {
    public static void premain(final String agentArgs,
                               final Instrumentation inst) {
        System.out.println("Starting to collect metrics");

        new AgentBuilder.Default()
                .with(new AgentBuilder.InitializationStrategy.SelfInjection.Eager())
                .type(named("org.openqa.selenium.remote.RemoteWebDriver"))
                .transform((builder, type, classLoader, module) -> builder
                        .method(nameStartsWith("findElement")
                                .and(takesArguments(By.class).or(takesArguments(String.class, String.class)))
                                .and(isPublic())
                        )
                        .intercept(Advice.to(FindElementInterceptor.class))
                )
                .installOn(inst);
    }
}