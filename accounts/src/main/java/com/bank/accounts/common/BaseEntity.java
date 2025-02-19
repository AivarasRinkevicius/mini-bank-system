package com.bank.accounts.common;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;

@Entity
@Audited
@Getter
@SuperBuilder
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class BaseEntity {
    @Setter
    public String lastModifiedBy;
    @Id
    @GeneratedValue
    private Long id;
    @Version
    private int versionNum;
    @Setter
    private String createdBy;
    private LocalDateTime creationDate;
    private LocalDateTime lastModifiedDate;

    @PrePersist
    private void onCreate() {
        this.creationDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastModifiedDate = LocalDateTime.now();
    }
}
