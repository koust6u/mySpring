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
        PersonController controller = context.getBean(PersonController.class);

        controller.createPerson("John",30);
        controller.createPerson("Jane", 25);
        System.out.println(controller.getPerson(1L));

        PersonService service = context.getBean(PersonService.class);
        List<Person> allPersons = service.findAll();
        System.out.println("All Persons: " + allPersons);

        service.delete(1L);
        System.out.println(controller.getPerson(1L));

        context.close();
    }
}
