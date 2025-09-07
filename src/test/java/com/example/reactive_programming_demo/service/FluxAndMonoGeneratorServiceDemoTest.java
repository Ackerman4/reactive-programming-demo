package com.example.reactive_programming_demo.service;

import com.sun.jdi.ArrayReference;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FluxAndMonoGeneratorServiceDemoTest {

    FluxAndMonoGeneratorServiceDemo fluxAndMonoGeneratorServiceDemo;

    @Test
    void namesFlux() {
        fluxAndMonoGeneratorServiceDemo = new FluxAndMonoGeneratorServiceDemo();
        var namesFluxResult = fluxAndMonoGeneratorServiceDemo.namesFlux();

        // This unit testing part creates a step verifier
        StepVerifier.create(namesFluxResult)
                // Validates if the items being consumed is contained in the list.
                .expectNextMatches(name -> Arrays.asList("JM", "JEROME", "ERICK", "CHARLES", "AJ")
                        .contains(name))
                // The first item consumption may stop the assessment, so expectNextCount is used to inform the step verifier
                // That there are 4 more items to come and to be assessed.
                .expectNextCount(4)
                .expectComplete()
                .verify();

    }

    @Test
    void usingMapToAlterNamesFlux() {
        fluxAndMonoGeneratorServiceDemo = new FluxAndMonoGeneratorServiceDemo();
        var lowerCasedNames = fluxAndMonoGeneratorServiceDemo.usingMapToAlterNamesFlux();

        StepVerifier.create(lowerCasedNames)
                .expectNext("aj", "charles", "erick", "jerome", "jm")
                .verifyComplete();
    }

    @Test
    void usingFilterToNamesFlux() {
        fluxAndMonoGeneratorServiceDemo = new FluxAndMonoGeneratorServiceDemo();
        var filteredNames = fluxAndMonoGeneratorServiceDemo.usingFilterToNamesFlux();

        StepVerifier.create(filteredNames)
                .expectNext("Charles", "Erick", "Jerome")
                .verifyComplete();
    }

    @Test
    void usingFlatMapToNamesFlux() {
        fluxAndMonoGeneratorServiceDemo = new FluxAndMonoGeneratorServiceDemo();
        var fluxNameChar = fluxAndMonoGeneratorServiceDemo.usingFlatMapToNamesFlux();

        StepVerifier.create(fluxNameChar)
                .expectNext("A", "J", "J", "m")
                .verifyComplete();

    }

    @Test
    void usingFlatMapWithDelay() {
        fluxAndMonoGeneratorServiceDemo = new FluxAndMonoGeneratorServiceDemo();
        var fluxNameChar = fluxAndMonoGeneratorServiceDemo.usingFlatMapWithDelay();

        StepVerifier.create(fluxNameChar)
                .expectNextCount(4)
                .verifyComplete();
    }

    @Test
    void usingConcatMap() {
        fluxAndMonoGeneratorServiceDemo = new FluxAndMonoGeneratorServiceDemo();
        var fluxNameChar = fluxAndMonoGeneratorServiceDemo.usingConcatMap();

        StepVerifier.create(fluxNameChar)
                .expectNextMatches(nameChar -> Arrays.asList("C", "H", "A", "R", "L", "E", "S", "E", "R", "I", "C", "K", "J", "E", "R", "O", "M", "E")
                        .contains(nameChar))
                .expectNextCount(17)
                .expectComplete()
                .verify();
    }

    @Test
    void usingFlatMapInMono() {
        fluxAndMonoGeneratorServiceDemo = new FluxAndMonoGeneratorServiceDemo();
        var charList = fluxAndMonoGeneratorServiceDemo.usingFlatMapInMono();

        StepVerifier.create(charList)
                .expectNext(List.of("M", "O", "R", "A", "L", "E", "S"))
                .verifyComplete();
    }

    @Test
    void usingFlatMapMany() {
        fluxAndMonoGeneratorServiceDemo = new FluxAndMonoGeneratorServiceDemo();
        var charList = fluxAndMonoGeneratorServiceDemo.usingFlatMapMany();

        StepVerifier.create(charList)
                .expectNext("M", "A", "D", "R", "O", "N", "E", "R", "O")
                .verifyComplete();
    }

    @Test
    void usingTransform() {
        fluxAndMonoGeneratorServiceDemo = new FluxAndMonoGeneratorServiceDemo();
        var filteredNames = fluxAndMonoGeneratorServiceDemo.usingTransform();

        StepVerifier.create(filteredNames)
                .expectNext("CHARLES", "ERICK", "JEROME")
                .verifyComplete();
    }

    @Test
    void usingDefaultIfEmpty() {
        fluxAndMonoGeneratorServiceDemo = new FluxAndMonoGeneratorServiceDemo();
        var emptyFlux = fluxAndMonoGeneratorServiceDemo.usingDefaultIfEmpty();

        StepVerifier.create(emptyFlux)
                .expectNext("No names more than the length of 10")
                .verifyComplete();
    }

    @Test
    void usingSwitchIfEmpty() {
        fluxAndMonoGeneratorServiceDemo = new FluxAndMonoGeneratorServiceDemo();
        var emptyFlux = fluxAndMonoGeneratorServiceDemo.usingSwitchIfEmpty();

        StepVerifier.create(emptyFlux)
                .expectNext("No name is more than the length of 10")
                .verifyComplete();
    }
}