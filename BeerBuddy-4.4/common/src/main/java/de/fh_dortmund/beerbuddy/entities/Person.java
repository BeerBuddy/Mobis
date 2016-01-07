package de.fh_dortmund.beerbuddy.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@JsonIgnoreProperties(ignoreUnknown = true)
public class Person implements Serializable{

    @Id
    @GeneratedValue
    long id;

    @NotBlank
    @Email
    @NonNull
    String email;

    @NotBlank
    @Length(min = 3, max = 30)
    @NonNull
    String username;

    @Lob
    byte[]  image;

    @Length(min = 3, max = 30)
    String password;

    int gender;

    @Temporal(TemporalType.DATE)
    Date dateOfBirth;

    String interests;

    String prefers;

    public static class Gender
    {
        public static final int MALE = 0;
        public static final int FEMALE = 1;
        public static final int OTHER = 2;

    }
}
