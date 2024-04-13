package com.example.HungryNet.Security;

import com.example.HungryNet.DTO.Users;
import com.example.HungryNet.Model.Admin;

import com.example.HungryNet.Model.Client;
import com.example.HungryNet.Model.Manager;
import com.example.HungryNet.Repository.AdminRepository;
import com.example.HungryNet.Repository.ClientRepository;
import com.example.HungryNet.Repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private AdminRepository adminRepo;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ManagerRepository managerRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{

        List<Admin> adminList = adminRepo.findAll();
        List<Manager> managerList = managerRepository.findAll();
        List<Client> clientList = clientRepository.findAll();

        Set<Users> users = new HashSet<>();
        for(Admin a: adminList){
            Users users1 = new Users();
            users1.setId(a.getId());
            users1.setUsername(a.getUsername());
            users1.setPassword(a.getPassword());
            users1.setRole(a.getRole());
            users.add(users1);
        }

        for(Manager a: managerList){
            Users users1 = new Users();
            users1.setId(a.getId());
            users1.setUsername(a.getUsername());
            users1.setPassword(a.getPassword());
            users1.setRole(a.getRole());
            users.add(users1);
        }

        for(Client a: clientList){
            Users users1 = new Users();
            users1.setId(a.getId());
            users1.setUsername(a.getUsername());
            users1.setPassword(a.getPassword());
            users1.setRole(a.getRole());
            users.add(users1);
        }

        Users user2 = new Users();

        Boolean exist= false ;

        for(Users u : users){
            if(u.getUsername().equals(username)) {
                user2 = u;
                exist = true ;
                break ;
            }
        }

        if (!exist){
            throw  new UsernameNotFoundException("This user does not exist") ;
        }

        System.out.println("User" + user2);
//        Collection<SimpleGrantedAuthority> authorityCollections = new ArrayList<>();
//        authorityCollections.add(new SimpleGrantedAuthority(user2.getRole().name()));
        return UserDetailsImpl.build(user2);
    }
}
