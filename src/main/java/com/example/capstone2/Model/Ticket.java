package com.example.capstone2.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ticketId;

    @Size(max=50, message = "The subject cannot exceed 50 characters.")
    @NotEmpty(message = "subject cannot be empty")
    private String subject;

    @Size(max=150, message = "The description cannot exceed 150 characters.")
    @NotEmpty(message = "description cannot be empty")
    private String description;

    @Column(columnDefinition = "date")
    private LocalDateTime createdAt=LocalDateTime.now();

    @NotEmpty(message = "status cannot be empty")
    @Pattern(regexp = "^(OPEN|IN_PROGRESS|CLOSED)$", message = "Status must be either OPEN, IN_PROGRESS, or CLOSED")
    private String status;

    @Pattern(regexp = "^(LOW|MEDIUM|HIGH)$",
            message = "Priority must be either LOW, MEDIUM, or HIGH")
    @NotEmpty(message = "priority cannot be empty")
    private String priority;

    @NotNull(message = "clientId is required")
    private Integer clientId;

    @NotNull(message = "agentId is required")
    private Integer agentId;


}
