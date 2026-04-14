package br.com.joga_together;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class JogaTogetherApplication {

	public static void main(String[] args) {
		SpringApplication.run(JogaTogetherApplication.class, args);
	}

}
