package top.cinema.app;

import org.springframework.boot.SpringApplication;

public class TestCinemaAppApplication {

	public static void main(String[] args) {
		SpringApplication.from(CinemaAppApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
