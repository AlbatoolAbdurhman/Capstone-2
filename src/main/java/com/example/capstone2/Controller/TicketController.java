package com.example.capstone2.Controller;

import com.example.capstone2.Model.Ticket;
import com.example.capstone2.Service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ticket")
@RequiredArgsConstructor
public class TicketController {


    private final TicketService ticketService;

    @GetMapping
    public ResponseEntity getTickets() {
        if (ticketService.getAllTickets().isEmpty()) {
            return ResponseEntity.ok().body("Empty List");
        }
        return ResponseEntity.ok().body(ticketService.getAllTickets());

    }

    @PostMapping
    public ResponseEntity addTicket(@RequestBody @Valid Ticket ticket, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors.getFieldError().getDefaultMessage());
        }
        return ResponseEntity.ok().body(ticketService.addTicket(ticket));
    }

    @PutMapping("/{ticketId}")
    public ResponseEntity updateTicket(@PathVariable Integer ticketId, @RequestBody Ticket ticket, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors.getFieldError().getDefaultMessage());
        }
        return ResponseEntity.ok().body(ticketService.updateTicket(ticketId, ticket));
    }


    @DeleteMapping("/delete/{ticketId}")
    public ResponseEntity deleteTicket(@PathVariable Integer ticketId) {
        Boolean deleted = ticketService.deleteTicket(ticketId);
        if (deleted) {
            return ResponseEntity.ok().body("Ticket deleted successfully");
        }
        return ResponseEntity.badRequest().body("Ticket not found or field deleted ");
    }


    @GetMapping("/status-statistics")
    public ResponseEntity getStatusStatistics() {
        Integer open = ticketService.getOpenTicketsCount();
        Integer inProgress = ticketService.getInProgressTicketsCount();
        Integer closed = ticketService.getClosedTicketsCount();

        String statistics = "Open: " + open + ", In Progress: " + inProgress + ", Closed: " + closed;

        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/{adminId}/responded-tickets/{agentId}")
    public ResponseEntity getTicketsByAgentResponse(@PathVariable Integer adminId, @PathVariable Integer agentId) {
        List<Ticket> tickets = ticketService.getTicketsRespondedByAgent(adminId,agentId);

        if (tickets == null) {
            return ResponseEntity.status(404).body("Admin or Support Agent not found");
        }
        if (tickets.isEmpty()) {
            return ResponseEntity.ok().body("No Response for this Agent");
        }
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/{agentId}/unanswered-48h")
    public ResponseEntity getUnansweredTicketsLast48Hours(@PathVariable Integer agentId) {
        List<Ticket> tickets = ticketService.getUnansweredTicketsLast48Hours(agentId);

        if(tickets==null){
            return ResponseEntity.badRequest().body("Agent Not Found");}
        if (tickets.isEmpty()) {
            return ResponseEntity.status(200).body("There is no ticket that has not been responded to within 48 hours.");
        }

        return ResponseEntity.ok(tickets);
    }

    @PutMapping("/assign-agent/{ticketId}/{agentId}/{adminId}")
    public ResponseEntity assignAgentToTicket(@PathVariable Integer ticketId, @PathVariable Integer agentId, @PathVariable Integer adminId) {

        return ResponseEntity.status(200).body(ticketService.assignAgentToTicket(ticketId, agentId, adminId));
    }

    @GetMapping("/{agentId}/conversation/{ticketId}")
    public ResponseEntity getTicketConversation(@PathVariable Integer agentId, @PathVariable Integer ticketId) {

        return ResponseEntity.status(200).body(ticketService.getTicketConversation(agentId,ticketId));
    }

    @DeleteMapping("/deleteClosedTicket/{adminId}/{days}")
    public ResponseEntity deleteClosedTicket(@PathVariable Integer adminId, @PathVariable int days) {
        return ResponseEntity.ok().body(ticketService.deleteClosedTickets(adminId, days));
    }

    @GetMapping("/{agentId}/search")
    public ResponseEntity searchTickets(@PathVariable Integer agentId,@RequestParam String keyword) {
        List<Ticket> re = ticketService.searchTickets(agentId,keyword);
        if(re==null){
            return ResponseEntity.badRequest().body("Agent Not found");}
        if (re.isEmpty()) {
            return ResponseEntity.ok().body("No tickets found with  " + keyword + " keyword");
        }
        return ResponseEntity.ok().body(re);
    }

    @GetMapping("/spam-detection/{adminId}")
    public ResponseEntity detectSpam(@PathVariable Integer adminId) {
        List<String> result = ticketService.detectSpamTickets(adminId);
        if (result.isEmpty()) {
            return ResponseEntity.ok().body("No spam tickets found or Admin not found");
        }
        return ResponseEntity.ok(result);
    }

    @PutMapping("/upgrade-old")
    public ResponseEntity upgradeOldTickets() {
        return ResponseEntity.ok(ticketService.upGradeOldTicketsWithoutResponse());
    }

    @GetMapping("/filter/{agentId}")
    public ResponseEntity filterTickets(@PathVariable Integer agentId, @RequestParam String priority, @RequestParam int daysAgo) {

        List<Ticket> tickets = ticketService.getImportantTicketSince(agentId, priority, daysAgo);

        if(tickets.isEmpty()){
            return ResponseEntity.status(404).body("No tickets matching the data");
        }
        if (tickets == null) {
            return ResponseEntity.badRequest().body("Agent not found");
        }

        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/monthly-stats/{adminId}")
    public ResponseEntity getMonthlyStats(@PathVariable Integer adminId) {
        return ResponseEntity.ok(ticketService.getMonthlyStats(adminId));
    }

    @GetMapping("/agent-overload/{adminId}")
    public ResponseEntity getAgentOverload(@PathVariable Integer adminId , @RequestParam int limit) {
        List<String> warnings = ticketService.checkAgentOverload(adminId,limit);
        if (warnings==null){
            return ResponseEntity.badRequest().body("Admin not found");
        }
        if (warnings.isEmpty()) {
            return ResponseEntity.ok((" No agents are overloaded"));
        }

        return ResponseEntity.ok(warnings);
    }

    @GetMapping("/my-tickets/{clientId}")
    public ResponseEntity getClientTickets(@PathVariable Integer clientId) {
        List<Ticket> tickets = ticketService.getTicketsByClientId(clientId);
            if(tickets.isEmpty()) {
                return ResponseEntity.ok().body("No Tickets found for "+clientId);
            }
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/agent-tickets/{adminId}/{agentId}")
    public ResponseEntity getTicketByAgent(@PathVariable Integer adminId,@PathVariable Integer agentId) {
        List<Ticket> tickets = ticketService.getTicketByAgentId(adminId,agentId);
        if(tickets==null) {
            return ResponseEntity.badRequest().body("Admin or Agent not found");
        }
        if(tickets.isEmpty()) {
            return ResponseEntity.ok().body("No Tickets found for "+agentId);
        }
        return ResponseEntity.ok(tickets);
    }
}






