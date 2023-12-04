package edu.pnu;

import edu.pnu.demo.controllers.PersonController;
import edu.pnu.demo.entities.Person;
import edu.pnu.demo.services.PersonService;
import edu.pnu.myspring.annotations.MySpringApplication;
import edu.pnu.myspring.boot.MySpringApplicationRunner;
import edu.pnu.myspring.core.MyApplicationContext;

import java.util.List;

@MySpringApplication
public class MySpringDemoApplication {
    public static void main(String[] args) {
        MyApplicationContext context = MySpringApplicationRunner.run(MySpringDemoApplication.class, args);
    }
}
