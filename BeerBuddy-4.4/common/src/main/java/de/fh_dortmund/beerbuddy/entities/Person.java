package de.fh_dortmund.beerbuddy.entities;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@Entity
@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@RequiredArgsConstructor(suppressConstructorProperties = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class Person implements Serializable{

    @Id
    @GeneratedValue
    long id;

    @NotBlank
    @Email
    @NonNull
    String email;


    String username;

    @Lob
    byte[]  image;

    @NotBlank
    @NonNull
    @Length(min = 3, max = 30)
    String password;

    int gender;

    @Temporal(TemporalType.DATE)
    Date dateOfBirth;

    String interests;

    String prefers;

    long version;

    public static class Gender
    {
        public static final int MALE = 0;
        public static final int FEMALE = 1;
        public static final int OTHER = 2;

    }
}
