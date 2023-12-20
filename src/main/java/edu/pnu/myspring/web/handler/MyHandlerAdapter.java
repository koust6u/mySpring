package edu.pnu.myspring.web.handler;

import edu.pnu.myspring.web.http.UserRequest;
import edu.pnu.myspring.web.http.UserResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MyHandlerAdapter {

    private final MyHandlerMapping myHandlerMapping;

    public MyHandlerAdapter(MyHandlerMapping myHandlerMapping) {
        this.myHandlerMapping = myHandlerMapping;
    }

    public UserResponse handle(UserRequest request, Method handler, Object[] args){
        try {
            Class<?>[] parameterTypes = handler.getParameterTypes();
            Object instanceOfController = myHandlerMapping.findInstanceOfController(request.getUri());
            Object[] insertArgs = new Object[handler.getParameters().length];
            for(int i = 0 ; i < handler.getParameters().length; i++){
                insertArgs[i] = convertToType(args[i], parameterTypes[i]);
            }
            String invoke = (String)handler.invoke(instanceOfController,insertArgs);
            return new UserResponse(200, invoke);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object convertToType(Object value, Class<?> targetType) {

        if (targetType == String.class) {
            return value.toString();
        } else if (targetType == Integer.class) {
            return Integer.parseInt(value.toString());
        }else if (targetType == Long.class){
            return Long.parseLong(value.toString());
        }

        throw new IllegalArgumentException("Unsupported type conversion");
    }

}
