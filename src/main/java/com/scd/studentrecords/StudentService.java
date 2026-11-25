package com.scd.studentrecords;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    private final StudentRepository repo;

    public StudentService(StudentRepository repo) {
        this.repo = repo;
    }

    public List<Student> getStudents(String name) {
        if (name != null && !name.trim().isEmpty()) {
            return repo.searchByName(name.trim());
        }
        return repo.findAll();
    }

    public Student getStudentByRegNo(String regNo) {
        return repo.findByRegNo(regNo);
    }

    public String addStudent(Student s) {
        String error = validate(s, true);
        if (error != null) {
            return error;
        }
        s.setRegNo(s.getRegNo().trim());
        s.setName(s.getName().trim());
        s.setDepartment(s.getDepartment().trim());
        repo.insert(s);
        return null;
    }

    public String updateStudent(String regNo, Student s) {
        if (!repo.existsByRegNo(regNo)) {
            return "Student with Registration No " + regNo + " not found.";
        }
        s.setRegNo(regNo);
        String error = validate(s, false);
        if (error != null) {
            return error;
        }
        s.setName(s.getName().trim());
        s.setDepartment(s.getDepartment().trim());
        repo.update(s);
        return null;
    }

    public void deleteStudent(String regNo) {
        if (repo.existsByRegNo(regNo)) {
            repo.deleteByRegNo(regNo);
        }
    }

    private String validate(Student s, boolean isNew) {
        if (s.getRegNo() == null || s.getRegNo().trim().isEmpty()) {
            return "Registration number is required.";
        }
        if (isNew && repo.existsByRegNo(s.getRegNo().trim())) {
            return "A student with Registration No '" + s.getRegNo().trim() + "' already exists.";
        }
        if (s.getName() == null || s.getName().trim().isEmpty()) {
            return "Student name is required.";
        }
        if (s.getDepartment() == null || s.getDepartment().trim().isEmpty()) {
            return "Department is required.";
        }
        if (s.getCgpa() == null || s.getCgpa() < 0.0 || s.getCgpa() > 4.0) {
            return "CGPA must be between 0.00 and 4.00.";
        }
        return null;
    }
}