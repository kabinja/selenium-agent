package tech.ikora.seleniumagent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassInjector;
import net.bytebuddy.utility.JavaModule;

import static java.util.Collections.singletonMap;

public class ClassLoaderTransformer implements AgentBuilder.Transformer {
    private final Class<?> targetClass;

    public ClassLoaderTransformer(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    @Override
    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule module) {
        try {
            ClassInjector.UsingUnsafe.ofBootLoader().inject(singletonMap(
                    new TypeDescription.ForLoadedType(targetClass),
                    ClassFileLocator.ForClassLoader.read(targetClass)
            ));
        } catch (Throwable e){
            System.out.println("Something went terribly wrong: " + e.getMessage());
            e.printStackTrace();
        }

        return builder;
    }
}
