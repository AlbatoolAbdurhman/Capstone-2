package com.example.capstone2.Service;

import com.example.capstone2.Model.Admin;
import com.example.capstone2.Repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {


    private final AdminRepository adminRepository  ;

    public List<Admin> getAdmins() {
        return adminRepository.findAll();
    }

    public void addAdmins(Admin admin) {
        adminRepository.save(admin);
    }

    public Boolean updateAdmin(Integer adminId, Admin admin) {
        Admin a= adminRepository.findAdminsByAdminId(adminId);
        if (a != null) {
            a.setAdminId(admin.getAdminId());
            a.setName(admin.getName());
            a.setCreatedAt(LocalDateTime.now());
            a.setPassword(admin.getPassword());
            a.setEmail(admin.getEmail());
            adminRepository.save(a);
            return true;
        }
        return false;
    }

    public Boolean deleteAdmin(Integer adminId){
        Admin a= adminRepository.findAdminsByAdminId(adminId);
        if (a != null) {
            adminRepository.delete(a);
            return true;
        }
        return false;
    }
}
