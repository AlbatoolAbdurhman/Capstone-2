package com.example.capstone2.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TicketResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer responseId;

    @NotEmpty(message = "message cannot be empty")
    private String message;

    @Column(columnDefinition = "date")
    private LocalDateTime responseDate=LocalDateTime.now();

    @NotNull(message = "ticketId is required")
    private Integer ticketId;

    @NotNull(message = "agentId is required")
    private Integer agentId;


}
