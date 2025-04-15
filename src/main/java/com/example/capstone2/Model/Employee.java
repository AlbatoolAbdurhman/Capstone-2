package com.example.capstone2.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer employeeId;

    @Column(columnDefinition = "varchar(30) not null")
    @NotEmpty(message = "email cannot be null")
    @Email(message = "Invalid email")
    private String email;

    @Column(columnDefinition = "varchar(15) not null unique")
    @NotEmpty(message = "username cannot be null")
    private String userName;

    @NotEmpty(message = "department can not be null")
    @Column(columnDefinition = "varchar(30) not null")
    @Pattern(regexp = "^(Information Technology|Human Resources|Financial|Support)$", message = "Department must be one of: Information Technology, Human Resources, Financial, Support")
    private String department;

    @Column(columnDefinition = "varchar(15) not null")
    @NotEmpty(message = "password cannot be null")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*]).+$", message = "Password must contain at least one uppercase letter and one special character")
    private String password;

    @Column(columnDefinition = "date")
    private LocalDateTime registrationDate = LocalDateTime.now();


}
