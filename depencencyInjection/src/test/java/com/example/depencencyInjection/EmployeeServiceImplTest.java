package com.example.dependencyInjection.service.impl;

import com.example.dependencyInjection.model.Employee;
import com.example.dependencyInjection.repository.EmployeeRepository;
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
public class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    void testGetAllEmployees() {
        // Arrange
        Employee emp1 = new Employee(1L, "Alice", "Developer", 60000);
        Employee emp2 = new Employee(2L, "Bob", "Manager", 80000);
        when(employeeRepository.findAll()).thenReturn(Arrays.asList(emp1, emp2));

        // Act
        List<Employee> employees = employeeService.getAllEmployees();

        // Assert
        assertNotNull(employees);
        assertEquals(2, employees.size());
        assertEquals("Alice", employees.get(0).getName());
    }

    @Test
    void testGetEmployeeById() {
        // Arrange
        Employee employee = new Employee(1L, "Alice", "Developer", 60000);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        // Act
        Employee result = employeeService.getEmployeeById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Alice", result.getName());
    }

    @Test
    void testGetEmployeeById_NotFound() {
        // Arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Employee result = employeeService.getEmployeeById(1L);

        // Assert
        assertNull(result);
    }

    @Test
    void testCreateEmployee() {
        // Arrange
        Employee employee = new Employee(null, "Alice", "Developer", 60000);
        Employee createdEmployee = new Employee(1L, "Alice", "Developer", 60000);
        when(employeeRepository.save(employee)).thenReturn(createdEmployee);

        // Act
        Employee result = employeeService.createEmployee(employee);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testUpdateEmployee() {
        // Arrange
        Employee existingEmployee = new Employee(1L, "Alice", "Developer", 60000);
        Employee updatedEmployee = new Employee(1L, "Alice", "Senior Developer", 70000);
        when(employeeRepository.existsById(1L)).thenReturn(true);
        when(employeeRepository.save(existingEmployee)).thenReturn(updatedEmployee);

        // Act
        Employee result = employeeService.updateEmployee(1L, updatedEmployee);

        // Assert
        assertNotNull(result);
        assertEquals("Senior Developer", result.getPosition());
    }

    @Test
    void testUpdateEmployee_NotFound() {
        // Arrange
        Employee updatedEmployee = new Employee(1L, "Alice", "Senior Developer", 70000);
        when(employeeRepository.existsById(1L)).thenReturn(false);

        // Act
        Employee result = employeeService.updateEmployee(1L, updatedEmployee);

        // Assert
        assertNull(result);
    }

    @Test
    void testDeleteEmployee() {
        // Act
        employeeService.deleteEmployee(1L);

        // Assert
        verify(employeeRepository, times(1)).deleteById(1L);
    }
}
