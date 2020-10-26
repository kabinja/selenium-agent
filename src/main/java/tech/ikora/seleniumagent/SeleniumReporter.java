package tech.ikora.seleniumagent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import tech.ikora.seleniumagent.helpers.AgentHelper;
import tech.ikora.seleniumagent.helpers.JsCode;

import java.lang.instrument.Instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.*;

public class SeleniumReporter {
    public static void premain(final String agentArgs,
                               final Instrumentation inst) {
        System.out.println("Starting to collect metrics");

        new AgentBuilder.Default()
                .with(new AgentBuilder.InitializationStrategy.SelfInjection.Eager())
                .type(named("org.openqa.selenium.remote.RemoteWebDriver"))
                .transform(new ClassLoaderTransformer(JsCode.class))
                .transform(new ClassLoaderTransformer(AgentHelper.class))
                .transform((builder, type, classLoader, module) -> builder
                        .method(namedOneOf("findElement", "findElements")
                                .and(takesArguments(1).or(takesArguments(String.class, String.class)))
                                .and(isPublic())
                        )
                        .intercept(Advice.withCustomMapping().bind(Port.class, Integer.parseInt(agentArgs)).to(FindElementInterceptor.class))
                )
                .installOn(inst);
    }
}