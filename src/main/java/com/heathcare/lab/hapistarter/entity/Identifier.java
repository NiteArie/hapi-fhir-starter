package com.heathcare.lab.hapistarter.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "identifier")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Identifier {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String system;
    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    private PatientEntity patientEntity;

}
