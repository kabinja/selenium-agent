package tech.ikora.seleniumagent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

public class LocatorReporterTransformer implements AgentBuilder.Transformer {

    @Override
    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder,
                                            TypeDescription typeDescription,
                                            ClassLoader classLoader,
                                            JavaModule javaModule) {

        System.out.println(typeDescription.getName());

        return builder.method(ElementMatchers.anyOf("findElements", "findElement"))
                .intercept(MethodDelegation.to(FindElementInterceptor.class));
    }
}
