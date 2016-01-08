package de.fh_dortmund.beerbuddy.entities;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Entity
@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FriendList implements Serializable {
    @Id
    @GeneratedValue
    long id;

    long personid;

    @ManyToMany(fetch = FetchType.EAGER)
    List<Person> friends = new ArrayList<Person>();

    long version;
}
