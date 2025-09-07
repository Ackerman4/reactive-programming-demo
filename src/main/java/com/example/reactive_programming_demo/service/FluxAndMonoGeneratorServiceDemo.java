package com.example.reactive_programming_demo.service;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

// This service is used for demonstration only for creating and accessing flux and mono.

@Slf4j
public class FluxAndMonoGeneratorServiceDemo {

    private Flux<String> names = Flux.just("AJ", "Charles", "Erick", "Jerome", "Jm");

    public Flux<String> namesFlux() {
        log.info("Accessing namesFlux...");
        // Creation of the flux, here we created a flux of string names.
        // Each item or name is emitted sequentially but is concurrently passed into the flatMap, basically transforming each item into its uppercase form.
        // Each item emitted for processing will not be waited by the Flux to be finished transforming before emitting another item to the flatMap operator.
        // Instead, it will emit the following items to the flatMap regardless if the current item in process is finished or not.
        return names
                // FlatMap has a merged flux internally, something like an empty flux that will receive each emitted item after processing.
                // Each emitted item received by the flatMap is received as a raw value at first, but is converted into a Mono/Publisher before processing.
                // The converted Mono/Publisher is subscribed by the flatMap to trigger the process of conversion to uppercase, therefore it undergoes the process.
                // After each Mono emits a value after processing, the flatMap merges the emitted value to the merged flux.
                .flatMap(this::convertToUpperCase).log(); // Each item is emitted here at the flatMap. Each item is passed to the convertToUpperCase method.
                // USE CONCATMAP IF YOU WANT TO PRESERVE THE ORDER OF THE RESULTS DESPITE PROCESSING EACH ITEM CONCURRENTLY.
                // The subscribe method is subscribed to the merged flux. Everytime the merged flux receives an item, it is notified to print the value.
//                .subscribe(System.out::println);
    }

    private Mono<String> convertToUpperCase(String name) {
        // Here, each name received is passed inside the lambda method to undergo the process of logging and conversion to uppercase.
        // First, it creates a Mono/Publisher, then invokes the lambda method for processing the name.
        // After invoking the method, the result is wrapped into a Mono/Publisher.
        return Mono.fromCallable(() -> { // Short for "Create a Mono/Publisher that will wrap the result of the fromCallable lambda method".
            System.out.println("Processing name: " + name); // Logs the current name being processed.
        //    System.out.println("Thread name: " + Thread.currentThread().getName()); // THIS LINE IS USED TO ACTUALLY SEE IF THE METHOD IS ASSIGNED WITH A THREAD.
            return name.toUpperCase(); // Converts the name to its uppercase form then returns it.
        // The method "subscribeOn" states that the method created and invoked in the fromCallable method is assigned
        // with its own thread so that it can run or process in parallel or asynchronous with the main thread.
        // Each Mono/Publisher now has its own thread for execution.
        }).subscribeOn(Schedulers.parallel());

//        The implementation above is almost the same as the implementation below.
//        But the implementation above is much more cleaner.
//        System.out.println("Processing name: " + name);
//        return Mono.just(name.toUpperCase())
//                .subscribeOn(Schedulers.parallel());
    }

    public Flux<String> usingMapToAlterNamesFlux() {
        return names.map(String::toLowerCase)
                .log();
    }

    public Flux<String> usingFilterToNamesFlux() {
        return names.filter(name -> name.length() > 3)
                .log();
    }

    public Flux<String> usingFlatMapToNamesFlux() {
        return names.filter(name -> name.length() < 3)
                .flatMap(name -> {
                    String[] nameCharArray = name.split("");
                    return Flux.fromArray(nameCharArray);
                })
                .log();
    }

    public Flux<String> usingFlatMapWithDelay() {
        return names.filter(name -> name.length() < 3)
                .flatMap(name -> {
                   String[] nameCharArray = name.split("");
                   var millis = new Random().nextInt(1000);
                   return Flux.fromArray(nameCharArray)
                           .delayElements(Duration.ofMillis(millis));
                })
                .log();
    }

    public Flux<String> usingConcatMap() {
        return names.filter(name -> name.length() > 3)
                .map(String::toUpperCase)
                .concatMap(name -> {
                    String[] nameCharArray = name.split("");
                    var millis = 1000;
                    return Flux.fromArray(nameCharArray)
                            .delayElements(Duration.ofMillis(millis));
                })
                .log();
    }

    public Mono<List<String>> usingFlatMapInMono() {
        return Mono.just("Morales")
                .map(String::toUpperCase)
                .flatMap(name -> {
                    List<String> nameChar = Arrays.stream(name.split("")).toList();
                    return Mono.just(nameChar);
                    // return Flux.fromIterable(nameChar) // Remember that this has a Merged Flux, that is a Flux<String>
                    //        .collectList(); // This method converts the Flux<String> into a List<String> first, then converts it into a Mono. So it will be Mono<List<String>>
                })
                .log();
    }

    public Flux<String> usingFlatMapMany() {
        // Purpose is to convert a Mono into a Flux, for example, converting the String into a list of individual character.
        return Mono.just("Madronero") // Declaration of Mono String.
                .map(String::toUpperCase) // Convert the String into its uppercase transformation.
                .flatMapMany(name -> { // The converted String will now undergo the flatMapMany operator.
                    List<String> nameChars = Arrays.stream(name.split("")).toList(); // Convert the string into a list of characters.
                    return Flux.fromIterable(nameChars); // Wrap the list of characters into a Flux.
                })
                .log();
    }

    public Flux<String> usingTransform() {
        // In using Transform, the concept is that the parameter for the transform operator is a method/function.
        // So it is ideal to declare a function containing the expected input and expected output, and also giving it a method implementation
        // before passing it as a parameter.
        Function<Flux<String>, Flux<String>> filterStringLength = name -> name.map(String::toUpperCase)
                    .filter(upperCaseName -> upperCaseName.length() > 3);

        return names.transform(filterStringLength)
                .flatMap(Mono::just)
                .log();
    }

    public Flux<String> usingDefaultIfEmpty() {
        return names.filter(name -> name.length() > 10)
                .defaultIfEmpty("No names more than the length of 10")
                .log();
    }

    public Flux<String> usingSwitchIfEmpty() {
        return names.filter(name -> name.length() > 10)
                .switchIfEmpty(Mono.just("No name is more than the length of 10"))
                .log();
    }

    public Mono<String> nameMono() {
        log.info("Accessing nameMono...");
        return Mono.just("Morales");
    }
}
