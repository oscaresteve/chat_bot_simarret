package edu.alumno.oscaresteve.chat_bot_simarret.service;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class SseChistesContadosService {

  private final Set<SseEmitter> emitters = ConcurrentHashMap.newKeySet();

  public void addEmitter(SseEmitter emitter) {
    emitters.add(emitter);

    emitter.onCompletion(() -> emitters.remove(emitter));
    emitter.onTimeout(() -> emitters.remove(emitter));
  }

  public void sendActualizacionChistesContados(int count) {
    emitters.forEach(emitter -> {
      try {
        emitter.send(SseEmitter.event().data(count));
      } catch (IOException e) {
        emitter.complete();
        emitters.remove(emitter);
      }
    });
  }
}
