package com.example.capstone2.Controller;

import com.example.capstone2.Model.Employee;
import com.example.capstone2.Service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {


    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity getEmployee() {

        List<Employee> employees = employeeService.getEmployee();
        if(employees.isEmpty()) {
            return ResponseEntity.ok().body("Empty list");
        }
        return ResponseEntity.ok(employees);
    }

    @PostMapping
    public ResponseEntity addEmployee(@RequestBody @Valid Employee employee, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors.getFieldError().getDefaultMessage());
        }
        employeeService.addEmployee(employee);
        return ResponseEntity.ok().body("Employee added successfully");
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity updateEmployee(@PathVariable Integer employeeId, @RequestBody @Valid Employee employee, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors.getFieldError().getDefaultMessage());
        }

        Boolean updated = employeeService.updateEmployee( employeeId, employee);
        if (updated) {
            return ResponseEntity.ok().body("Employee updated successfully");
        }
        return ResponseEntity.badRequest().body("Employee not found or update failed");
    }

    @DeleteMapping("/delete/{employeeId}")
    public ResponseEntity deleteEmployee(@PathVariable Integer employeeId) {
        Boolean deleted = employeeService.deleteEmployee(employeeId);
        if (deleted) {
            return ResponseEntity.ok().body("Employee deleted successfully");
        }
        return ResponseEntity.badRequest().body("Employee not found or delete failed");
    }

}
