package com.example.capstone2.Repository;

import com.example.capstone2.Model.SupportAgent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupportAgentRepository extends JpaRepository<SupportAgent, Integer> {

    SupportAgent findSupportAgentByAgentId(Integer agentId);
}
