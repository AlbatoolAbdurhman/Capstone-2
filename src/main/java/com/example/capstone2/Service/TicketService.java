package com.example.capstone2.Service;

import com.example.capstone2.Model.Admin;
import com.example.capstone2.Model.SupportAgent;
import com.example.capstone2.Model.Ticket;
import com.example.capstone2.Model.TicketResponse;
import com.example.capstone2.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final EmployeeRepository employeeRepository;
    private final SupportAgentRepository supportAgentRepository;
    private final TicketResponseRepository ticketResponseRepository;
    private final AdminRepository adminRepository;

    public List<Ticket> getAllTickets() {

        return ticketRepository.findAll();
    }

    public String addTicket(Ticket ticket) {

        if (employeeRepository.findEmployeeByEmployeeId(ticket.getEmployeeId()) == null) {
            return "Clint not found";
        }

        if (supportAgentRepository.findSupportAgentByAgentId(ticket.getAgentId()) == null) {
            return "Support Agent not found";
        }

        ticketRepository.save(ticket);
        return "Ticket added successfully";
    }


    public String updateTicket(Integer ticketId, Ticket ticket) {

        Ticket ticket1 = ticketRepository.findTicketByTicketId(ticketId);
        if (ticket1 == null) {
            return "Ticket not found";
        }

        if (employeeRepository.findEmployeeByEmployeeId(ticket.getTicketId()) == null) {
            return "Employee not found";
        }
        if (supportAgentRepository.findSupportAgentByAgentId(ticket.getAgentId()) == null) {
            return "Support Agent not found";
        }
        ticket1.setTicketId(ticket.getTicketId());
        ticket1.setAgentId(ticket.getAgentId());
        ticket1.setEmployeeId(ticket.getEmployeeId());
        ticket1.setStatus(ticket.getStatus());
        ticket1.setPriority(ticket.getPriority());
        ticket1.setDescription(ticket.getDescription());
        ticket1.setSubject(ticket.getSubject());
        ticket1.setCreatedAt(LocalDateTime.now());
        ticketRepository.save(ticket);
        return "Update successfully";
    }


    public Boolean deleteTicket(Integer ticketId) {
        Ticket ticket = ticketRepository.findTicketByTicketId(ticketId);
        if (ticket == null) {
            return false;
        }
        ticketRepository.delete(ticket);
        return true;
    }


    public Integer getOpenTicketsCount() {
        return ticketRepository.countByStatus("Open");
    }

    public Integer getInProgressTicketsCount() {
        return ticketRepository.countByStatus("In_Progress");
    }

    public Integer getClosedTicketsCount() {
        return ticketRepository.countByStatus("Closed");
    }

    public List<Ticket> getTicketsRespondedByAgent(Integer adminId, Integer agentId) {
        if(adminRepository.findAdminsByAdminId(adminId)==null){
            return null;
        }
        SupportAgent agent = supportAgentRepository.findSupportAgentByAgentId(agentId);
        if (agent == null) {
            return null;
        }

        //جلب الردود اللي كتبها Agent معين
        List<TicketResponse> responses = ticketResponseRepository.findTicketResponseByAgentId(agentId);

        List<Ticket> tickets = new ArrayList<>();
        for (TicketResponse response : responses) {
//نأخذ من كل response رقم التذكرة ticketId
            Ticket ticket = ticketRepository.findTicketByTicketId(response.getTicketId());
            if (ticket != null) {
                tickets.add(ticket);
            }
        }
        return tickets;
    }

    public List<Ticket> getUnansweredTicketsLast48Hours(Integer agentId) {
        if(supportAgentRepository.findSupportAgentByAgentId(agentId)==null){
            return null;
        }
        List<Ticket> allTickets = ticketRepository.findAll();
        List<Ticket> result = new ArrayList<>();

        //للمقارنه متى اخر رد حصل
        LocalDateTime now = LocalDateTime.now();

        for (Ticket t : allTickets) {
            // نتأكد أن التذكرة مفتوحة فقط -1
            if (!t.getStatus().equals("OPEN")) {
                continue;
            } // تجاهل اذا كانت  close او"in progres

            //جلب الردود لهذي التذكره
            List<TicketResponse> r = ticketResponseRepository.findByTicketId(t.getTicketId());


            if (r.isEmpty()) {
                result.add(t);
            } else {

                TicketResponse lastResponse = null;
                //2-ابحقث عن اخر رد تم على التذكره
                //3-مقارنه التواريخ
                for (TicketResponse response : r) {
                    if (lastResponse == null || response.getResponseDate().isAfter(lastResponse.getResponseDate())) {
                        lastResponse = response; // خزنت احدث تاريخ للرد
                    }
                }
//4-احسب الفرق بين الان وتاريخ اخر رد
                if (lastResponse != null) {
                    Duration duration = Duration.between(lastResponse.getResponseDate(), now);
                    if (duration.toHours() > 48) {
                        result.add(t);
                    }
                }
            }
        }
        return result;

    }

    public String assignAgentToTicket(Integer ticketId, Integer agentId, Integer adminId) {

        // نتحقق ان التذكرة موجودة
        Ticket ticket = ticketRepository.findTicketByTicketId(ticketId);
        if (ticket == null) {
            return "Ticket not found";
        }

        // نتحقق إن Agent موجود
        SupportAgent agent = supportAgentRepository.findSupportAgentByAgentId(agentId);
        if (agent == null) {
            return "Support Agent not found";
        }

        // نتحقق ان Admin موجود
        Admin admin = adminRepository.findAdminsByAdminId(adminId);
        if (admin == null) {
            return "Admin not found";
        }
//كل الشروط محققه =عين التدكره Agent
        ticket.setAgentId(agentId);
        ticketRepository.save(ticket);

        return "Agent assigned to ticket successfully";
    }

    public String getTicketConversation(Integer agentId, Integer ticketId) {

        if(supportAgentRepository.findSupportAgentByAgentId(agentId)==null){
            return "Support Agent not found";
        }
        Ticket ticket = ticketRepository.findTicketByTicketId(ticketId);
        if (ticket == null) {
            return "Ticket not found";
        }

        // جلب ردود التذكرة
        List<TicketResponse> responses = ticketResponseRepository.findByTicketId(ticketId);
        if (responses.isEmpty()) {
            return "No responses found for this ticket";
        }

        //  تنسيق المحادثةبالشكل اللي اريده
        StringBuilder conversation = new StringBuilder();

        // اظهار التذكره اولا
        conversation.append("Ticket: \n");
        conversation.append("ID: ").append(ticket.getTicketId()).append("\n");
        conversation.append("Status: ").append(ticket.getStatus()).append("\n");
        conversation.append("Created At: ").append(ticket.getCreatedAt()).append("\n\n");

        // ثم الردود المتعلقه بالتذكره
        conversation.append("Responses: \n");
        for (TicketResponse response : responses) {
            conversation.append("Response ID: ").append(response.getResponseId()).append("\n");
            conversation.append("Response By Agent ID: ").append(response.getAgentId()).append("\n");
            conversation.append("Response Date: ").append(response.getResponseDate()).append("\n");
            conversation.append("Message: ").append(response.getMessage()).append("\n\n");
        }
        return conversation.toString();
    }

    public String deleteClosedTickets(Integer adminId, int days) {
        // التحقق من وجود Admin
        if (adminRepository.findAdminsByAdminId(adminId) == null) {
            return "Admin Not Found";
        }

        //  نحسب التاريخ اللي بناء عليه يتم الحذفع
        LocalDateTime date = LocalDateTime.now().minusDays(days);

        //جلب التذاكر القديمه
        List<Ticket> oldClosedTickets = ticketRepository.findClosedTicketsBefore(date);

        if (oldClosedTickets.isEmpty()) {
            return "No closed tickets older than " + days + " days";
        }

        ticketRepository.deleteAll(oldClosedTickets);

        return "Deleted closed tickets older than " + days + " days";

    }


    public List<Ticket> searchTickets(Integer agentId, String keyword)
    {
        if(supportAgentRepository.findSupportAgentByAgentId(agentId)==null){
            return null;
        }
        return ticketRepository.searchTicketsByKeyword(keyword);
    }

    public List<String> detectSpamTickets(Integer adminId) {
        //التحقق من وجود Admin
        if (adminRepository.findAdminsByAdminId(adminId) == null) {
            return null;
        }

        List<Ticket> allTickets = ticketRepository.findAll();
        List<String> spamList = new ArrayList<>();

        for (Ticket t : allTickets) {
            LocalDateTime thirtyMinutesAgo = t.getCreatedAt().minusMinutes(30);

            List<Ticket> recentTickets = ticketRepository.findByEmployeeIdAndCreatedAt(t.getEmployeeId(), thirtyMinutesAgo);

            for (Ticket recent : recentTickets) {
                if (!t.getTicketId().equals(recent.getTicketId())) {
                    if (t.getSubject().equalsIgnoreCase(recent.getSubject()) ||
                            t.getDescription().equalsIgnoreCase(recent.getDescription())) {

                        spamList.add("Employee ID " + t.getEmployeeId() + " sent duplicate ticket within 30 minutes.");
                        break;
                    }
                }
            }
        }
        return spamList;
    }

    public String upGradeOldTicketsWithoutResponse() {
        LocalDateTime date = LocalDateTime.now().minusDays(3);// حساب التاريخ الذي كان قبل 3 ايام
        List<Ticket> oldTickets = ticketRepository.findOldUnResponseTicket(date);

        //عد كم تذكره تم ترقرقيتها
        int upGCount = 0;

        for (Ticket ticket : oldTickets) {//نتحقق من اولويتها
            if (ticket.getPriority().equalsIgnoreCase("LOW")) {
                ticket.setPriority("HIGH");
                ticketRepository.save(ticket);
                upGCount++;
            }
        }

        return upGCount > 0 ? "upgraded" + upGCount + " tickets to HIGH priority." : " No tickets qualified for upgrade.";
    }

    public List<Ticket> getImportantTicketSince(Integer agentId, String priority, int daysAgo) {
        if (supportAgentRepository.findSupportAgentByAgentId(agentId) == null) {
            return null;

        }
        LocalDateTime fromDate = LocalDateTime.now().minusDays(daysAgo);
        return ticketRepository.filterTicketsByPriorityAndDate(priority.toUpperCase(), fromDate);
    }


    public String getMonthlyStats(Integer adminId) {
        if (adminRepository.findAdminsByAdminId(adminId) == null) {
            return "Admin Not Found";
        }
        Integer total = ticketRepository.countMonthlyTickets();
        Integer closed = ticketRepository.countMonthlyClosedTickets();
        Integer open = ticketRepository.countOpenTickets();

        return "This month's statistics:\n" +
                " Tickets added: " + total + "\n" +
                "closed: " + closed + "\n" +
                "open: " + open;
    }

    public List<String> checkAgentOverload(Integer adminId, int limit) {
        Admin admin = adminRepository.findAdminsByAdminId(adminId);
        if (admin == null) {
            return null;
        }

        List<Object[]> result = ticketRepository.countTicketsAssignedAgent();
        List<String> overloadedAgents = new ArrayList<>();

        for (Object[] row : result) {
            Integer agentId = (Integer) row[0];
            Long Count = (Long) row[1];
            int ticketCount = Count.intValue();

            if (ticketCount > limit) {
                overloadedAgents.add("Agent ID " + agentId + " has " + ticketCount + " tickets.");
            }
        }

        return overloadedAgents;
    }

    public List<Ticket> getTicketsByClientId(Integer clientId) {
        return ticketRepository.findByTicketId(clientId);

    }

    public List<Ticket>getTicketByAgentId(Integer adminId , Integer agentId) {
        if(adminRepository.findAdminsByAdminId(adminId)==null){
            return null;
        }
        if(supportAgentRepository.findSupportAgentByAgentId(agentId)==null){
            return null;
        }
        return ticketRepository.findByAgentId(agentId);

    }
}













