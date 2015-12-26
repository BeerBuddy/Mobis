package de.fh_dortmund.beerbuddy.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


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

}
