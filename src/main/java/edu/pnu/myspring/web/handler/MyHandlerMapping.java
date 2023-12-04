package edu.pnu.myspring.web.handler;

import edu.pnu.myspring.annotations.Mapping;
import edu.pnu.myspring.web.dispatcher.ControllerRegistry;
import edu.pnu.myspring.web.http.UserRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MyHandlerMapping {

    Map<Class<?>, Object> registry;
    public MyHandlerMapping(ControllerRegistry registry) {
        this.registry = registry.getRegistry();
    }

    public Method getHandler(String method,String uri){
        List<Method> collect = registry.keySet()
                .stream()
                .filter(instance -> instance.isAnnotationPresent(Mapping.class))
                .flatMap(instance -> Arrays.stream(instance.getDeclaredMethods()))
                .filter(field -> pathMatch(uri, field))
                .filter(field -> methodMatch(method, field))
                .collect(Collectors.toList());

        if (collect.size() > 1){
            throw new IllegalStateException("http  request에 대한 중복이 존재합니다.");
        }

        return collect.get(0);
    }

    public Map<String, String> extractPathVariables(UserRequest request){
        Map<String, String> map = new HashMap<>();
        return extractPathVariable(findOriginUriByMethodAndUri(request.getMethod(), request.getUri()),
                request.getUri(),
                map);
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
        map.put(key, pathVariable.replaceAll("/", ""));

        return extractPathVariable(prefix+ pathVariable + origin.substring(postfixIdx+1)
                ,uri
                ,map);
    }
    public Object[] extractArgsForMethod(Method method, Map<String, Object> params){
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] args = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> paramType = parameterTypes[i];
            String paramName = method.getParameters()[i].getName();
            Object paramValue = params.get(paramName);
            args[i] = convertToType(paramValue, paramType);
        }
        return args;
    }

    private static Object convertToType(Object value, Class<?> targetType) {
        return targetType.cast(value);
    }



    private String findOriginUriByMethodAndUri(String method, String uri){
        return registry.keySet()
                .stream()
                .filter(instance -> instance.isAnnotationPresent(Mapping.class))
                .flatMap(instance -> Arrays.stream(instance.getDeclaredAnnotationsByType(Mapping.class)))
                .filter(annotation -> annotation.method().equals(method))
                .filter(annotation -> annotation.value().equals(uri))
                .map(Mapping::value)
                .collect(Collectors.toList())
                .get(0);
    }

    private static boolean pathMatch(String uri, Method method){
        method.setAccessible(true);

         return Arrays.stream(method.getAnnotationsByType(Mapping.class))
                 .anyMatch(annotation -> uri.matches(annotation.value()
                         .replaceAll("\\{[^}]*\\}",".*")));
    }

    private boolean methodMatch(String HttpMethod, Method method){
        method.setAccessible(true);

        return Arrays.stream(method.getAnnotationsByType(Mapping.class))
                .anyMatch(annotation -> annotation.method().equals(HttpMethod));
    }
}
