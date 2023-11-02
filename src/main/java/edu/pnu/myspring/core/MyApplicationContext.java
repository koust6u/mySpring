package edu.pnu.myspring.core;

import edu.pnu.myspring.annotations.*;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class MyApplicationContext {
    private Map<Class<?>, Object> beanRegistry = new HashMap<>();

    private List<Object> beansToAutowire = new ArrayList<>();

    private Map<Object, Method> postConstructMethodRegistry = new HashMap<>();

    private Map<Object, Method> preDestroyMethodRegistry = new HashMap<>();


    public MyApplicationContext(String basePackage) {
        scanAndRegisterBeans(basePackage);
        processAutowiring();
    }

    private void scanAndRegisterBeans(String basePackage) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String packagePath = basePackage.replaceAll("\\.", "/");
        File packageDir = new File(loader.getResource(packagePath).getFile());

        if (packageDir.isDirectory()) {
            Arrays.stream(packageDir.listFiles())
                    .filter(f -> f.isFile() && f.getName().endsWith(".class"))
                    .map(f -> basePackage + "." + f.getName().replaceAll("\\.class", ""))
                    .map(this::convertToClass)
                    .filter(e -> isComponent(e.getAnnotations()))
                    .forEach(this::registerBean);
            Arrays.stream(packageDir.listFiles())
                    .filter(f -> f.isDirectory())
                    .map(dir -> basePackage + "." + dir.getName())
                    .forEach(this::scanAndRegisterBeans);
        }

    }


    private void processAutowiring() {
        beanRegistry.values()
                .forEach(this::dependencyInject);
    }




    private Object getInstance(String name){
        for (Object o : beansToAutowire) {
            if (o.getClass().getName().equalsIgnoreCase(name)){
                return o;
            }
        }
        return null;
    }

    public <T> void registerBean(Class<? extends T> beanClass) {
        try {
            T instance = beanClass.getDeclaredConstructor().newInstance();
            beanRegistry.put(beanClass, instance);
            beansToAutowire.add(instance);
        } catch (Exception ignored) {
        }

    }

    public <T> T getBean(Class<T> type) {
        return type.cast(beanRegistry.get(type));
    }

    public void close() {
        for (Object o : preDestroyMethodRegistry.keySet()) {
            Method method = preDestroyMethodRegistry.get(o);
            try {
                method.invoke(o);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        this.beanRegistry.clear();
        this.postConstructMethodRegistry.clear();
        this.preDestroyMethodRegistry.clear();
        this.beansToAutowire.clear();
    }


    private boolean isComponent(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(MyService.class)
                    || annotation.annotationType().equals(MyRestController.class)
                    || annotation.annotationType().equals(MyRepository.class)) {
                return true;
            }
        }
        return false;
    }

    private Class<?> convertToClass(String className) {
        try {
            return Class.forName(className);
        } catch (Exception e) {
            System.out.println("can not found " + className);
        }
        return null;
    }

    private void dependencyInject(Object e) {
        try {
            autowired(e, e.getClass().getName());
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void autowired(Object o, String name) throws ClassNotFoundException {
        Class<?> clazz = Class.forName(name);
        Field[] fields = clazz.getDeclaredFields();
        Method[] methods = clazz.getDeclaredMethods();

        Arrays.stream(fields)
                .forEach(f -> f.setAccessible(true));
        Arrays.stream(methods)
                .filter(m -> m.isAnnotationPresent(PostConstruct.class))
                .forEach(m -> postConstruction(clazz, m));

        Arrays.stream(methods)
                .filter(m -> m.isAnnotationPresent(PreDestroy.class))
                .forEach(m -> preDestroyMethodRegistry.put(beanRegistry.get(clazz),m));

        Arrays.stream(fields)
                .filter(f -> f.isAnnotationPresent(MyAutowired.class))
                .forEach(f -> beenInject(o, f));

    }

    private void postConstruction(Class<?> clazz, Method method) {
        Object instance = beanRegistry.get(clazz);
        postConstructMethodRegistry.put(instance,method);
        try {
            method.invoke(instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void beenInject(Object o, Field f) {
        try {
            f.set(o, getInstance(f.getType().getName()));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
