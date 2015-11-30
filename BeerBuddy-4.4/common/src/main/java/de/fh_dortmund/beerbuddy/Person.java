package de.fh_dortmund.beerbuddy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Entity
@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@RequiredArgsConstructor(suppressConstructorProperties = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Person {

    @Id
    @GeneratedValue
    long id;

    @NotBlank
    @Email
    @NonNull
    @DatabaseField
    String email;

    @NotBlank
    @Length(min = 3, max = 30)
    @NonNull
    @DatabaseField
    String username;

    @Lob
    @DatabaseField(dataType= DataType.BYTE_ARRAY)
    byte[]  image;

    @Length(min = 3, max = 30)
    @DatabaseField
    String password;

    @DatabaseField
    int gender;

    @Temporal(TemporalType.DATE)
    @DatabaseField
    Date dateOfBirth;

    @DatabaseField
    String interests;

    @DatabaseField
    String prefers;

    public static class Gender
    {
        public static final int MALE = 1;
        public static final int FEMALE = 2;

    }
}
