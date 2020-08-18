package tech.ikora.seleniumagent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassInjector;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.utility.JavaModule;

import java.lang.invoke.MethodHandles;

import static java.util.Collections.singletonMap;

public class ClassLoaderTransformer implements AgentBuilder.Transformer {
    private final Class<?> targetClass;

    public ClassLoaderTransformer(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    @Override
    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule module) {
        try {
            resolveClassLoadingStrategy(targetClass).load(classLoader, singletonMap(
                    new TypeDescription.ForLoadedType(targetClass),
                    ClassFileLocator.ForClassLoader.read(targetClass)
            ));
        } catch (IllegalAccessException e) {
            System.out.println("Failed to load class: " + e.getMessage());
        }
        catch (Exception e){
            System.out.println("Something went terribly wrong: " + e.getMessage());
        }

        return builder;
    }

    private static ClassLoadingStrategy<ClassLoader> resolveClassLoadingStrategy(Class<?> targetClass) throws IllegalAccessException {
        if ( !ClassInjector.UsingLookup.isAvailable() ) {
            return new ClassLoadingStrategy.ForUnsafeInjection(targetClass.getProtectionDomain() );
        }

        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandles.Lookup privateLookup = MethodHandles.privateLookupIn(targetClass, lookup);

        return ClassLoadingStrategy.UsingLookup.of( privateLookup );
    }
}
