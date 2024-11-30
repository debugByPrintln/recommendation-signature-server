package com.alfa.recommendationsignatureserver.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Mapper {

    @Id
    private String clientId;
    private int counter;
}
