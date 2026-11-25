package com.scd.studentrecords;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class StudentRepository {

    private final JdbcTemplate jdbc;

    // Spring creates the JdbcTemplate from application.properties
    public StudentRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // Turns ONE row of the result into ONE Student object
    private final RowMapper<Student> studentRowMapper = (rs, rowNum) -> new Student(
            rs.getInt("id"),
            rs.getString("reg_no"),
            rs.getString("name"),
            rs.getString("department"),
            rs.getDouble("cgpa")
    );

    // EXAMPLE: complete
    public List<Student> findAll() {
        return jdbc.query("SELECT * FROM students ORDER BY name", studentRowMapper);
    }

    // Names that CONTAIN the text, ignoring upper/lower case
    public List<Student> searchByName(String name) {
        String sql = "SELECT * FROM students WHERE LOWER(name) LIKE LOWER(?) ORDER BY name";
        return jdbc.query(sql, studentRowMapper, "%" + name + "%");
    }

    // The student with this reg no, or null if there is none
    public Student findByRegNo(String regNo) {
        String sql = "SELECT * FROM students WHERE reg_no = ?";
        List<Student> list = jdbc.query(sql, studentRowMapper, regNo);
        return list.isEmpty() ? null : list.get(0);
    }

    // True if a student with this reg no already exists
    public boolean existsByRegNo(String regNo) {
        String sql = "SELECT COUNT(*) FROM students WHERE reg_no = ?";
        Integer count = jdbc.queryForObject(sql, Integer.class, regNo);
        return count != null && count > 0;
    }

    // Insert a new row; return the number of rows inserted
    public int insert(Student s) {
        String sql = "INSERT INTO students (reg_no, name, department, cgpa) VALUES (?, ?, ?, ?)";
        return jdbc.update(sql, s.getRegNo(), s.getName(), s.getDepartment(), s.getCgpa());
    }

    // Change name, department and cgpa of the row with s.getRegNo()
    public int update(Student s) {
        String sql = "UPDATE students SET name = ?, department = ?, cgpa = ? WHERE reg_no = ?";
        return jdbc.update(sql, s.getName(), s.getDepartment(), s.getCgpa(), s.getRegNo());
    }

    // Delete the row with this reg no
    public int deleteByRegNo(String regNo) {
        String sql = "DELETE FROM students WHERE reg_no = ?";
        return jdbc.update(sql, regNo);
    }
}