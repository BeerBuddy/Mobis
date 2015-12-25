package de.fh_dortmund.beerbuddy;

import org.hibernate.validator.constraints.NotEmpty;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.dropwizard.Configuration;
import io.dropwizard.db.*;

public class ServerConfig extends Configuration {
	
	@NotEmpty
	private String version;
	
	@JsonProperty
	public String getVersion(){
		return version;
	}
	
	@JsonProperty
	public void setVersion(String version){
		this.version = version;
	}

	@Valid
	@NotNull
	private DataSourceFactory database = new DataSourceFactory();

	@JsonProperty("database")
	public DataSourceFactory getDataSourceFactory() {
		return database;
	}

	@JsonProperty("database")
	public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
		this.database = dataSourceFactory;
	}
}
