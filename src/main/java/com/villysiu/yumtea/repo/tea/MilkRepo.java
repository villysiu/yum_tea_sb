package com.villysiu.yumtea.repo.tea;

import com.villysiu.yumtea.models.tea.Milk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface MilkRepo extends JpaRepository<Milk, Long> {
    Optional<Milk> findMilkByTitle(String title);


}
