package edu.pnu.demo.services;

import edu.pnu.demo.entities.Person;
import edu.pnu.demo.repositories.PersonRepository;
import edu.pnu.myspring.annotations.MyAutowired;
import edu.pnu.myspring.annotations.MyService;

import java.util.List;

@MyService
public class PersonService {

    @MyAutowired
    private PersonRepository repository;

    public PersonService() {
    }

    public Person save(String name, int age) {
        return repository.save(name, age);
    }

    public Person findById(Long id) {
        Person person = repository.findById(id);
        if (person == null) {
            return Person.NullPerson;
        }
        return person;
    }

    public List<Person> findAll() {
        return repository.findAll();
    }

    public void delete(Long id) {
        repository.delete(id);
    }

}