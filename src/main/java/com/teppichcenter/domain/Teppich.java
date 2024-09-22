package com.teppichcenter.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)

public class Teppich extends AbstractEntity {
    private String bezeichnung;
    private String farbe;
    private double preis;
    private int menge;
    private Long fkLager;

    public Teppich(){}

    public Teppich(String bezeichnung, String farbe, double preis, int menge) {
        setBezeichnung(bezeichnung);
        setFarbe(farbe);
        setPreis(preis);
        setMenge(menge);
    }

    public Teppich(String bezeichnung, String farbe, double preis, int menge, Long fkLager) {
        setBezeichnung(bezeichnung);
        setFarbe(farbe);
        setPreis(preis);
        setMenge(menge);
        setFkLager(fkLager);
    }
}