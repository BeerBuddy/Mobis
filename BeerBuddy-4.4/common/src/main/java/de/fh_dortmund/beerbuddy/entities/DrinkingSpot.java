package de.fh_dortmund.beerbuddy.entities;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.validation.constraints.NotNull;

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
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrinkingSpot implements Serializable {

    @Id
    @GeneratedValue
    long id;

    @OneToOne(fetch = FetchType.EAGER)
    @NotNull
    Person creator;

    @ManyToMany(fetch = FetchType.EAGER)
    List<Person> persons = new ArrayList<Person>();

    String beschreibung;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    Date startTime;

    int ageFrom;

    int ageTo;

    @NotNull
    String gps;

    int amountMaleWithoutBeerBuddy;

    int amountFemaleWithoutBeerBuddy;

    boolean active = true;

    long version;

    @JsonIgnore
    public int getTotalAmount() {
        return amountFemaleWithoutBeerBuddy + amountMaleWithoutBeerBuddy;
    }
}
