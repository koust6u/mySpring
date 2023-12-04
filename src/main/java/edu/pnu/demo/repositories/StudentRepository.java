package edu.pnu.demo.repositories;


import edu.pnu.demo.entities.Student;
import edu.pnu.myspring.annotations.MyRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@MyRepository
public class StudentRepository {
    private Map<Long, Student> dataStore = new HashMap();
    private AtomicLong counter = new AtomicLong(1L);

    public StudentRepository() {
    }

    public Student save(Student student) {
        long id = this.counter.getAndIncrement();
        student.setId(id);
        this.dataStore.put(id, student);
        return student;
    }

    public Student findById(Long id) {
        return (Student)this.dataStore.get(id);
    }

    public List<Student> findAll() {
        return new ArrayList(this.dataStore.values());
    }

    public void delete(Long id) {
        this.dataStore.remove(id);
    }
}

