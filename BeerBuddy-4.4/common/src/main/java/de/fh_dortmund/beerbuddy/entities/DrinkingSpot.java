package de.fh_dortmund.beerbuddy.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
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
public class DrinkingSpot implements Serializable {

    @Id
    @GeneratedValue
    long id;

    @OneToOne(fetch = FetchType.EAGER)
    Person creator;

    @ManyToMany(fetch = FetchType.EAGER)
    List<Person> persons;

    String beschreibung;

    @Temporal(TemporalType.TIMESTAMP)
    Date startTime;

    int ageFrom;

    int ageTo;

    String gps;

    int amountMaleWithoutBeerBuddy;

    int amountFemaleWithoutBeerBuddy;

    boolean active = true;

    long version;

    public int getTotalAmount() {
        return persons.size() + amountFemaleWithoutBeerBuddy + amountMaleWithoutBeerBuddy;
    }
}