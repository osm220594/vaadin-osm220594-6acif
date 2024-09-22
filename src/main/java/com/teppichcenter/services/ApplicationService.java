package com.teppichcenter.services;
import com.teppichcenter.domain.Teppich;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Stream;

@Transactional(readOnly = true)
@Service
public class ApplicationService {
    private final TeppichRepository teppichRepository;
    public ApplicationService(@Autowired TeppichRepository teppichRepository){
            this.teppichRepository = teppichRepository;
    }
    public Stream<Teppich> findAll(Pageable pageable) {
        return teppichRepository.findAll( pageable ).stream();
    }
    public void deleteTeppiche(Set<Teppich> teppichSet) {
        teppichRepository.deleteAll(teppichSet);
    }
}