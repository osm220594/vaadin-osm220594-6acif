package com.teppichcenter.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//@Table("EMPLOYEE")
@Setter
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Person extends AbstractEntity {

//    @Column(value = "erster_name")
    private String firstName;

    private String lastName;

    private Long fkCompany;

    public Person(String firstName, String lastName) {
        setFirstName(firstName);
        setLastName(lastName);
    }
}
