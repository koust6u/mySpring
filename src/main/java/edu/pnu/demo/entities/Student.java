package edu.pnu.demo.entities;


public class Student {
    private Long id;
    private String name;
    private String course;
    private static long Counter = 0L;

    public Student(String name, String course) {
        this.id = ++Counter;
        this.name = name;
        this.course = course;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourse() {
        return this.course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String toString() {
        return "Student{id=" + this.id + ", name='" + this.name + "', course='" + this.course + "'}";
    }
}

