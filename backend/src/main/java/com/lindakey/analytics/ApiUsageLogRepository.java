package com.lindakey.analytics;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiUsageLogRepository extends JpaRepository<ApiUsageLog, Long> {
}