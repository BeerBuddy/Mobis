package de.fh_dortmund.beerbuddy.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class FriendInvitation implements Serializable{

    @Id
    @GeneratedValue
    long id;

    @NotNull
    Long einladerId;


    @NotNull
    Long eingeladenerId;

    String freitext;
}
