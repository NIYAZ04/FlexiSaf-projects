package com.example.studentApiForSchool.service;

import com.example.studentApiForSchool.model.Student;
import com.example.studentApiForSchool.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    void testGetAllStudents() {
        // Arrange
        Student student1 = new Student(1L, "John", 20);
        Student student2 = new Student(2L, "Jane", 22);
        when(studentRepository.findAll()).thenReturn(Arrays.asList(student1, student2));

        // Act
        List<Student> students = studentService.getAllStudents();

        // Assert
        assertNotNull(students);
        assertEquals(2, students.size());
        assertEquals("John", students.get(0).getName());
    }

    @Test
    void testCreateStudent() {
        // Arrange
        Student student = new Student(null, "John", 20);
        Student createdStudent = new Student(1L, "John", 20);
        when(studentRepository.save(student)).thenReturn(createdStudent);

        // Act
        Student result = studentService.createStudent(student);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetStudentById() {
        // Arrange
        Student student = new Student(1L, "John", 20);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        // Act
        Student result = studentService.getStudentById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("John", result.getName());
    }

    @Test
    void testGetStudentById_NotFound() {
        // Arrange
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> studentService.getStudentById(1L));
    }

    @Test
    void testUpdateStudent() {
        // Arrange
        Student existingStudent = new Student(1L, "John", 20);
        Student updatedStudent = new Student(1L, "John Doe", 21);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(existingStudent));
        when(studentRepository.save(existingStudent)).thenReturn(updatedStudent);

        // Act
        Student result = studentService.updateStudent(1L, updatedStudent);

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
    }

    @Test
    void testDeleteStudent() {
        // Act
        studentService.deleteStudent(1L);

        // Assert
        verify(studentRepository, times(1)).deleteById(1L);
    }
}
