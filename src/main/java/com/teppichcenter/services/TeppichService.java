package com.teppichcenter.services;

import com.teppichcenter.domain.Teppich;
import com.teppichcenter.domain.Color;

import java.util.*;
import java.util.function.Predicate;


public class TeppichService implements Service<Teppich> {

    private static final TeppichService INSTANCE = new TeppichService();
    public static TeppichService getInstance() {
        return INSTANCE;
    }

    private final List<Teppich> data = new ArrayList<>();
    private final List<Color> colors = new ArrayList<>();


    private TeppichService() {

        colors.addAll(List.of(
                new Color("Beige"),
                new Color("Blau"),
                new Color("Braun"),
                new Color("Creme"),
                new Color("Gelb"),
                new Color("Gold"),
                new Color("Grau"),
                new Color("Grün"),
                new Color("Lavendel"),
                new Color("Oliv"),
                new Color("Orange"),
                new Color("Pink"),
                new Color("Rot"),
                new Color("Silber"),
                new Color("Türkis"),
                new Color("Violett"),
                new Color("Weiss")
        ));
    }
    public List<Color> findAllColors() {
        return colors;
    }
    public List<Color> findAllColors(Comparator<Color> sortBy) {
        List<Color> sortedData = new ArrayList<>(colors);
        sortedData.sort( sortBy );
        return sortedData;
    }

    @Override
    public Collection<Teppich> findAll() {
        return data;
    }
    @Override
    public Collection<Teppich> findAll(Comparator<Teppich> sort) {
        List<Teppich> sorted = new ArrayList<>(data);
        sorted.sort(sort);
        return sorted;
    }
    @Override
    public Optional<Teppich> findById(Long id) {
        if(id == null)
            throw new IllegalArgumentException("Id must not be null");

        return data.stream()
                .filter(teppich -> id.equals( teppich.getId() ))
                .findFirst();
    }

    @Override
    public boolean save(Teppich teppich) {
        if(teppich.getId() == null) {
            teppich.setId( DbSequence.nextVal() );
            return data.add(teppich);
        } else {
            Teppich toUpdate = findById(teppich.getId()).orElseThrow();
            toUpdate.setBezeichnung( teppich.getBezeichnung() );
            toUpdate.setPreis( teppich.getPreis() );
            toUpdate.setFarbe( teppich.getFarbe() );
            toUpdate.setFkLager( teppich.getFkLager() );
            return true;
        }
    }


    @Override
    public boolean deleteAll(Collection<Teppich> teppicheLoeschen) {
        return data.removeAll( teppicheLoeschen );
    }
    @Override
    public boolean delete(Teppich teppicheLoeschen) {
        return data.remove( teppicheLoeschen );
    }
    @Override
    public boolean deleteById(Long id) {
        if(id == null)
            throw new IllegalArgumentException("Id must not be null");

        return data.removeIf(teppich -> id.equals( teppich.getId() ));
    }
    @Override
    public int count() {
        return data.size();
    }

    public Optional<Color> findColorsByChar(String colorString) {
        return colors.stream().filter(new Predicate<Color>() {
            @Override
            public boolean test(Color color) {
                return color.getLabel() == colorString;
            }
        }).findAny();

    }
}