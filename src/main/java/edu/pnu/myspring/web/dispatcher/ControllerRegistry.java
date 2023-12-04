package edu.pnu.myspring.web.dispatcher;

import edu.pnu.myspring.annotations.MyRestController;
import edu.pnu.myspring.core.MyApplicationContext;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ControllerRegistry {

    private Map<Class<?>, Object> registry = new HashMap<>();
    public ControllerRegistry(MyApplicationContext context) {
        try {


            context.getClass()
                    .getDeclaredField("beanRegistry")
                    .setAccessible(true);
            Map<Class<?>, Object> beanRegistry = (Map<Class<?>, Object>) context
                    .getClass()
                    .getDeclaredField("beanRegistry")
                    .get(context);

            beanRegistry.keySet()
                    .stream()
                    .filter(this::isController)
                    .forEach(clazz -> registry.put(clazz, beanRegistry.get(clazz)));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    public Map<Class<?>, Object> getRegistry() {
        return registry;
    }

    private boolean isController(Class<?> clazz){
        return Arrays.stream(clazz.getDeclaredFields())
                .anyMatch(field -> field.isAnnotationPresent(MyRestController.class));

    }
}
