package edu.pnu.demo.controllers;

import edu.pnu.demo.entities.Student;
import edu.pnu.demo.services.StudentService;
import edu.pnu.myspring.annotations.*;

@MyRestController
public class StudentController {
    @MyAutowired
    private StudentService service;

    public StudentController() {
    }

    @PostMapping("/students")
    public String createStudent(@PathVariable("name") String name, @PathVariable("course") String course) {
        Student student = new Student(name, course);
        this.service.save(student);
        return "Student created with ID: " + student.getId();
    }

    @MyRequestMapping("/students/{id}/{replay_id}")
    public String getStudent(@PathVariable("id") Long id) {
        Student student = this.service.findById(id);
        return student != null ? student.toString() : "Student not found!";
    }
}
