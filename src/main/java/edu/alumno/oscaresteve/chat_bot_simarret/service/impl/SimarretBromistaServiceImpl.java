package edu.alumno.oscaresteve.chat_bot_simarret.service.impl;

import java.time.Duration;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import edu.alumno.oscaresteve.chat_bot_simarret.ExternalApiException;
import edu.alumno.oscaresteve.chat_bot_simarret.event.ChisteContadoEvent;
import edu.alumno.oscaresteve.chat_bot_simarret.service.SimarretBromistaService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SimarretBromistaServiceImpl implements SimarretBromistaService {

  private final WebClient webClient;
  private final ApplicationEventPublisher eventPublisher;

  @Override
  public Mono<String> getPresentacionMono() {
    return webClient.get()
        .uri("/getPresentacionAlUsuarioMonoResponse")
        .retrieve()
        .bodyToMono(String.class)
        .timeout(Duration.ofSeconds(5))
        .onErrorResume(e -> Mono.error(ExternalApiException.handleException(e,
            "getPresentacionAlUsuarioMonoResponse")));
  }

  // Si leemos de un endpoint Spring boot un Flux<String> puede que nos quite los
  // espacios,
  // para evitarlo podemos hacer lo siguiente: Leer el Mono<String> y convertirlo
  // en un Flux<String>
  // con un carácter por emisión, de esta forma se mantendrán los espacios.
  // Si desde Sprint Boot llamamos a un endpoint que devuelve un Mono<String> y
  // queremos convertirlo en un
  // Flux<String> para enviarlo a un cliente que espera un stream de texto,
  // podemos hacerlo de la siguiente manera:
  // Flux<String> flux = mono.flatMapMany(text ->
  // Flux.fromStream(text.codePoints().mapToObj(c -> String.valueOf((char) c))));
  public Flux<String> getPresentacionFluxFromMono() {
    return webClient.get()
        .uri("/getPresentacionAlUsuarioMonoResponse")
        .retrieve()
        .bodyToMono(String.class)
        .timeout(Duration.ofSeconds(5))
        .onErrorResume(e -> Mono
            .error(ExternalApiException.handleException(e, "getPresentacionAlUsuarioMonoResponse")))
        .flatMapMany(text -> Flux.fromStream(
            text.codePoints()
                .mapToObj(c -> String.valueOf((char) c))))
        .delayElements(Duration.ofMillis(35)); // Simula la escritura humana con un pequeño retraso
    // .doOnNext(ch -> System.out.println("Enviando carácter: [" + ch + "]"));
  }

  @Override
  public Mono<String> getChisteMono(String tema) {
    eventPublisher.publishEvent(new ChisteContadoEvent(this)); // Notifica evento: nuevo chiste solicitado
    return webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/getChisteMonoResponse")
            .queryParam("tema", tema)
            .build())
        .retrieve()
        .bodyToMono(String.class)
        .timeout(Duration.ofSeconds(5))
        .onErrorResume(e -> Mono.error(
            ExternalApiException.handleException(e, "getChisteMonoResponse")));
  }

  // Si leemos de un endpoint Spring boot un Flux<String> puede que nos quite los
  // espacios,
  // para evitarlo podemos hacer lo siguiente: Leer el Mono<String> y convertirlo
  // en un Flux<String>
  // con un carácter por emisión, de esta forma se mantienen todos los espacios.
  // Si desde Sprint Boot llamamos a un endpoint que devuelve un Mono<String> y
  // queremos convertirlo en un
  // Flux<String> para enviarlo a un cliente que espera un stream de texto,
  // podemos hacerlo de la siguiente manera:
  // Flux<String> flux = mono.flatMapMany(text ->
  // Flux.fromStream(text.codePoints().mapToObj(c -> String.valueOf((char) c))));

  @Override
  public Flux<String> getChisteFluxFromMono(String tema) {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/getChisteMonoResponse")
            .queryParam("tema", tema)
            .build())
        .accept(MediaType.TEXT_EVENT_STREAM)
        .retrieve()
        .bodyToMono(String.class)
        .timeout(Duration.ofSeconds(5))
        .onErrorResume(e -> Mono
            .error(ExternalApiException.handleException(e, "getChisteMonoResponse")))
        .flatMapMany(text -> Flux.fromStream(
            text.codePoints()
                .mapToObj(c -> String.valueOf((char) c))))
        .delayElements(Duration.ofMillis(35)) // Simula la escritura humana con un pequeño retraso
        .doOnNext(ch -> System.out.println("Enviando carácter: [" + ch + "]"));
  }

}
