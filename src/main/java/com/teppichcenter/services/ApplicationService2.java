package com.teppichcenter.services;
import com.teppichcenter.domain.Lager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Stream;

@Transactional(readOnly = true)
@Service
public class ApplicationService2 {
    private final LagerRepository lagerRepository;
    public ApplicationService2(@Autowired LagerRepository lagerRepository){
            this.lagerRepository = lagerRepository;
    }
    public Stream<Lager> findAll(Pageable pageable) {
        return lagerRepository.findAll( pageable ).stream();
    }
    public void deleteLager(Set<Lager> lagerSet) {
        lagerRepository.deleteAll(lagerSet);
    }
}