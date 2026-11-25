package com.scd.studentrecords;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    // Redirect root URL to /students
    @GetMapping("/")
    public String home() {
        return "redirect:/students";
    }

    // 1. GET /students (lists all or searches by ?name=)
    @GetMapping("/students")
    public String listStudents(@RequestParam(required = false) String name, Model model) {
        model.addAttribute("students", service.getStudents(name));
        model.addAttribute("searchName", name == null ? "" : name);
        return "students";
    }

    // 2. GET /students/{regNo} (shows one student or "not found" message)
    @GetMapping("/students/{regNo}")
    public String viewStudent(@PathVariable String regNo, Model model) {
        Student student = service.getStudentByRegNo(regNo);
        model.addAttribute("student", student);
        model.addAttribute("regNo", regNo);
        return "student-details";
    }

    // 3. GET /students/new (shows an empty form)
    @GetMapping("/students/new")
    public String showNewForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("isEdit", false);
        return "student-form";
    }

    // 4. POST /students (saves a new student)
    @PostMapping("/students")
    public String createStudent(
            @RequestParam String regNo,
            @RequestParam String name,
            @RequestParam String department,
            @RequestParam(required = false) Double cgpa,
            Model model) {

        Student s = new Student(0, regNo, name, department, cgpa);
        String error = service.addStudent(s);

        if (error != null) {
            model.addAttribute("error", error);
            model.addAttribute("student", s);
            model.addAttribute("isEdit", false);
            return "student-form";
        }
        return "redirect:/students";
    }

    // 5. GET /students/{regNo}/edit (shows form filled with student's data)
    @GetMapping("/students/{regNo}/edit")
    public String showEditForm(@PathVariable String regNo, Model model) {
        Student student = service.getStudentByRegNo(regNo);
        if (student == null) {
            return "redirect:/students";
        }
        model.addAttribute("student", student);
        model.addAttribute("isEdit", true);
        return "student-form";
    }

    // 6. POST /students/{regNo}/update (saves changes)
    @PostMapping("/students/{regNo}/update")
    public String updateStudent(
            @PathVariable String regNo,
            @RequestParam String name,
            @RequestParam String department,
            @RequestParam(required = false) Double cgpa,
            Model model) {

        Student s = new Student(0, regNo, name, department, cgpa);
        String error = service.updateStudent(regNo, s);

        if (error != null) {
            model.addAttribute("error", error);
            model.addAttribute("student", s);
            model.addAttribute("isEdit", true);
            return "student-form";
        }
        return "redirect:/students";
    }

    // 7. POST /students/{regNo}/delete (deletes student)
    @PostMapping("/students/{regNo}/delete")
    public String deleteStudent(@PathVariable String regNo) {
        service.deleteStudent(regNo);
        return "redirect:/students";
    }
}