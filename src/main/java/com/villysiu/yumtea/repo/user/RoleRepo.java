package com.villysiu.yumtea.repo.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.villysiu.yumtea.models.user.Role;

import java.util.Optional;


public interface RoleRepo extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);

}
