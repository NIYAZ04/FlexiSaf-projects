package com.example.studentApiForSchool.repository;

import com.example.studentApiForSchool.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
