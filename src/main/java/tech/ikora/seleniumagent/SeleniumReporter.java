package tech.ikora.seleniumagent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;

public class SeleniumReporter {
    public static void premain(final String agentArgs,
                               final Instrumentation inst) {
        System.out.println("Starting to collect metrics");

        new AgentBuilder.Default()
                .with(new AgentBuilder.InitializationStrategy.SelfInjection.Eager())
                .type(ElementMatchers.named("org.openqa.selenium.remote.RemoteWebDriver"))
                .transform((builder, type, classLoader, module) -> builder
                        .method(ElementMatchers.named("findElement")
                                .and(ElementMatchers.takesArguments(1))
                        )
                        .intercept(Advice.to(FindElementInterceptor.class)))
                .installOn(inst);
    }
}