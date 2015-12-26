package de.fh_dortmund.beerbuddy.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.j256.ormlite.field.DatabaseField;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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
public class FriendInvitation {


    @Id
    @GeneratedValue
    long id;

    @NotBlank
    @DatabaseField(canBeNull = false, index=true)
    long einladerId;


    @NotBlank
    @DatabaseField(canBeNull = false, index=true)
    long eingeladenerId;

    @DatabaseField
    String freitext;

}
