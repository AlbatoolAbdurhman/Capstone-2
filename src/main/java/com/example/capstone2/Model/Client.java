package com.example.capstone2.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer clientId;

    @Column(columnDefinition = "varchar(30) not null")
    @NotEmpty(message = "email cannot be null")
    @Email(message = "Invalid email")
    private String email;

    @Column(columnDefinition = "varchar(15) not null unique")
    @NotEmpty(message = "username cannot be null")
    private String userName;

    @Column(columnDefinition = "varchar(15) not null")
    @NotEmpty(message = "password cannot be null")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*]).+$", message = "Password must contain at least one uppercase letter and one special character")
    private String password;

    @Column(columnDefinition = "date")
    private LocalDateTime registrationDate = LocalDateTime.now();


}
