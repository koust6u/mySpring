package edu.pnu.myspring.web.handler;

import edu.pnu.myspring.annotations.MyRequestMapping;
import edu.pnu.myspring.annotations.PathVariable;
import edu.pnu.myspring.annotations.PostMapping;
import edu.pnu.myspring.web.dispatcher.ControllerRegistry;
import edu.pnu.myspring.web.http.UserRequest;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


public class MyHandlerMapping {

    private Map<Class<?>, Object> registry;
    public MyHandlerMapping(ControllerRegistry registry) {
        this.registry = registry.getRegistry();
    }


    public Object findInstanceOfController(String uri){
        for (Class<?> clazz : registry.keySet()) {
            Method[] declaredMethods = clazz.getDeclaredMethods();
            for (Method declaredMethod : declaredMethods) {
                if (declaredMethod.isAnnotationPresent(MyRequestMapping.class) ||
                declaredMethod.isAnnotationPresent(PostMapping.class)){
                    if (pathMatch(uri, declaredMethod)){
                        return this.registry.get(clazz);
                    }
                }
            }
        }
        return null;
    }
    public Method getHandler(String method,String uri){
        List<Method> collect = registry.keySet()
                .stream()
                .flatMap(instance -> Arrays.stream(instance.getDeclaredMethods()))
                .filter(field -> field.isAnnotationPresent(MyRequestMapping.class)||
                        field.isAnnotationPresent(PostMapping.class))
                .filter(field -> pathMatch(uri, field))
                .filter(field -> methodMatch(method, field))
                .collect(Collectors.toList());

        if (collect.size() > 1){
            throw new IllegalStateException("http request에 대한 중복이 존재합니다.");
        }

        return collect.get(0);
    }

    public Map<String, String> extractPathVariables(UserRequest request){
        Map<String, String> map = new HashMap<>();
        Method handler = getHandler(request.getMethod(), request.getUri());
        if (handler.isAnnotationPresent(MyRequestMapping.class)){
            String origin = handler.getAnnotation(MyRequestMapping.class).value();
            return extractPathVariable(origin,request.getUri(),map);
        }
        return map;
    }


    public  Map<String ,String> extractPathVariable(String origin,String uri ,Map<String, String> map){
        int prefixIdx = origin.indexOf("{");
        int postfixIdx = origin.indexOf("}");

        if (prefixIdx == -1) return map;
        String prefix = origin.substring(0, prefixIdx-1);
        String postfix = uri.substring(prefixIdx);
        postfix = postfix.substring(postfix.contains("/")?postfix.indexOf("/"):0);

        String key = origin.substring(prefixIdx+1, postfixIdx);

        String pathVariable = uri.replaceAll(prefix, "");
        pathVariable = pathVariable.replaceAll(postfix, "");
        if (postfixIdx == origin.length()-1){
            pathVariable = postfix;
        }
        if (!pathVariable.replaceAll("/", "").isBlank()){
            map.put(key, pathVariable.replaceAll("/", ""));
        }
        return extractPathVariable(prefix+ pathVariable + origin.substring(postfixIdx+1)
                ,uri
                ,map);
    }
    public Object[] extractArgsForMethod(Method method, Map<String, Object> params){
        Object[] args = new Object[method.getParameters().length];
        AtomicInteger idx = new AtomicInteger();
        Arrays.stream(method.getParameters())
                .sequential()
                .filter(e -> e.isAnnotationPresent(PathVariable.class))
                .map(e -> e.getAnnotation(PathVariable.class).value())
                .forEach(e -> args[idx.getAndIncrement()] = params.get(e));
        return args;
    }






    private boolean pathMatch(String uri, Method method){
        method.setAccessible(true);

        boolean flag = Arrays.stream(method.getAnnotationsByType(MyRequestMapping.class))
                .anyMatch(annotation -> uri.matches(annotation.value()
                        .replaceAll("\\{[^}]*\\}", ".*")));

        if (flag) return true;

       return Arrays.stream(method.getAnnotationsByType(PostMapping.class))
                .anyMatch(annotation -> uri.matches(annotation.value()
                        .replaceAll("\\{[^}]*\\}", ".*")));

    }

    private boolean methodMatch(String HttpMethod, Method method){
        method.setAccessible(true);

        boolean flag = Arrays.stream(method.getAnnotationsByType(MyRequestMapping.class))
                .anyMatch(annotation -> annotation.method().equals(HttpMethod));
        if (flag) return true;

        return Arrays.stream(method.getAnnotationsByType(PostMapping.class))
                .anyMatch(annotation -> annotation.method().equals(HttpMethod));
    }
}
