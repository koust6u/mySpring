package edu.pnu.myspring.web.dispatcher;

import edu.pnu.myspring.annotations.MyRestController;
import edu.pnu.myspring.core.MyApplicationContext;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ControllerRegistry {

    private Map<Class<?>, Object> registry = new HashMap<>();
    public ControllerRegistry(MyApplicationContext context) {
        try {

            Field registryField = MyApplicationContext.class
                    .getDeclaredField("beanRegistry");
            registryField.setAccessible(true);

            Map<Class<?>, Object> registry = (Map<Class<?>, Object>) registryField.get(context);

            registry.keySet()
                    .stream()
                    .filter(this::isController)
                    .forEach(clazz -> this.registry.put(clazz, registry.get(clazz)));

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    public Map<Class<?>, Object> getRegistry() {
        return registry;
    }

    private boolean isController(Class<?> clazz){
        return clazz.isAnnotationPresent(MyRestController.class);

    }
}
