package com.teppichcenter.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class Lager extends AbstractEntity {
    private String name;

    public Lager(){}
    public Lager(String name){
        setName(name);
    }
}