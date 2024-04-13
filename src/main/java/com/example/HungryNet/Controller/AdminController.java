package com.example.HungryNet.Controller;

import com.example.HungryNet.Enumerator.Role;
import com.example.HungryNet.Model.Admin;
import com.example.HungryNet.Repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasAnyAuthority('ADMIN')")
public class AdminController {
    @Autowired
    AdminRepository adminRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping
    public List<Admin> getAdmin(){
        return adminRepo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Admin> getAdminById(@PathVariable Integer id){
        Optional<Admin> admin = adminRepo.findById(id);
        return new ResponseEntity<>(admin.get(),HttpStatus.OK);
    }

    @GetMapping("/role")
    public ResponseEntity<?> getAdminByRole(@RequestParam("role") Role role){
        return new ResponseEntity<>(adminRepo.findAllByRole(), HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<Admin> createAdmin(@RequestBody Admin admin){
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        return new ResponseEntity<>(adminRepo.save(admin), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Admin> updateAdmin(@PathVariable Integer id, @RequestBody Admin admin){
        Optional<Admin> oldAdmin = adminRepo.findById(id);
        Admin updatedAmin=oldAdmin.get();
        updatedAmin.setUsername(admin.getUsername());
        updatedAmin.setPassword(admin.getPassword());
        updatedAmin.setRole(admin.getRole());
        return new ResponseEntity<>((createAdmin(updatedAmin).getBody()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteAdmin(@PathVariable Integer id){
        adminRepo.deleteById(id);
    }
}
