package com.teppichcenter.services;

import com.teppichcenter.domain.Teppich;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;
public interface TeppichRepository
        extends CrudRepository<Teppich, Long>,
        ListPagingAndSortingRepository<Teppich, Long> {
}