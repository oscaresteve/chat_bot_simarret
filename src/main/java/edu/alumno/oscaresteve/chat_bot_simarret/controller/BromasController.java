package edu.alumno.oscaresteve.chat_bot_simarret.controller;

import java.time.Duration;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.alumno.oscaresteve.chat_bot_simarret.service.impl.SimarretBromistaServiceImpl;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/bromas")
@RequiredArgsConstructor
public class BromasController {

  private final SimarretBromistaServiceImpl bromistaService;

  @GetMapping("/presentacion-mono")
  public Mono<String> getPresentacionMono() {
    return bromistaService.getPresentacionMono();
  }

  @GetMapping(value = "/presentacion-flux")
  public Flux<String> getPresentacionFlux() {
    // Leemos un Mono<String> y convertimos en un Flux<String> con un carácter por
    // emisión.
    // Al retardo le añadimos 35 ms para simular la escritura humana
    return bromistaService.getPresentacionFluxFromMono()
        .delayElements(Duration.ofMillis(35)); // Ajusta el delay según necesites
  }

  @GetMapping("/chiste-mono")
  public Mono<String> getChisteMono(@RequestParam String tema) {
    return bromistaService.getChisteMono(tema);
  }

  @GetMapping(value = "/chiste-flux")
  public Flux<String> getChisteFlux(@RequestParam String tema) {
    // Leemos un Flux<Character> y convertimos en un Flux<String> con un carácter
    // por emisión.
    // Al retardo le añadimos 35 ms para simular la escritura humana
    // return bromistaService.getChisteFluxFromCharacter(tema)
    return bromistaService.getChisteFluxFromMono(tema)
        .delayElements(Duration.ofMillis(35)); // Ajusta el delay según necesites
  }
}
