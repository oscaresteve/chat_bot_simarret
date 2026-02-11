package edu.alumno.oscaresteve.chat_bot_simarret.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import edu.alumno.oscaresteve.chat_bot_simarret.service.ChistesContadosService;
import edu.alumno.oscaresteve.chat_bot_simarret.service.SseChistesContadosService;

@RestController
@RequestMapping("api/sse")
public class SseChistesContadosController {
  private final SseChistesContadosService sseService;
  private final ChistesContadosService counterService;

  public SseChistesContadosController(SseChistesContadosService sseService, ChistesContadosService counterService) {
    this.sseService = sseService;
    this.counterService = counterService;
  }

  @GetMapping(value = "/counter", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter streamContadorChistes() {
    SseEmitter emitter = new SseEmitter(5_000L); // Timeout 5 segundos
    sseService.addEmitter(emitter);
    sseService.sendActualizacionChistesContados(counterService.getNumeroChistesContados()); // Envía valor inicial
    return emitter;
  }
}
