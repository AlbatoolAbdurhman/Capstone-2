package com.example.capstone2.Repository;

import com.example.capstone2.Model.TicketResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketResponseRepository extends JpaRepository<TicketResponse, Integer> {

    TicketResponse findTicketResponseByResponseId(Integer responseId);

    //All tickets responded to by a specific Agent.
    List<TicketResponse> findTicketResponseByAgentId(Integer agentId);

    //Tickets that have not been answered within the last 48 hours.
    List<TicketResponse> findByTicketId(Integer ticketId);

}
