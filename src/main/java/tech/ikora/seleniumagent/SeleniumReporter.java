package tech.ikora.seleniumagent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;

public class SeleniumReporter {
    public static void premain(final String agentArgs,
                               final Instrumentation inst) {
        System.out.println("Starting to collect metrics");

        new AgentBuilder.Default()
                .ignore(ElementMatchers.none())
                .with(new AgentBuilder.InitializationStrategy.SelfInjection.Eager())
                .type(ElementMatchers.any())
                .transform(new LocatorReporterTransformer())
                //.with(AgentBuilder.Listener.StreamWriting.toSystemOut())
                .with(AgentBuilder.TypeStrategy.Default.REDEFINE)
                .installOn(inst);
    }
}