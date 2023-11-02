package edu.pnu.myspring.boot;

import edu.pnu.myspring.annotations.MySpringApplication;
import edu.pnu.myspring.core.MyApplicationContext;

public class MySpringApplicationRunner {
    public static MyApplicationContext run(Class<?> clazz, String...args){
        if (!clazz.isAnnotationPresent(MySpringApplication.class)){
            throw new RuntimeException("The class must be annotated with @MySpringApplication");
        }
        String basePackage = clazz.getPackageName();
        MyApplicationContext context = new MyApplicationContext(basePackage);

        return context;
    }
}
