package edu.alumno.oscaresteve.chat_bot_simarret.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SimarretBromistaService {
  public Mono<String> getPresentacionMono();

  public Flux<String> getPresentacionFluxFromMono();

  public Mono<String> getChisteMono(String tema);

  public Flux<String> getChisteFluxFromMono(String tema);
}
