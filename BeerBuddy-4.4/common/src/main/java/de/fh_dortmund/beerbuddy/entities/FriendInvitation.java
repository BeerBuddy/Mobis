package de.fh_dortmund.beerbuddy.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * Created by David on 25.11.2015.
 */
@DatabaseTable(tableName = "friendinvitation")
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
    @DatabaseField(canBeNull = false, index = true)
    Long einladerId;


    @NotNull
    @DatabaseField(canBeNull = false, index = true)
    Long eingeladenerId;

    @DatabaseField
    String freitext;

    @DatabaseField
    long version;
}
