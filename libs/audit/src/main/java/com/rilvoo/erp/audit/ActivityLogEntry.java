package com.rilvoo.erp.audit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Append-only row in {@code audit.activity_log}. The table is protected by PostgreSQL triggers
 * (UPDATE/DELETE denied) — see database/audit migrations. No soft delete, no version column.
 */
@Entity
@Table(name = "activity_log", schema = "audit")
public class ActivityLogEntry {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "actor_user_id")
    private UUID actorUserId;

    @Column(name = "action", nullable = false, length = 120)
    private String action;

    @Column(name = "resource", nullable = false, length = 200)
    private String resource;

    @Column(name = "request_id", length = 64)
    private String requestId;

    @Column(name = "outcome", nullable = false, length = 16)
    private String outcome;

    @Column(name = "details", columnDefinition = "jsonb")
    private String detailsJson;

    @Column(name = "source", nullable = false, length = 64)
    private String source;

    @Column(name = "occurred_at", nullable = false)
    private OffsetDateTime occurredAt;

    protected ActivityLogEntry() {
    }

    public ActivityLogEntry(UUID actorUserId, String action, String resource, String requestId,
                            String outcome, String detailsJson, String source) {
        this.id = UUID.randomUUID();
        this.actorUserId = actorUserId;
        this.action = action;
        this.resource = resource;
        this.requestId = requestId;
        this.outcome = outcome;
        this.detailsJson = detailsJson;
        this.source = source;
    }

    @PrePersist
    void onPrePersist() {
        if (occurredAt == null) {
            occurredAt = OffsetDateTime.now();
        }
    }

    public UUID getId() { return id; }
    public UUID getActorUserId() { return actorUserId; }
    public String getAction() { return action; }
    public String getResource() { return resource; }
    public String getRequestId() { return requestId; }
    public String getOutcome() { return outcome; }
    public String getDetailsJson() { return detailsJson; }
    public String getSource() { return source; }
    public OffsetDateTime getOccurredAt() { return occurredAt; }
}
