package de.fh_dortmund.beerbuddy.health;

import com.codahale.metrics.health.HealthCheck;

public class PersonHealthCheck extends HealthCheck {
    private final String version;

    public PersonHealthCheck(String version) {
        this.version = version;
    }

    @Override
    protected Result check() throws Exception {
        return Result.healthy();
    }
}
