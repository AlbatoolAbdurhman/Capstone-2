package com.example.capstone2.Repository;

import com.example.capstone2.Model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Admin findAdminsByAdminId(Integer adminId);
}
