package com.teppichcenter.services;

import com.teppichcenter.domain.Lager;
import com.teppichcenter.domain.Teppich;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LagerService {


    private static final LagerService INSTANCE = new LagerService();
    public static LagerService getInstance() {return INSTANCE;}
    private final List<Lager> data = new CopyOnWriteArrayList<>();




    public boolean save(Lager lager) {
        if(lager.getId() == null) {
            lager.setId( DbSequence.nextVal() );
            return data.add(lager);
        } else {
            Lager toUpdate = findById(lager.getId()).orElseThrow(() -> new IllegalArgumentException("Lager not found"));
            toUpdate.setId( lager.getId() );
            toUpdate.setName( lager.getName() );
            return true;
        }
    }

    public Optional<Lager> findById(Long id) {
        return data.stream().filter(lager -> lager.getId().equals(id)).findFirst();
    }
}