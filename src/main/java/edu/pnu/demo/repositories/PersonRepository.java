package edu.pnu.demo.repositories;

import edu.pnu.demo.entities.Person;
import edu.pnu.myspring.annotations.MyRepository;
import edu.pnu.myspring.annotations.PostConstruct;
import edu.pnu.myspring.annotations.PreDestroy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@MyRepository
public class PersonRepository {
    private Map<Long, Person> dataStore = new HashMap<>();
    private AtomicLong counter = new AtomicLong(1);

    public PersonRepository() {

    }

    @PostConstruct
    public void init() {
        System.out.println("PersonRepository initialized!");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("PersonRepository is about to be destroyed!");
    }

    public Person save(String name, int age) {
        Person person = new Person(name, age);
        long id = counter.getAndIncrement();
        person.setId(id);
        dataStore.put(id, person);
        return person;
    }



    public Person findById(Long id) {
        return dataStore.get(id);
    }



    public List<Person> findAll() {
        return new ArrayList<>(dataStore.values());
    }



    public void delete(Long id) {
        dataStore.remove(id);
    }
}

