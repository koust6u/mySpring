package edu.pnu.demo.controllers;

import edu.pnu.demo.entities.Person;
import edu.pnu.demo.services.PersonService;
import edu.pnu.myspring.annotations.MyAutowired;
import edu.pnu.myspring.annotations.MyRestController;

@MyRestController
public class PersonController {

    @MyAutowired
    private PersonService service;



    public PersonController() {
    }

    public void createPerson(String name, int age) {
        service.save(name, age);
    }

    public Person getPerson(Long id) {
        return service.findById(id);
    }
}