package com.example.capstone2.Controller;

import com.example.capstone2.Model.TicketResponse;
import com.example.capstone2.Service.TicketResponseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/response")
@RequiredArgsConstructor
public class TicketResponseController {

    private final TicketResponseService ticketResponseService;

    @GetMapping
    public ResponseEntity getRes(){
        if(ticketResponseService.getAllResponses().isEmpty()){
            return ResponseEntity.ok().body("Empty List");
        }
        return ResponseEntity.ok().body(ticketResponseService.getAllResponses());

    }

    @PostMapping
    public ResponseEntity addRes(@RequestBody @Valid TicketResponse response, Errors errors){
        if(errors.hasErrors()){
            return ResponseEntity.badRequest().body(errors.getFieldError().getDefaultMessage());
        }

        return ResponseEntity.ok().body(ticketResponseService.addResponse(response));
    }

    @PutMapping("/{responseId}")
    public ResponseEntity updateRes(@PathVariable Integer responseId, @RequestBody TicketResponse response, Errors errors){
        if(errors.hasErrors()){
            return ResponseEntity.badRequest().body(errors.getFieldError().getDefaultMessage());
        }
        return ResponseEntity.ok().body(ticketResponseService.updateResponse(responseId, response));
    }


    @DeleteMapping("/delete/{responseId}")
    public ResponseEntity deleteRes(@PathVariable Integer responseId){
        Boolean deleted=ticketResponseService.deleteRes(responseId);
        if(deleted){
            return ResponseEntity.ok().body("Ticket Response deleted successfully");
        }
        return ResponseEntity.badRequest().body("Ticket Response not found or field deleted ");
    }
}
