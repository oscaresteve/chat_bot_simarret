package edu.alumno.oscaresteve.chat_bot_simarret.service;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import edu.alumno.oscaresteve.chat_bot_simarret.event.ChisteContadoEvent;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChistesContadosService {
  private final AtomicInteger chistesContados = new AtomicInteger(0); // Thread-safe
  private final SseChistesContadosService sseService;

  @EventListener
  public void handleChisteRequestedEvent(ChisteContadoEvent event) {
    int numeroActual = chistesContados.incrementAndGet();
    sseService.sendActualizacionChistesContados(numeroActual);
  }

  public int getNumeroChistesContados() {
    return chistesContados.get();
  }
}
