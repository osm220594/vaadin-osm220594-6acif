package com.teppichcenter.services;

import com.teppichcenter.domain.Company;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CompanyRepository extends CrudRepository<Company, Long> {

    // Beispiel: Definition von eigenen Queries im Repository
    // Manuelle 1 ... n Implementierung


    @Modifying  // Alle Änderungen (insert, update, delete) in der DB benötigen die Annotation @Modifying
    @Query(value = """
        UPDATE PERSON per 
        SET per.FK_COMPANY = :fkCompany 
        WHERE per.ID = :personId
    """)
    void hirePerson(@Param("fkCompany") Long fkCompany,
                    @Param("personId") Long personId);
}
