package com.rilvoo.erp.audit;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/** Repository for append-only activity log entries. */
public interface ActivityLogRepository extends JpaRepository<ActivityLogEntry, UUID> {
}
