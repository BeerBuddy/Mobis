package de.fh.dortmund.beerbuddy.backend.entity.person;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by seckinger on 14.09.2015.
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode
public class Person {
    @Id
    @NotBlank
    @Length(min = 3, max = 30)
    private String ldap;

    @NotBlank
    @Length(min = 3, max = 30)
    private String firstName;

    @NotBlank
    @Length(min = 3, max = 30)
    private String lastName;

}
