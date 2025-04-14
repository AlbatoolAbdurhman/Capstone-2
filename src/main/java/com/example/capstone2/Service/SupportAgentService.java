package com.example.capstone2.Service;

import com.example.capstone2.Model.Admin;
import com.example.capstone2.Model.SupportAgent;
import com.example.capstone2.Model.Ticket;
import com.example.capstone2.Model.TicketResponse;
import com.example.capstone2.Repository.AdminRepository;
import com.example.capstone2.Repository.SupportAgentRepository;
import com.example.capstone2.Repository.TicketRepository;
import com.example.capstone2.Repository.TicketResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SupportAgentService {


    private final SupportAgentRepository supportAgentRepository  ;
    private final AdminRepository adminRepository ;
    private final TicketResponseRepository ticketResponseRepository;
    private final TicketRepository ticketRepository;

    public List<SupportAgent> getAgent() {
        return supportAgentRepository.findAll();
    }

    public void addAgent(SupportAgent agent) {
        supportAgentRepository.save(agent);
    }

    public Boolean updateAgent(Integer agentId, SupportAgent agent) {

        SupportAgent u= supportAgentRepository.findSupportAgentByAgentId(agentId);
        if (u != null) {
            u.setAgentId(agent.getAgentId());
            u.setName(agent.getName());
            u.setHireDate(agent.getHireDate());
            u.setPassword(agent.getPassword());
            u.setEmail(agent.getEmail());
            supportAgentRepository.save(u);
            return true;
        }
        return false;
    }

    public Boolean deleteAgent(Integer agentId){
        SupportAgent d = supportAgentRepository.findSupportAgentByAgentId(agentId);
        if (d != null) {
            supportAgentRepository.delete(d);
                return true;
        }
        return false;
    }

    public String getAgentReport(Integer adminId, Integer agentId) {
//التحقق من وجود Admin
        if (adminRepository.findAdminsByAdminId(adminId) == null) {

                return "Admin Not Found";
            }
            //التحقق من وجود Agent
            SupportAgent agent = supportAgentRepository.findSupportAgentByAgentId(agentId);
            if (agent == null) {
                return "Support Agent Not Found";
            }
            //الحصول على عدد جميع ردود Agent l معين
            List<TicketResponse> r = ticketResponseRepository.findTicketResponseByAgentId(agentId);
            int resCount = r.size();

            //الحصول على التذاكر Closed
            Integer closedTicketsCount = ticketRepository.countByStatusAndAgentId("CLOSED", agentId);

            //حساب متوسط وقت الاستجابه
            double totalHours = 0;
            int count=0;
//مرور على كل resopnse وجلب التذكره المرتبطه بالرد
            for (TicketResponse response : r) {
                Ticket ticket = ticketRepository.findTicketByTicketId(response.getTicketId());
                if (ticket != null) {
                    LocalDateTime created = ticket.getCreatedAt();
                    LocalDateTime responseDate = response.getResponseDate();

                    // نحسب الفرق بالساعات
                    Duration duration = Duration.between(created, responseDate);
                    totalHours += duration.toHours();//عدد الساعات بين التاريخين
                    count++;//عدد الردود
                }
            }

            double averageResponseTime = 0.0;
            if (count > 0) {
                averageResponseTime = totalHours / count;
            }


            return "\nReport: "+"Agent Name: " + agent.getName()
                    + "\nTotal Responses: " + resCount
                    + "\nClosed Tickets: " + closedTicketsCount
                    + "\nAverage Response Time (Hours): " + averageResponseTime;
        }
        }












