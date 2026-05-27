package com.example.demo.controller;

import com.example.demo.exception.EmployeeNotFoundException;
import com.example.demo.model.Employee;
import com.example.demo.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllEmployees_ReturnHttp200AndJsonList() throws Exception {
        List<Employee> employees = List.of(
                new Employee(1L, "Nguyen Van A", "Engineering", 15000000.0),
                new Employee(2L, "Tran Thi B", "Human Resources", 12000000.0)
        );

        Mockito.when(employeeService.getAllEmployees()).thenReturn(employees);

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].fullName").value("Nguyen Van A"))
                .andExpect(jsonPath("$[0].department").value("Engineering"))
                .andExpect(jsonPath("$[0].salary").value(15000000.0))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].fullName").value("Tran Thi B"));
    }

    @Test
    void getEmployeeById_Found_ReturnHttp200() throws Exception {
        Employee employee = new Employee(
                1L,
                "Nguyen Van A",
                "Engineering",
                15000000.0
        );

        Mockito.when(employeeService.getEmployeeById(1L)).thenReturn(employee);

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fullName").value("Nguyen Van A"))
                .andExpect(jsonPath("$.department").value("Engineering"))
                .andExpect(jsonPath("$.salary").value(15000000.0));
    }

    @Test
    void getEmployeeById_NotFound_ReturnHttp404() throws Exception {
        Mockito.when(employeeService.getEmployeeById(999L))
                .thenThrow(new EmployeeNotFoundException(999L));

        mockMvc.perform(get("/api/employees/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Employee not found with id: 999"));
    }

    @Test
    void addEmployee_Success_ReturnHttp201() throws Exception {
        Employee request = new Employee(
                null,
                "Nguyen Van F",
                "Finance",
                20000000.0
        );

        Employee response = new Employee(
                6L,
                "Nguyen Van F",
                "Finance",
                20000000.0
        );

        Mockito.when(employeeService.addEmployee(any(Employee.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(6))
                .andExpect(jsonPath("$.fullName").value("Nguyen Van F"))
                .andExpect(jsonPath("$.department").value("Finance"))
                .andExpect(jsonPath("$.salary").value(20000000.0));
    }
}