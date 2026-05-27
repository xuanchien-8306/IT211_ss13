package com.example.demo.service;

import com.example.demo.exception.EmployeeNotFoundException;
import com.example.demo.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class EmployeeService {

    private static final Logger logger =
            LoggerFactory.getLogger(EmployeeService.class);

    private final List<Employee> employees = new ArrayList<>();

    private final AtomicLong idGenerator = new AtomicLong(5);

    public EmployeeService() {
        employees.add(new Employee(1L, "Nguyen Van A", "Engineering", 15000000.0));
        employees.add(new Employee(2L, "Tran Thi B", "Human Resources", 12000000.0));
        employees.add(new Employee(3L, "Le Van C", "Finance", 13500000.0));
        employees.add(new Employee(4L, "Pham Thi D", "Marketing", 11000000.0));
        employees.add(new Employee(5L, "Hoang Van E", "Engineering", 18000000.0));
    }

    public List<Employee> getAllEmployees() {
        return employees;
    }

    public Employee getEmployeeById(Long id) {
        return employees.stream()
                .filter(employee -> employee.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> {
                    logger.warn("Employee not found with id: {}", id);
                    return new EmployeeNotFoundException(id);
                });
    }

    public Employee addEmployee(Employee employee) {
        Long newId = idGenerator.incrementAndGet();

        Employee newEmployee = new Employee(
                newId,
                employee.getFullName(),
                employee.getDepartment(),
                employee.getSalary()
        );

        employees.add(newEmployee);

        return newEmployee;
    }

    public Employee updateEmployee(Long id, Employee request) {
        Employee existingEmployee = getEmployeeById(id);

        existingEmployee.setFullName(request.getFullName());
        existingEmployee.setDepartment(request.getDepartment());
        existingEmployee.setSalary(request.getSalary());

        return existingEmployee;
    }

    public void deleteEmployee(Long id) {
        Employee employee = getEmployeeById(id);
        employees.remove(employee);
    }
}