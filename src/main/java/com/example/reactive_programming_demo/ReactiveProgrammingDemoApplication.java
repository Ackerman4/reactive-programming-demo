package com.example.reactive_programming_demo;

import com.example.reactive_programming_demo.service.FluxAndMonoGeneratorServiceDemo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class ReactiveProgrammingDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveProgrammingDemoApplication.class, args);

        FluxAndMonoGeneratorServiceDemo fluxAndMonoGeneratorService = new FluxAndMonoGeneratorServiceDemo();
        fluxAndMonoGeneratorService.namesFlux();

//        fluxAndMonoGeneratorService.nameMono()
//                .subscribe(System.out::println);
	}
}
