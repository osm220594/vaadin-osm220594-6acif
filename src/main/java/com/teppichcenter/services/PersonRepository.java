package com.teppichcenter.services;

import com.teppichcenter.domain.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;


public interface PersonRepository
        extends CrudRepository<Person, Long>,
        ListPagingAndSortingRepository<Person, Long> {
}
