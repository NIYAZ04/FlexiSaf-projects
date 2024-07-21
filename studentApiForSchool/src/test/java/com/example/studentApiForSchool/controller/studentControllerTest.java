package com.example.studentApiForSchool.controller;

import com.example.studentApiForSchool.model.Student;
import com.example.studentApiForSchool.service.StudentService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @Test
    void testGetAllStudents() throws Exception {
        // Arrange
        List<Student> students = Arrays.asList(new Student(1L, "John", 20), new Student(2L, "Jane", 22));
        when(studentService.getAllStudents()).thenReturn(students);

        // Act & Assert
        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", is("John")))
                .andExpect(jsonPath("$[1].name", is("Jane")));
    }

    @Test
    void testGetStudentById() throws Exception {
        // Arrange
        Student student = new Student(1L, "John", 20);
        when(studentService.getStudentById(1L)).thenReturn(student);

        // Act & Assert
        mockMvc.perform(get("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("John")));
    }

    @Test
    void testCreateStudent() throws Exception {
        // Arrange
        Student student = new Student(null, "John", 20);
        Student createdStudent = new Student(1L, "John", 20);
        when(studentService.createStudent(any(Student.class))).thenReturn(createdStudent);

        // Act & Assert
        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John\",\"age\":20}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("John")));
    }

    @Test
    void testUpdateStudent() throws Exception {
        // Arrange
        Student updatedStudent = new Student(1L, "John Doe", 21);
        when(studentService.updateStudent(eq(1L), any(Student.class))).thenReturn(updatedStudent);

        // Act & Assert
        mockMvc.perform(put("/api/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe\",\"age\":21}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("John Doe")));
    }

    @Test
    void testDeleteStudent() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/students/1"))
                .andExpect(status().isNoContent());

        verify(studentService, times(1)).deleteStudent(1L);
    }
}
