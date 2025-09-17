package com.picsy.domain;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Representation of a participant (person or company) in the community.
 * The position of an agent inside community ordered lists determines its index in the evaluation matrix.
 */
public final class Agent {

    private final UUID id;
    private final String name;
    private final AgentType type;
    private final AgentStatus status;
    private final Instant createdAt;
    private final Map<String, String> metadata;

    private Agent(Builder builder) {
        this.id = Objects.requireNonNull(builder.id, "id");
        this.name = Objects.requireNonNull(builder.name, "name");
        this.type = Objects.requireNonNull(builder.type, "type");
        this.status = Objects.requireNonNullElse(builder.status, AgentStatus.ACTIVE);
        this.createdAt = Objects.requireNonNullElse(builder.createdAt, Instant.now());
        this.metadata = builder.metadata == null ? Map.of() : Collections.unmodifiableMap(builder.metadata);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public AgentType getType() {
        return type;
    }

    public AgentStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public Agent withStatus(AgentStatus newStatus) {
        return builder()
                .id(this.id)
                .name(this.name)
                .type(this.type)
                .status(newStatus)
                .createdAt(this.createdAt)
                .metadata(this.metadata)
                .build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private UUID id;
        private String name;
        private AgentType type;
        private AgentStatus status;
        private Instant createdAt;
        private Map<String, String> metadata;

        private Builder() {
        }

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder type(AgentType type) {
            this.type = type;
            return this;
        }

        public Builder status(AgentStatus status) {
            this.status = status;
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder metadata(Map<String, String> metadata) {
            this.metadata = metadata;
            return this;
        }

        public Agent build() {
            if (id == null) {
                id = UUID.randomUUID();
            }
            return new Agent(this);
        }
    }
}
