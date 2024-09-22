package com.teppichcenter.services;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

public interface Service<M> {
    Collection<M> findAll();
    Collection<M> findAll(Comparator<M> sort);
    Optional<M> findById(Long id);
    boolean save(M teppich);
    boolean deleteAll(Collection<M> teppicheLoeschen);
    boolean delete(M teppicheLoeschen);
    boolean deleteById(Long id);
    int count();
}