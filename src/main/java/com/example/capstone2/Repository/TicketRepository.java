package com.example.capstone2.Repository;

import com.example.capstone2.Model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {


    Ticket findTicketByTicketId(Integer ticketId);

    //Statistics of the number of tickets by status (open, under review, closed).
    Integer countByStatus(String status);

    //A report for each agent containing the number of responses, closed tickets, and average response time
    Integer countByStatusAndAgentId(String status, Integer agentId);

    List<Ticket> findByAgentId(Integer agentId);
    List<Ticket>findByTicketId(Integer ticketId);

    //Delete tickets closed more than X days ago (as determined by Admin)
    @Query("SELECT t FROM Ticket t WHERE t.status = 'CLOSED' AND t.createdAt < ?1")
    List<Ticket> findClosedTicketsBefore(LocalDateTime date);

    // Search tickets by keyword in the subject or description.
    @Query("SELECT t FROM Ticket t WHERE LOWER(t.subject) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Ticket> searchTicketsByKeyword(String keyword);

    //Getting repeated tickets from the same user within a short period (Spam Detection)
    List<Ticket> findByClientIdAndCreatedAtAfter(Integer clientId, LocalDateTime afterTime);

    //Upgrade ticket priority if it exceeds a certain number of days without a response
    @Query("SELECT t FROM Ticket t WHERE t.createdAt < ?1 AND (t.status = 'OPEN' OR t.status = 'IN_PROGRESS') AND t.ticketId NOT IN (SELECT tr.ticketId FROM TicketResponse tr)")
    List<Ticket> findOldUnResponseTicket(LocalDateTime dateTime);

    //Filter tickets by priority and date
    @Query("SELECT t FROM Ticket t WHERE t.priority = ?1 AND t.createdAt >= ?2 AND t.status ='OPEN'")
    List<Ticket> filterTicketsByPriorityAndDate(String priority, LocalDateTime fromDate);

//System performance statistics (number of tickets this month)
    @Query("SELECT COUNT(t) FROM Ticket t WHERE MONTH(t.createdAt) = MONTH(CURRENT_DATE) AND YEAR(t.createdAt) = YEAR(CURRENT_DATE)")
    Integer countMonthlyTickets();
    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.status = 'CLOSED' AND MONTH(t.createdAt) = MONTH(CURRENT_DATE) AND YEAR(t.createdAt) = YEAR(CURRENT_DATE)")
    Integer countMonthlyClosedTickets();
    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.status = 'OPEN'")
    Integer countOpenTickets();

    //Warning when tickets are overloaded for a specific agent
    @Query("SELECT t.agentId, COUNT(t) FROM Ticket t GROUP BY t.agentId")
    List<Object[]> countTicketsAssignedAgent();
}
