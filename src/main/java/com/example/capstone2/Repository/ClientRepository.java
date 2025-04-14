package com.example.capstone2.Repository;

import com.example.capstone2.Model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

    Client findClientByClientId (Integer clientId);
}
