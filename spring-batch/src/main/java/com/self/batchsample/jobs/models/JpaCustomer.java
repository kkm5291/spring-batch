package com.self.batchsample.jobs.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "jpa_customer")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class JpaCustomer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private Integer age;
    private String gender;
}
