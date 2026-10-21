package com.rilvoo.foundation.persistence;

import java.time.Instant;
import java.util.UUID;

/**
 * Optional mapped-superclass contract for future JPA-backed applications built on this
 * foundation. Kept annotation-free in the core module so the template never requires a JPA
 * provider at runtime: concrete applications extend an application-local class that combines
 * this state with {@code @MappedSuperclass}/{@code jakarta.persistence} annotations, or copy
 * the fields directly into their entities. No entity, repository or DataSource is required
 * for the foundation to start.
 */
public abstract class AuditableAggregateRoot {

    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    private UUID createdBy;
    private UUID updatedBy;
    private Instant deletedAt;
    private long version;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }

    public UUID getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(UUID updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public boolean isSoftDeleted() {
        return deletedAt != null;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}
