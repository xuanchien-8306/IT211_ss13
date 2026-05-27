package com.example.demo.service;

import com.example.demo.exception.EmployeeNotFoundException;
import com.example.demo.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeServiceTest {

    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        employeeService = new EmployeeService();
    }

    @Test
    void getAllEmployees_ReturnList() {
        List<Employee> employees = employeeService.getAllEmployees();

        assertNotNull(employees);
        assertFalse(employees.isEmpty());
        assertEquals(5, employees.size());
    }

    @Test
    void getById_Found() {
        Employee employee = employeeService.getEmployeeById(1L);

        assertNotNull(employee);
        assertEquals(1L, employee.getId());
        assertEquals("Nguyen Van A", employee.getFullName());
        assertEquals("Engineering", employee.getDepartment());
        assertEquals(15000000.0, employee.getSalary());
    }

    @Test
    void getById_NotFound_ThrowException() {
        assertThrows(
                EmployeeNotFoundException.class,
                () -> employeeService.getEmployeeById(999L)
        );
    }

    @Test
    void addEmployee_Success() {
        Employee request = new Employee(
                null,
                "Nguyen Van F",
                "Finance",
                20000000.0
        );

        Employee createdEmployee = employeeService.addEmployee(request);

        assertNotNull(createdEmployee);
        assertNotNull(createdEmployee.getId());
        assertEquals(6L, createdEmployee.getId());
        assertEquals("Nguyen Van F", createdEmployee.getFullName());
        assertEquals("Finance", createdEmployee.getDepartment());
        assertEquals(20000000.0, createdEmployee.getSalary());
        assertEquals(6, employeeService.getAllEmployees().size());
    }

    @Test
    void deleteEmployee_RemovesCorrectElement() {
        employeeService.deleteEmployee(1L);

        assertEquals(4, employeeService.getAllEmployees().size());

        assertThrows(
                EmployeeNotFoundException.class,
                () -> employeeService.getEmployeeById(1L)
        );
    }
}