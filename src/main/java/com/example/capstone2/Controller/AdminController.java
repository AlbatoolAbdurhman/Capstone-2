package com.example.capstone2.Controller;

import com.example.capstone2.Model.Admin;
import com.example.capstone2.Service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping
    public ResponseEntity getAdmin() {

        List<Admin> admins = adminService.getAdmins();
        if(admins.isEmpty()) {
            return ResponseEntity.ok().body("Empty list");
        }
        return ResponseEntity.ok(admins);
    }

    @PostMapping
    public ResponseEntity addAdmin(@RequestBody @Valid Admin admin, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors.getFieldError().getDefaultMessage());
        }
        adminService.addAdmins(admin);
        return ResponseEntity.ok().body("Admin added successfully");
    }

    @PutMapping("/{adminId}")
    public ResponseEntity updateAdmin(@PathVariable Integer adminId, @RequestBody @Valid Admin admin, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors.getFieldError().getDefaultMessage());
        }

        Boolean updated = adminService.updateAdmin(adminId,admin);
        if (updated) {
            return ResponseEntity.ok().body("Admin updated successfully");
        }
        return ResponseEntity.badRequest().body("Admin not found or update failed");
    }

    @DeleteMapping("/delete/{adminId}")
    public ResponseEntity deleteAdmin(@PathVariable Integer adminId) {
        Boolean deleted = adminService.deleteAdmin(adminId);
        if (deleted) {
            return ResponseEntity.ok().body("Admin deleted successfully");
        }
        return ResponseEntity.badRequest().body("Admin not found or delete failed");
    }
}
