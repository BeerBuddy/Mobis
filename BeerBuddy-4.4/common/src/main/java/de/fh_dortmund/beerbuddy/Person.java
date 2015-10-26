package de.fh_dortmund.beerbuddy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@EqualsAndHashCode
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Person {
    @Id
    @GeneratedValue
    private long id;

    @NotBlank
    @Length(min = 3, max = 30)
    @NonNull
    private String firstName;

    @NotBlank
    @Length(min = 3, max = 30)
    @NonNull
    private String lastName;

}
