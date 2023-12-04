package edu.pnu.myspring.web.handler;

import edu.pnu.myspring.web.http.UserRequest;
import edu.pnu.myspring.web.http.UserResponse;
import java.lang.reflect.Method;

public class MyHandlerAdapter {

    public MyHandlerAdapter(MyHandlerMapping myHandlerMapping) {
    }

    public UserResponse handle(UserRequest request, Method handler, Object[] args){
        System.out.println("aa");
        return new UserResponse(200, "{\"aa\":\"bb\"}");
    }
}
