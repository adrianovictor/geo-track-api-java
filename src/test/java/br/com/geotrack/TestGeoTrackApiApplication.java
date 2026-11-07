package br.com.geotrack;

import org.springframework.boot.SpringApplication;

public class TestGeoTrackApiApplication {
    public static void main(String[] args) {
        SpringApplication.from(GeoTrackApiApplication::main)
                .with(TestcontainersConfiguration.class)
                .run(args);
    }
}
