package de.fh.dortmund.beerbuddy.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by seckinger on 01.09.2015.
 */
@SpringBootApplication
@ImportResource("classpath:spring-config.xml")
public class InfosysRestService extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(InfosysRestService.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(InfosysRestService.class);
    }

}
