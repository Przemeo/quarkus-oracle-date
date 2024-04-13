package com.przemekm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;

@Entity
@Table(name = "FIRST_ENTITY")
public class FirstEntity {

    @Id
    public Long id;

    @Column(name = "CREATE_DATE")
    public OffsetDateTime createDate;

    @Column(name = "CREATE_TIMESTAMP")
    public Timestamp createTimestamp;

    @Column(name = "CREATE_TIMESTAMP_INSTANT")
    public Instant createTimestampInstant;

    public FirstEntity() {}

    public FirstEntity(Long id,
                       OffsetDateTime createDate,
                       Timestamp createTimestamp,
                       Instant createTimestampInstant) {
        this.id = id;
        this.createDate = createDate;
        this.createTimestamp = createTimestamp;
        this.createTimestampInstant = createTimestampInstant;
    }

    public Long getId() {
        return id;
    }

    public OffsetDateTime getCreateDate() {
        return createDate;
    }

    public Timestamp getCreateTimestamp() {
        return createTimestamp;
    }

    public Instant getCreateTimestampInstant() {
        return createTimestampInstant;
    }

}