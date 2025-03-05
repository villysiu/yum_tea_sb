package com.villysiu.yumtea.repo.tea;

import com.villysiu.yumtea.models.tea.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface SizeRepo extends JpaRepository<Size, Long> {
    Optional<Size> findByTitle(String title);
}
