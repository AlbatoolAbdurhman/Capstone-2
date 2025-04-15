package com.example.capstone2.Service;

import com.example.capstone2.Model.Employee;
import com.example.capstone2.Repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {


    private final EmployeeRepository employeeRepository;

    public List<Employee> getEmployee() {
        return employeeRepository.findAll();
    }

    public void addEmployee(Employee employee) {
        employeeRepository.save(employee);
    }


    public Boolean updateEmployee(Integer employeeId, Employee employee) {
        Employee e= employeeRepository.findEmployeeByEmployeeId(employeeId);
        if (e != null) {
            e.setDepartment(employee.getDepartment());
            e.setEmployeeId(employee.getEmployeeId());
            e.setUserName(employee.getUserName());
            e.setPassword(employee.getPassword());
            e.setEmail(employee.getEmail());
            e.setRegistrationDate(LocalDateTime.now());
            employeeRepository.save(e);
            return true;
        }
        return false;
    }

    public Boolean deleteEmployee(Integer employeeId){
        Employee e = employeeRepository.findEmployeeByEmployeeId(employeeId);
        if (e != null) {
            employeeRepository.delete(e);
            return true;
        }
        return false;
    }
}
