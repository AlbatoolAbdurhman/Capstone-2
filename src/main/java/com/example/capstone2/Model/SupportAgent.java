package com.example.capstone2.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SupportAgent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer agentId;

    @Column(columnDefinition = "varchar(10) not null")
    @NotEmpty(message = "name cannot be null")
    private String name;

    @Column(columnDefinition = "varchar(30) not null")
    @NotEmpty(message = "email cannot be null")
    @Email(message = "Invalid email")
    private String email;

    @Column(columnDefinition = "varchar(15) not null")
    @NotEmpty(message = "password cannot be null")
    private String password;

    private LocalDate hireDate;
}
