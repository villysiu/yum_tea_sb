package com.villysiu.yumtea.service.user;

import com.villysiu.yumtea.models.user.Account;
import com.villysiu.yumtea.models.user.Role;
import com.villysiu.yumtea.repo.user.RoleRepo;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepo roleRepo;

    public RoleService(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    public Role getRoleByName(String roleName){
        return roleRepo.findByName(roleName).orElseGet(
                ()->roleRepo.save(new Role(roleName))
        );
    }

    public boolean isAdmin(Account account) {
        Role adminRole = getRoleByName("ROLE_ADMIN");
        return account.getRoles().contains(adminRole);
    }

}
