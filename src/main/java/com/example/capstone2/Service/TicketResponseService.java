package com.example.capstone2.Service;

import com.example.capstone2.Model.TicketResponse;
import com.example.capstone2.Repository.SupportAgentRepository;
import com.example.capstone2.Repository.TicketRepository;
import com.example.capstone2.Repository.TicketResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketResponseService {


    private final TicketResponseRepository ticketResponseRepository;
    private final TicketRepository ticketRepository ;
    private final SupportAgentRepository supportAgentRepository ;

    public List<TicketResponse> getAllResponses() {

        return ticketResponseRepository.findAll();
    }

    public String addResponse(TicketResponse ticketResponse) {

        if (ticketRepository.findTicketByTicketId(ticketResponse.getTicketId())== null) {
            return "Ticket not found";
        }

        if (supportAgentRepository.findSupportAgentByAgentId(ticketResponse.getAgentId()) == null) {
            return "Agent Agent not found";
        }
        ticketResponseRepository.save(ticketResponse);
        return "Response added successfully";
    }


    public String updateResponse(Integer responseId, TicketResponse ticketResponse) {

     TicketResponse response1 =ticketResponseRepository.findTicketResponseByResponseId(responseId);
        if(response1 == null){
            return "response not found";
        }

        if (ticketRepository.findTicketByTicketId(ticketResponse.getTicketId())== null) {
            return "Ticket not found";
        }

        if (supportAgentRepository.findSupportAgentByAgentId(ticketResponse.getAgentId()) == null) {
            return "Agent Agent not found";
        }

        response1.setResponseId(ticketResponse.getResponseId());
        response1.setResponseDate(LocalDateTime.now());
        response1.setMessage(ticketResponse.getMessage());
        response1.setTicketId(ticketResponse.getTicketId());
        response1.setAgentId(ticketResponse.getAgentId());
        ticketResponseRepository.save(ticketResponse);
        return "Update successfully";
    }


    public Boolean deleteRes(Integer responseId){
        TicketResponse d =ticketResponseRepository.findTicketResponseByResponseId(responseId);
        if(d == null){
            return false;
        }
        ticketResponseRepository.delete(d);
        return true;
    }
}
