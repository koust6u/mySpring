package edu.pnu.demo.services;


import edu.pnu.demo.entities.Student;
import edu.pnu.demo.repositories.StudentRepository;
import edu.pnu.myspring.annotations.MyAutowired;
import edu.pnu.myspring.annotations.MyService;

import java.util.List;

@MyService
public class StudentService {
    @MyAutowired
    private StudentRepository repository;

    public StudentService() {
    }

    public Student save(Student student) {
        return this.repository.save(student);
    }

    public Student findById(Long id) {
        return this.repository.findById(id);
    }

    public List<Student> findAll() {
        return this.repository.findAll();
    }

    public void delete(Long id) {
        this.repository.delete(id);
    }
}
