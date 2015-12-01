package de.fh_dortmund.beerbuddy.persistence;

import java.util.*;

import de.fh_dortmund.beerbuddy.Person;

public class PersonDB {
	private static Map<Long, Person> persons = new HashMap<>();

	static {
		persons.put(1L, new Person("email1@email.com", "user1"));
		persons.put(2L, new Person("email2@email.com", "user2"));
		persons.put(3L, new Person("email3@email.com", "user3"));
		persons.put(4L, new Person("email4@email.com", "user4"));
	}

	public static Person getById(int id) {
		return persons.get(id);
	}

	public static List<Person> getAll() {
		List<Person> result = new ArrayList<Person>();
		for (Long key : persons.keySet()) {
			result.add(persons.get(key));
		}
		return result;
	}

	public static int getCount() {
		return persons.size();
	}

	public static void remove() {
		if (!persons.keySet().isEmpty()) {
			persons.remove(persons.keySet().toArray()[0]);
		}
	}

	public static String save(Person person) {
		String result = "";
		if (persons.get(person.getId()) != null) {
			result = "Updated _Person with id=" + person.getId();
		} else {
			result = "Added _Person with id=" + person.getId();
		}
		persons.put(person.getId(), person);
		return result;
	}
}
