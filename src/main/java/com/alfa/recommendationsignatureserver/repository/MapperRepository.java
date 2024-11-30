package com.alfa.recommendationsignatureserver.repository;

import com.alfa.recommendationsignatureserver.entity.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MapperRepository extends JpaRepository<Mapper, String> {
    Mapper findByClientId(String clientId);
}
