package com.villysiu.yumtea.service.impl;

import com.villysiu.yumtea.models.user.Account;
import com.villysiu.yumtea.models.user.Role;
import com.villysiu.yumtea.repo.user.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class RoleService {

    private final RoleRepo roleRepo;

    public RoleService(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    public Role getRoleByName(String roleName){
        return roleRepo.findByName(roleName).orElse(null);
    }

    public boolean isAdmin(Account account) {
        Role adminRole = roleRepo.findByName("ROLE_ADMIN").get();
//        Role adminRole = roleRepo.findByName("ROLE_ADMIN").orElse(new Role("ROLE_ADMIN"));
        return account.getRoles().contains(adminRole);
    }

}
