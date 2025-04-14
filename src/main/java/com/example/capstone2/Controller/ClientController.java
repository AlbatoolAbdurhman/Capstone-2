package com.example.capstone2.Controller;

import com.example.capstone2.Model.Client;
import com.example.capstone2.Service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {


    private final ClientService clientService ;

    @GetMapping
    public ResponseEntity getClients() {

        List<Client> clients = clientService.getClients();
        if(clients.isEmpty()) {
            return ResponseEntity.ok().body("Empty list");
        }
        return ResponseEntity.ok(clients);
    }

    @PostMapping
    public ResponseEntity addClient(@RequestBody @Valid Client client, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors.getFieldError().getDefaultMessage());
        }
        clientService.addClient(client);
        return ResponseEntity.ok().body("User added successfully");
    }

    @PutMapping("/{clientId}")
    public ResponseEntity updateClient(@PathVariable Integer clientId, @RequestBody @Valid Client client, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors.getFieldError().getDefaultMessage());
        }

        Boolean updated = clientService.updateClient( clientId, client);
        if (updated) {
            return ResponseEntity.ok().body("Client updated successfully");
        }
        return ResponseEntity.badRequest().body("Client not found or update failed");
    }

    @DeleteMapping("/delete/{clientId}")
    public ResponseEntity deleteClient(@PathVariable Integer clientId) {
        Boolean deleted = clientService.deleteClient(clientId);
        if (deleted) {
            return ResponseEntity.ok().body("Client deleted successfully");
        }
        return ResponseEntity.badRequest().body("Client not found or delete failed");
    }

}
