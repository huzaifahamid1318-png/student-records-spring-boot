# Student Records Management System

A full-stack CRUD web application built with **Spring Boot**, **Thymeleaf**, **Spring JDBC (`JdbcTemplate`)**, and **MySQL**.

## Features
- **View & Search:** Browse all students sorted alphabetically or filter by name (case-insensitive).
- **Student Profile:** View detailed academic records for any student by Registration Number.
- **Add & Update:** Register new students and edit existing details with server-side validation (CGPA range check & duplicate registration prevention).
- **Permanent Deletion:** Remove records permanently from MySQL.
- **SQL Injection Safe:** Uses parameterized `?` placeholders via `JdbcTemplate`.

## Tech Stack
- **Backend:** Java 17, Spring Boot, Spring Web, Spring JDBC
- **Frontend:** Thymeleaf, HTML5, CSS3
- **Database:** MySQL

## Database Setup
Run the following SQL script in MySQL before starting the application:

```sql
CREATE DATABASE IF NOT EXISTS scd_records;
USE scd_records;

CREATE TABLE IF NOT EXISTS students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    reg_no VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    department VARCHAR(50) NOT NULL,
    cgpa DECIMAL(3,2) NOT NULL
);