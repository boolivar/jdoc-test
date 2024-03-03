package org.bool.jdoc.spock;

import lombok.SneakyThrows;

import java.lang.reflect.Constructor;

public class ClassIntrospector {

    @SneakyThrows
    public <T> Constructor<T> findMockConstructor(Class<T> targetClass) {
        return targetClass.getConstructor();
    }
}
