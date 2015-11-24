package de.fh_dortmund.beerbuddy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;
import java.util.List;

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
@JsonIgnoreProperties(ignoreUnknown = true)
public class FriendList {
    @Id
    @GeneratedValue
    long id;

    @NotBlank
    @DatabaseField(canBeNull = false, index=true)
    long personid;

    @NotBlank
    @ForeignCollectionField(eager = false)
    List<Person> friends;

    @NotBlank
    @ForeignCollectionField(eager = false)
    List<Person> requests;

}
