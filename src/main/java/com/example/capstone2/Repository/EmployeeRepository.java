package com.example.capstone2.Repository;

import com.example.capstone2.Model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    Employee findEmployeeByEmployeeId (Integer employeeId);
}
