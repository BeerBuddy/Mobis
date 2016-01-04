package de.fh_dortmund.beerbuddy.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Created by David on 25.11.2015.
 */
@Entity
@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrinkingSpot {

    @Id
    @GeneratedValue
    long id;

    @ForeignCollectionField(eager = false)
    @OneToOne(fetch = FetchType.EAGER)
    Person creator;


    @ForeignCollectionField(eager = false)
    @ManyToMany(fetch = FetchType.EAGER)
    List<Person> persons;

    @DatabaseField
    String beschreibung;

    @Temporal(TemporalType.TIMESTAMP)
    @DatabaseField
    Date startTime;

    @DatabaseField
    int ageFrom;

    @DatabaseField
    int ageTo;

    @DatabaseField
    String gps;

    @DatabaseField
    int amountMaleWithoutBeerBuddy;

    @DatabaseField
    int amountFemaleWithoutBeerBuddy;

    public int getTotalAmount() {
        return persons.size() + amountFemaleWithoutBeerBuddy + amountMaleWithoutBeerBuddy;
    }


}
