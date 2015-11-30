package de.fh_dortmund.beerbuddy.health;

import com.codahale.metrics.health.HealthCheck;

import de.fh_dortmund.beerbuddy.persistence.PersonDB;

public class PersonHealthCheck extends HealthCheck {
	private final String version;

	public PersonHealthCheck(String version) {
		this.version = version;
	}

	@Override
	protected Result check() throws Exception {
		if (PersonDB.getCount() == 0) {
			return Result.unhealthy("No persons in DB! Version: " +
					this.version);
		}
		return Result.healthy("OK with version: " + this.version +
				". Persons count: " + PersonDB.getCount());
	}
}
