package com.heathcare.lab.hapistarter.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.ReadOnlyProperty;

import javax.persistence.Embeddable;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.stream.Stream;

@Embeddable
@Data
@Builder
public class PersonNameEntity {
    private String firstName;
    private String lastName;
    private String middleName;

    @ReadOnlyProperty
    private String fullName;

    @PrePersist
    @PreUpdate
    void updateFullName(){
        this.fullName = String.join(" ", Stream.of(firstName,middleName,lastName).filter(v -> !v.isEmpty()).toList());
    }
}
