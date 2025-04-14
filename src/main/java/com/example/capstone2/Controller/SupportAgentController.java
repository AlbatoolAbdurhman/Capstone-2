package com.example.capstone2.Controller;

import com.example.capstone2.Model.SupportAgent;
import com.example.capstone2.Service.SupportAgentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agents")
@RequiredArgsConstructor
public class SupportAgentController {


    private final SupportAgentService supportAgentService;

    @GetMapping
    public ResponseEntity getAgent() {

        List<SupportAgent> agentList = supportAgentService.getAgent();
        if(agentList.isEmpty()) {
            return ResponseEntity.ok().body("Empty list");
        }
        return ResponseEntity.ok(agentList);
    }

    @PostMapping
    public ResponseEntity addAgent(@RequestBody @Valid SupportAgent agent, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors.getFieldError().getDefaultMessage());
        }
        supportAgentService.addAgent(agent);
        return ResponseEntity.ok().body("Agent added successfully");
    }

    @PutMapping("/{agentId}")
    public ResponseEntity updateClient(@PathVariable Integer agentId, @RequestBody @Valid SupportAgent agent, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors.getFieldError().getDefaultMessage());
        }

        Boolean updated = supportAgentService.updateAgent(agentId,agent);
        if (updated) {
            return ResponseEntity.ok().body("Agent updated successfully");
        }
        return ResponseEntity.badRequest().body("Agent not found or update failed");
    }

    @DeleteMapping("/delete/{agentId}")
    public ResponseEntity deleteAgent(@PathVariable Integer agentId) {
        Boolean deleted = supportAgentService.deleteAgent(agentId);
        if (deleted) {
            return ResponseEntity.ok().body("Agent deleted successfully");
        }
        return ResponseEntity.badRequest().body("Agent not found or delete failed");
    }

    @GetMapping("/agent/{adminId}/{agentId}")
    public ResponseEntity getAgentReport(@PathVariable Integer adminId, @PathVariable Integer agentId) {

        return ResponseEntity.ok().body(supportAgentService.getAgentReport(adminId, agentId));
    }

}

