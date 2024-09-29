package com.teppichcenter.services;

import com.teppichcenter.domain.Lager;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


public interface LagerRepository extends CrudRepository<Lager, Long>, ListPagingAndSortingRepository<Lager, Long>{

    @Modifying  // Alle Änderungen (insert, update, delete) in der DB benötigen die Annotation @Modifying
    @Query(value = """
        UPDATE TEPPICH teppich 
        SET teppich.FK_LAGER = :fkLager
        WHERE teppich.ID = :teppichId
    """)
    void lagereTeppich(@Param("fkLager") Long fkLager,
                       @Param("teppichId") Long teppichId);
}