package com.lindakey.applications;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    
    // This allows us to fetch all API applications owned by a specific developer
    List<Application> findByUserId(Long userId);
}