package io.ankush.kap_mini.domain;

import io.ankush.kap_mini.model.FieldType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Field {

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "char(36)")
    @GeneratedValue
    @UuidGenerator
    private UUID fieldId;

    @Column(nullable = false)
    private String fieldName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FieldType fieldType;

    @Column(nullable = false)
    private String defaultValue;

    @Column
    private String validationRules;

    @Column(nullable = false, columnDefinition = "tinyint", length = 1)
    private Boolean isRequired;

    @Column
    private String deleteAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "form_id_id", nullable = false)
    private Form formId;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
