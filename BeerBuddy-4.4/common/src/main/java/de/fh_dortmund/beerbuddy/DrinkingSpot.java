package de.fh_dortmund.beerbuddy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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

    @NotBlank
    @ForeignCollectionField(eager = false)
    List<Person> persons;

    @DatabaseField
    String beschreibung;

    @Temporal(TemporalType.TIMESTAMP)
    @DatabaseField
    Date startTime;

    @Temporal(TemporalType.DATE)
    @DatabaseField
    Date ageFrom;

    @Temporal(TemporalType.TIMESTAMP)
    @DatabaseField
    Date ageTo;

    @DatabaseField
    String gps;
}
